package com.read.read_sphere.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.read.read_sphere.repository.UserRepository;
import com.read.read_sphere.repository.UserBookshelfRepository;
import com.read.read_sphere.model.UserBookshelf;

import java.util.List;

@Component
public class DefaultShelvesInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserBookshelfRepository userBookshelfRepository;

    public DefaultShelvesInitializer(UserRepository userRepository, UserBookshelfRepository userBookshelfRepository) {
        this.userRepository = userRepository;
        this.userBookshelfRepository = userBookshelfRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultShelves = List.of("myReadings", "myFutureReadings", "myCurrentReadings");

        // Iterate over all existing users and add default shelves if missing
        userRepository.findAll().forEach(user -> {
            for (String shelfName : defaultShelves) {
                userBookshelfRepository.findByUserUserIdAndShelfName(user.getUserId(), shelfName)
                        .orElseGet(() -> {
                            UserBookshelf newShelf = new UserBookshelf();
                            newShelf.setUser(user);
                            newShelf.setShelfName(shelfName);
                            return userBookshelfRepository.save(newShelf);
                        });
            }
        });
    }
}