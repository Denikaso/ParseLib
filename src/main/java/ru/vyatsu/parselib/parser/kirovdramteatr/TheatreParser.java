package ru.vyatsu.parselib.parser.kirovdramteatr;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import ru.vyatsu.parselib.model.Poster;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;

@Slf4j
public class TheatreParser implements Parser<ArrayList<Poster>> {
    @Override
    public ArrayList<Poster> parse(Document document, final ImageProcessor imageProcessor) {
        val postersElements = document.select("div.t_afisha");

        return postersElements.stream()
                .map(poster -> {
                    try {
                        val duration = ofNullable(poster
                                .select(".td3 .td2 .td1 div")
                                .first())
                                .map(element -> element.textNodes().get(0).text())
                                .orElse(NO_DATA);

                        val td2Element = poster.select(".td2").first();
                        val imageUrl = ofNullable(td2Element)
                                .map(element -> element.select("img").first())
                                .map(element -> element.absUrl("src"))
                                .orElse(NO_DATA);

                        val tInfoAfishaElement = poster.select(".t_info_afisha").first();
                        val date = ofNullable(tInfoAfishaElement)
                                .map(element -> element.select(".td1 .date_afisha").text())
                                .orElse(NO_DATA);

                        val title = ofNullable(tInfoAfishaElement)
                                .map(element -> element.select("h3 a").textNodes().get(0).text())
                                .orElse(NO_DATA);

                        val ageLimit = ofNullable(tInfoAfishaElement)
                                .map(element -> element.select(".value_limit").text())
                                .orElse(NO_DATA);

                        return Poster.builder()
                                .title(title)
                                .imageUrl(imageUrl)
                                .date(date)
                                .duration(duration)
                                .ageLimit(ageLimit)
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
}
