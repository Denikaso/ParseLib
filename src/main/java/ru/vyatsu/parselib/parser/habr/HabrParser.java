package ru.vyatsu.parselib.parser.habr;

import ru.vyatsu.parselib.model.Article;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import lombok.val;
import static java.util.stream.Collectors.toCollection;

/**
 * Класс HabrParser реализует парсинг статей с сайта "<a href="https://habr.com/ru/all">...</a>".
 */
public class HabrParser implements Parser<ArrayList<Article>> {
    /**
     * Парсит документ и возвращает список статей.
     *
     * @param document        HTML-документ для парсинга.
     * @param imageProcessor  Обработчик изображений.
     * @return                Список статей.
     */
    @Override
    public final ArrayList<Article> parse(Document document, final ImageProcessor imageProcessor) {
        val articleElements = document.select("article");

        return articleElements.stream()
                .map(articleElement -> Article.builder()
                        .title(articleElement.select("h2.tm-title a.tm-title__link").text())
                        .text(articleElement.select("div.article-formatted-body").text())
                        .imageUrl(articleElement.select("div.article-formatted-body img").attr("src"))
                        .build()
                )
                .peek(article -> {
                    if (article.getImageUrl().startsWith("https")) {
                        imageProcessor.copyImage(article.getImageUrl());
                    }
                })
                .collect(toCollection(ArrayList::new));
    }
}