package ru.vyatsu.parselib.parser;

import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class HtmlLoader {
    String url;

    public HtmlLoader(ParserSetting settings) {
        url = settings.getBaseUrl()+"/"+ settings.getPrefix();
    }

    public Document getSourceByPageId(int id) throws IOException {
        val currentUrl = url.replace("{CurrentId}", Integer.toString(id));
        return Jsoup.connect(currentUrl).get();
    }
}
