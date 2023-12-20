package ru.vyatsu.parselib.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель, представляющая статью.
 */
@Data
@Builder
public class Article {
    /**
     * Заголовок статьи.
     */
    private final String title;

    /**
     * Текст статьи.
     */
    private final String text;

    /**
     * URL изображения в статье.
     */
    private final String imageUrl;
}
