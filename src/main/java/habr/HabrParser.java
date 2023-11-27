package habr;

import habr.model.Article;
import org.example.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public ArrayList<Article> Parse(Document document) {
        ArrayList<Article> articles = new ArrayList<Article>();
        Elements articleElements = document.select("article");

        Path folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Element articleElement : articleElements) {
            String title = articleElement.select("h2.tm-title a.tm-title__link").text();
            String text = articleElement.select("div.article-formatted-body").text();
            String imageUrl = articleElement.select("div.article-formatted-body img").attr("src");
            articles.add(new Article(title, text, imageUrl));

            if (imageUrl.startsWith("https")) {
                try {
                    URL url = new URL(imageUrl);

                    try (InputStream in = url.openStream()) {
                        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                        Path imagePath = Paths.get(folderPath.toString(), fileName);
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
