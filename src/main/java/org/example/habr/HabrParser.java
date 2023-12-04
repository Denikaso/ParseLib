package org.example.habr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ParsingRuntimeException;
import org.example.models.Article;
import org.example.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import lombok.val;

public class HabrParser implements Parser<ArrayList<Article>> {
    private static final Logger logger = LogManager.getLogger(HabrParser.class);

    @Override
    public final ArrayList<Article> parse(Document document) {
        ArrayList<Article> articles = new ArrayList<>();
        val articleElements = document.select("article");

        val folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try {
            Files.createDirectories(folderPath);
            logger.info("Созданы директории для сохранения изображений.");
        } catch (IOException exception) {
            logger.error("Ошибка при создании директорий", exception);
            throw new ParsingRuntimeException("Ошибка при создании директорий", exception);
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
                copyImage(imageUrl, folderPath);
            }
        }
        return articles;
    }
    private void copyImage(String imageUrl, Path folderPath) {
        try {
            URI uri = new URI(imageUrl);
            URL url = uri.toURL();

            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            Path imagePath = Paths.get(folderPath.toString(), fileName);
            downloadImage(url, imagePath);
            logger.info("Изображение скопировано успешно: {}", fileName);
        } catch (URISyntaxException | MalformedURLException exception) {
            logger.error("Ошибка при обработке URL", exception);
            throw new ParsingRuntimeException("Ошибка при обработке URL", exception);
        }
    }

    private void downloadImage(URL url, Path imagePath) {
        try (val in = url.openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            logger.error("Ошибка при загрузке изображения", exception);
            throw new ParsingRuntimeException("Ошибка при загрузке изображения", exception);
        }
    }
}
