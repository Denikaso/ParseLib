package org.example.ekvus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ParsingRuntimeException;
import org.example.models.Poster;
import lombok.val;
import org.example.parser.ImageProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.example.parser.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class EkvusParser implements Parser<ArrayList<Poster>> {
    private static final Logger logger = LogManager.getLogger(EkvusParser.class);

    @Override
    public ArrayList<Poster> parse(Document document, ImageProcessor imageProcessor) {
        ArrayList<Poster> posters = new ArrayList<>();
        val postersElements = document.getElementsByClass("page_box")
                .get(0)
                .getElementsByTag("table")
                .get(0)
                .getElementsByTag("tr");

        for (Element poster : postersElements) {
            try {
                if (poster.getElementsByTag("td").size() < 2) {
                    continue;
                }

                val date = poster.getElementsByTag("font").text();
                val title = Optional.ofNullable(poster.select("td:eq(1)").select("a").first())
                        .map(Element::text)
                        .orElse(NO_DATA);

                val ageLimit = Optional.of(poster.select("[class=\"al_s\"], [class=\"al\"]").text())
                        .orElse(NO_DATA);

                val posterDocument = loadPerformance(poster);

                val duration = getDuration(posterDocument);
                val imageUrl = getImageUrl(posterDocument);

                posters.add(Poster.builder()
                        .title(title)
                        .imageUrl(imageUrl)
                        .ageLimit(ageLimit)
                        .date(date)
                        .duration(duration)
                        .build());

                if (imageUrl.startsWith("https")) {
                    imageProcessor.copyImage(imageUrl);
                }

            } catch (Exception exception) {
                logger.error("Ошибка при обработке данных афиши", exception);
                throw new ParsingRuntimeException("Ошибка при обработке данных афиши", exception);
            }
        }
        return posters;
    }

    private static Document loadPerformance(Element poster) {
        try {
            return Jsoup.connect("https://ekvus-kirov.ru" +
                    poster.getElementsByTag("a").get(1).attr("href")).get();
        } catch (IOException exception) {
            throw new ParsingRuntimeException("Ошибка при загрузке информации о спектакле", exception);
        }
    }

    private static String getDuration(Document document) {
        val dur = "Продолжительность спектакля:";
        val durations = document.getElementsMatchingText(dur);
        return Optional.of(durations.isEmpty() ? NO_DATA : durations.get(8).text().substring(dur.length()))
                .orElse(NO_DATA);
    }

    private static String getImageUrl(Document doc) {
        val image = doc.getElementById("photo_osnova");
        return Optional.ofNullable(image != null ? image.absUrl("src") : doc.getElementsByClass("img_right").first())
                .map(element -> {
                    if (element instanceof Element imageElement) {
                        return imageElement.absUrl("src");
                    } else {
                        return NO_DATA;
                    }
                })
                .orElse(NO_DATA);
    }

}
