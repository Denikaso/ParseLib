package ru.vyatsu.parselib.parser;

import org.jsoup.nodes.Document;
import java.io.IOException;
import static org.jsoup.Jsoup.connect;

/**
 * Класс HtmlLoader отвечает за загрузку HTML-страниц с использованием библиотеки Jsoup.
 */
public class HtmlLoader {
    private final String url;

    /**
     * Конструктор HtmlLoader принимает настройки парсера и формирует URL для загрузки страниц.
     *
     * @param settings Настройки парсера.
     */
    public HtmlLoader(ParserSetting settings) {
        url = settings.getBaseUrl() + "/" + settings.getPrefix();
    }

    /**
     * Метод getSourceByPageId загружает HTML-страницу по указанному идентификатору страницы.
     *
     * @param id Идентификатор страницы.
     * @return Объект Document, представляющий HTML-содержимое страницы.
     * @throws IOException Возникает при ошибке загрузки страницы.
     */
    public Document getSourceByPageId(final int id) throws IOException {
        return connect(url.replace("{CurrentId}", Integer.toString(id))).get();
    }
}