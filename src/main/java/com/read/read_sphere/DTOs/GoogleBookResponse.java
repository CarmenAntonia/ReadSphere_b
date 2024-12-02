package com.read.read_sphere.DTOs;

import java.util.List;

public class GoogleBookResponse {
    private List<BookDTO> items;

    public List<BookDTO> getItems() {
        return items;
    }

    public void setItems(List<BookDTO> items) {
        this.items = items;
    }
}
