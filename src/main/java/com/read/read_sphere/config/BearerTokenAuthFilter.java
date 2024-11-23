package com.read.read_sphere.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.read.read_sphere.DTOs.OAuthTokenResponse;
import com.read.read_sphere.model.User;
import com.read.read_sphere.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class BearerTokenAuthFilter {

    private static final Logger logger = LoggerFactory.getLogger(BearerTokenAuthFilter.class);
    @Autowired
    private UserRepository userRepository;

    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    public String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "openid https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
        params.add("grant_type", "authorization_code");
        params.add("access_type", "online");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        String url = "https://oauth2.googleapis.com/token";

        try {
            ResponseEntity<OAuthTokenResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, OAuthTokenResponse.class
            );
            OAuthTokenResponse tokenResponse = response.getBody();

            if (tokenResponse != null) {
                return tokenResponse.getAccess_token();
            } else {
                logger.error("OAuth token response is null.");
                throw new IllegalStateException("Failed to fetch access token from response");
            }
        } catch (HttpClientErrorException e) {
            logger.error("Error exchanging code for token: {} | Status code: {} | Response body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    private JsonNode getProfileDetailsGoogle(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching user profile: {} | Status code: {} | Response body: {}",
                    e.getMessage(), e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    public void handleGoogleLoginAndCreateUser(String code, HttpSession session) {
        String accessToken = getOauthAccessTokenGoogle(code);
        JsonNode profile = getProfileDetailsGoogle(accessToken);

        if (profile == null) {
            throw new IllegalArgumentException("Failed to fetch user profile from Google.");
        }

        String email = profile.has("email") ? profile.get("email").asText() : null;
        String name = profile.has("name") ? profile.get("name").asText() : null;

        if (email != null && name != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                user = new User(name, email, null);
                userRepository.save(user);
            }
            session.setAttribute("token", accessToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            logger.error("Email or name is missing in the Google profile.");
            throw new IllegalArgumentException("Email or name is missing in the Google profile.");
        }
    }
}
