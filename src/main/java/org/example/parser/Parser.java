package org.example.parser;

import org.jsoup.nodes.Document;

public interface Parser<T>{
    T parse(Document document, ImageProcessor imageProcessor);
}
