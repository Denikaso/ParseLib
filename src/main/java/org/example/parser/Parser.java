package org.example.parser;

import org.jsoup.nodes.Document;

public interface Parser<T>{
    String NO_DATA = "Нет данных";
    T parse(Document document, ImageProcessor imageProcessor);
}
