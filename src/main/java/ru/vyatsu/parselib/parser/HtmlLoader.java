package ru.vyatsu.parselib.parser;

import org.jsoup.nodes.Document;
import java.io.IOException;

import static org.jsoup.Jsoup.connect;

public class HtmlLoader {
    private final String url;

    public HtmlLoader(ParserSetting settings) {
        url = settings.getBaseUrl() + "/" + settings.getPrefix();
    }

    public Document getSourceByPageId(int id) throws IOException {
        return connect(url.replace("{CurrentId}", Integer.toString(id))).get();
    }
}
