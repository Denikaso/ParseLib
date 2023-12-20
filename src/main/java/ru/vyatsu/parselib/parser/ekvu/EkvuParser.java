package ru.vyatsu.parselib.parser.ekvu;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import ru.vyatsu.parselib.model.Poster;
import lombok.val;
import ru.vyatsu.parselib.parser.ImageProcessor;
import org.jsoup.nodes.Document;
import ru.vyatsu.parselib.parser.Parser;
import java.io.IOException;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toCollection;
import static org.jsoup.Jsoup.connect;
@Slf4j
public class EkvuParser implements Parser<ArrayList<Poster>> {

    @Override
    public ArrayList<Poster> parse(Document document, final ImageProcessor imageProcessor) {
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
                                .title(ofNullable(poster.select("td:eq(1)").select("a").first())
                                        .map(Element::text)
                                        .orElse(NO_DATA))
                                .imageUrl(getImageUrl(loadPerformance(poster)))
                                .ageLimit(of(poster.select("[class=\"al_s\"], [class=\"al\"]").text())
                                        .orElse(NO_DATA))
                                .date(poster.getElementsByTag("font").text())
                                .duration(of(document.getElementsMatchingText("Продолжительность спектакля:"))
                                        .filter(durations -> !durations.isEmpty())
                                        .map(durations -> durations.get(8).text().substring("Продолжительность спектакля:".length()))
                                        .orElse(NO_DATA))
                                .build();
                    } catch (Exception exception) {
                        log.error("Ошибка при обработке данных афиши", exception);
                        throw new ParsingRuntimeException("Ошибка при обработке данных афиши", exception);
                    }
                })
                .peek(poster -> {
                    if (poster.getImageUrl().startsWith("https")) {
                        imageProcessor.copyImage(poster.getImageUrl());
                    }
                })
                .collect(toCollection(ArrayList::new));
    }

    private static Document loadPerformance(final Element poster) {
        try {
            return connect("https://ekvus-kirov.ru" +
                    poster.getElementsByTag("a").get(1).attr("href")).get();
        } catch (IOException exception) {
            throw new ParsingRuntimeException("Ошибка при загрузке информации о спектакле", exception);
        }
    }

    private static String getImageUrl(final Document doc) {
        val image = doc.getElementById("photo_osnova");
        return ofNullable(image != null ? image.absUrl("src") : doc.getElementsByClass("img_right").first())
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
