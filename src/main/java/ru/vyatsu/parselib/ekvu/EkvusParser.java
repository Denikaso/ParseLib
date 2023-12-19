package ru.vyatsu.parselib.ekvu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import ru.vyatsu.parselib.model.Poster;
import lombok.val;
import ru.vyatsu.parselib.parser.ImageProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.stream.Collectors;
import ru.vyatsu.parselib.parser.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class EkvusParser implements Parser<ArrayList<Poster>> {
    private static final Logger logger = LogManager.getLogger(EkvusParser.class);

    @Override
    public ArrayList<Poster> parse(Document document, ImageProcessor imageProcessor) {
        val postersElements = document.getElementsByClass("page_box")
                .get(0)
                .getElementsByTag("table")
                .get(0)
                .getElementsByTag("tr");

        return postersElements.stream()
                .filter(poster -> poster.getElementsByTag("td").size() >= 2)
                .map(poster -> {
                    try {
                        return Poster.builder()
                                .title(Optional.ofNullable(poster.select("td:eq(1)").select("a").first())
                                        .map(Element::text)
                                        .orElse(NO_DATA))
                                .imageUrl(getImageUrl(loadPerformance(poster)))
                                .ageLimit(Optional.of(poster.select("[class=\"al_s\"], [class=\"al\"]").text())
                                        .orElse(NO_DATA))
                                .date(poster.getElementsByTag("font").text())
                                .duration(Optional.of(document.getElementsMatchingText("Продолжительность спектакля:"))
                                        .filter(durations -> !durations.isEmpty())
                                        .map(durations -> durations.get(8).text().substring("Продолжительность спектакля:".length()))
                                        .orElse(NO_DATA))
                                .build();
                    } catch (Exception exception) {
                        logger.error("Ошибка при обработке данных афиши", exception);
                        throw new ParsingRuntimeException("Ошибка при обработке данных афиши", exception);
                    }
                })
                .peek(poster -> {
                    if (poster.getImageUrl().startsWith("https")) {
                        imageProcessor.copyImage(poster.getImageUrl());
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static Document loadPerformance(Element poster) {
        try {
            return Jsoup.connect("https://ekvus-kirov.ru" +
                    poster.getElementsByTag("a").get(1).attr("href")).get();
        } catch (IOException exception) {
            throw new ParsingRuntimeException("Ошибка при загрузке информации о спектакле", exception);
        }
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
