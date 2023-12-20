package ru.vyatsu.parselib.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {
    private final String title;
    private final String text;
    private final String imageUrl;
}
