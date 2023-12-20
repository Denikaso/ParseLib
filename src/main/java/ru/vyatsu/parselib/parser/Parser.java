package ru.vyatsu.parselib.parser;

import org.jsoup.nodes.Document;

/**
 * Интерфейс Parser определяет метод для парсинга документа.
 * @param <T> Тип данных, который возвращает парсер.
 */
public interface Parser<T>{
    /**
     * Константа, представляющая отсутствие данных.
     */
    String NO_DATA = "Нет данных";

    /**
     * Метод для парсинга документа.
     *
     * @param document Документ, который необходимо распарсить.
     * @param imageProcessor Объект ImageProcessor для обработки изображений, если необходимо.
     * @return Результат парсинга, представленный в виде объекта заданного типа T.
     */
    T parse(Document document, final ImageProcessor imageProcessor);
}