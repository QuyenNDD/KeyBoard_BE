package com.project.keyboard.dto.response.page;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private int totalElements;
    private int size;
    private int page;
}
