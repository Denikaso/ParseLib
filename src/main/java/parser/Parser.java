package parser;

import org.jsoup.nodes.Document;

public interface Parser<T> {
    T Parse(Document document);
}
