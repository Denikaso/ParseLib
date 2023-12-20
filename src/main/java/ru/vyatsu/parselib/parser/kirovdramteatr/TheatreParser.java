package ru.vyatsu.parselib.parser.kirovdramteatr;

import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import ru.vyatsu.parselib.model.Poster;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;

public class TheatreParser implements Parser<ArrayList<Poster>> {
    private static final Logger logger = LogManager.getLogger(TheatreParser.class);
    private static final String NO_DATA = "";

    @Override
    public ArrayList<Poster> parse(Document document, ImageProcessor imageProcessor) {
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

                    } catch (Exception e) {
                        logger.error("Ошибка при обработке данных афиши", e);
                        throw new ParsingRuntimeException("Ошибка при обработке данных афиши", e);
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
