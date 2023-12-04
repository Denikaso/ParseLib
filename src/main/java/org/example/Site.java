package org.example;

import org.example.ekvus.EkvusParser;
import org.example.ekvus.EkvusSetting;
import org.example.habr.HabrParser;
import org.example.habr.HabrSetting;
import org.example.kirovdramteatr.TheatreParser;
import org.example.kirovdramteatr.TheatreSetting;
import lombok.Getter;
import org.example.parser.Parser;
import org.example.parser.ParserSetting;

@Getter
public enum Site {
    HABR("https://habr.com/ru/all", HabrParser.class, HabrSetting.class),
    THEATRE("https://kirovdramteatr.ru/afisha", TheatreParser.class, TheatreSetting.class),
    EKVUS("https://ekvus-kirov.ru/afisha", EkvusParser.class, EkvusSetting.class);

    private final String url;
    private final Class<? extends Parser<?>> parserClass;
    private final Class<? extends ParserSetting> settingClass;

    Site(String url, Class<? extends Parser<?>> parserClass, Class<? extends ParserSetting> settingClass) {
        this.url = url;
        this.parserClass = parserClass;
        this.settingClass = settingClass;
    }

    public static Site fromUrl(String url) {
        for (Site site : values()) {
            if (site.getUrl().equals(url)) {
                return site;
            }
        }
        return null;
    }
}
