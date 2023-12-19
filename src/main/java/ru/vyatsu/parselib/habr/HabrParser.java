package ru.vyatsu.parselib.habr;

import ru.vyatsu.parselib.model.Article;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import java.util.stream.Collectors;
import java.util.ArrayList;
import lombok.val;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public final ArrayList<Article> parse(Document document, ImageProcessor imageProcessor) {
        val articleElements = document.select("article");

        return articleElements.stream()
                .map(articleElement -> {
                    val title = articleElement
                            .select("h2.tm-title a.tm-title__link")
                            .text();
                    val text = articleElement
                            .select("div.article-formatted-body")
                            .text();
                    val imageUrl = articleElement
                            .select("div.article-formatted-body img")
                            .attr("src");

                    return Article.builder()
                            .title(title)
                            .text(text)
                            .imageUrl(imageUrl)
                            .build();
                })
                .peek(article -> {
                    if (article.getImageUrl().startsWith("https")) {
                        imageProcessor.copyImage(article.getImageUrl());
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
