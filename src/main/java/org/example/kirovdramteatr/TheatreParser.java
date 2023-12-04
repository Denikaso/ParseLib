package org.example.kirovdramteatr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ParsingRuntimeException;
import org.example.models.Poster;
import org.example.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Optional;
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

public class TheatreParser implements Parser<ArrayList<Poster>> {
    private static final Logger logger = LogManager.getLogger(TheatreParser.class);
    private static final String NO_DATA = "Нет данных";

    @Override
    public final ArrayList<Poster> parse(Document document) {
        ArrayList<Poster> posters = new ArrayList<>();
        val postersElements = document.select("div.t_afisha");
        val folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try {
            Files.createDirectories(folderPath);
            logger.info("Созданы директории для сохранения изображений.");
        } catch (IOException e) {
            logger.error("Ошибка при создании директорий", e);
            throw new ParsingRuntimeException("Ошибка при создании директорий", e);
        }

        for (Element poster : postersElements) {
            try {
                val duration = Optional.ofNullable(poster
                                .select(".td3 .td2 .td1 div")
                                .first())
                        .map(element -> element.textNodes().get(0).text())
                        .orElse(NO_DATA);

                val td2Element = poster.select(".td2").first();
                val imageUrl = Optional.ofNullable(td2Element)
                        .map(element -> element.select("img").first())
                        .map(element -> element.absUrl("src"))
                        .orElse(NO_DATA);

                val tInfoAfishaElement = poster.select(".t_info_afisha").first();
                val date = Optional.ofNullable(tInfoAfishaElement)
                        .map(element -> element.select(".td1 .date_afisha").text())
                        .orElse(NO_DATA);

                val title = Optional.ofNullable(tInfoAfishaElement)
                        .map(element -> element.select("h3 a").textNodes().get(0).text())
                        .orElse(NO_DATA);

                val ageLimit = Optional.ofNullable(tInfoAfishaElement)
                        .map(element -> element.select(".value_limit").text())
                        .orElse(NO_DATA);

                posters.add(Poster.builder()
                        .title(title)
                        .imageUrl(imageUrl)
                        .date(date)
                        .duration(duration)
                        .ageLimit(ageLimit)
                        .build());

                if (imageUrl.startsWith("https")) {
                    copyImage(imageUrl, folderPath);
                }

            } catch (Exception e) {
                logger.error("Ошибка при обработке данных афиши", e);
                throw new ParsingRuntimeException("Ошибка при обработке данных афиши", e);
            }
        }
        return posters;
    }

    private void copyImage(String imageUrl, Path folderPath) {
        try {
            URI uri = new URI(imageUrl);
            URL url = uri.toURL();

            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            Path imagePath = Paths.get(folderPath.toString(), fileName);
            downloadImage(url, imagePath);
            logger.info("Изображение скопировано успешно: {}", fileName);
        } catch (URISyntaxException | MalformedURLException e) {
            logger.error("Ошибка при обработке URL", e);
            throw new ParsingRuntimeException("Ошибка при обработке URL", e);
        }
    }

    private void downloadImage(URL url, Path imagePath) {
        try (val in = url.openStream()) {
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Ошибка при загрузке изображения", e);
            throw new ParsingRuntimeException("Ошибка при загрузке изображения", e);
        }
    }
}
