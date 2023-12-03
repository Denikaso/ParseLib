package habr;

import models.Article;
import parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import lombok.val;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public final ArrayList<Article> parse(Document document) {
        ArrayList<Article> articles = new ArrayList<>();
        val articleElements = document.select("article");

        val folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                try {
                    val url = new URL(imageUrl);

                    try (val in = url.openStream()) {
                        val fileName = imageUrl
                                .substring(imageUrl.lastIndexOf('/') + 1);
                        val imagePath = Paths
                                .get(folderPath.toString(), fileName);
                        Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        return articles;
    }
}
