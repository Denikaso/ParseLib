package ru.vyatsu.parselib.habr;

import ru.vyatsu.parselib.model.Article;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.val;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public final ArrayList<Article> parse(Document document, ImageProcessor imageProcessor) {
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
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
