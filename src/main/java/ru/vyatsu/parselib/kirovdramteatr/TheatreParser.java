package ru.vyatsu.parselib.kirovdramteatr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.vyatsu.parselib.exception.ParsingRuntimeException;
import ru.vyatsu.parselib.model.Poster;
import ru.vyatsu.parselib.parser.ImageProcessor;
import ru.vyatsu.parselib.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Optional;
import java.util.ArrayList;
import lombok.val;

public class TheatreParser implements Parser<ArrayList<Poster>> {
    private static final Logger logger = LogManager.getLogger(TheatreParser.class);

    @Override
    public final ArrayList<Poster> parse(Document document, ImageProcessor imageProcessor) {
        ArrayList<Poster> posters = new ArrayList<>();
        val postersElements = document.select("div.t_afisha");

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
                    imageProcessor.copyImage(imageUrl);
                }

            } catch (Exception e) {
                logger.error("Ошибка при обработке данных афиши", e);
                throw new ParsingRuntimeException("Ошибка при обработке данных афиши", e);
            }
        }
        return posters;
    }
}
