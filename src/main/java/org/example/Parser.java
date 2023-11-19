package org.example;

import javax.swing.text.Document;

public interface Parser<T> {
    T Parse(Document document);
}
