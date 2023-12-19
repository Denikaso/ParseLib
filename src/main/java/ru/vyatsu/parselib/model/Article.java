package ru.vyatsu.parselib.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {
    private String title;
    private String text;
    private String imageUrl;
}