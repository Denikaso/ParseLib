package ru.vyatsu.parselib.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель, представляющая афишу спектакля.
 */
@Data
@Builder
public class Poster {
    /**
     * Название спектакля.
     */
    private final String title;
    /**
     * URL изображения афиши.
     */
    private final String imageUrl;
    /**
     * Дата проведения спектакля.
     */
    private final String date;
    /**
     * Продолжительность спектакля.
     */
    private final String duration;
    /**
     * Возрастное ограничение спектакля.
     */
    private final String ageLimit;
}
