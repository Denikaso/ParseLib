package ru.vyatsu.parselib.habr;

import ru.vyatsu.parselib.model.Article;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import lombok.val;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public final ArrayList<Article> parse(Document document, ImageProcessor imageProcessor) {
        ArrayList<Article> articles = new ArrayList<>();
        val articleElements = document.select("article");

        for (Element articleElement : articleElements) {
            val title = articleElement
                    .select("h2.tm-title a.tm-title__link")
                    .text();
            val text = articleElement
                    .select("div.article-formatted-body")
                    .text();
            val imageUrl = articleElement
                    .select("div.article-formatted-body img")
                    .attr("src");
            articles.add(Article
                    .builder()
                    .title(title)
                    .text(text)
                    .imageUrl(imageUrl)
                    .build());

            if (imageUrl.startsWith("https")) {
                imageProcessor.copyImage(imageUrl);
            }
        }
        return articles;
    }
}
