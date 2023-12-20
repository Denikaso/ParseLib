package ru.vyatsu.parselib;

import ru.vyatsu.parselib.parser.ekvu.EkvuParser;
import ru.vyatsu.parselib.parser.ekvu.EkvuSetting;
import ru.vyatsu.parselib.parser.habr.HabrParser;
import ru.vyatsu.parselib.parser.habr.HabrSetting;
import ru.vyatsu.parselib.parser.kirovdramteatr.TheatreParser;
import ru.vyatsu.parselib.parser.kirovdramteatr.TheatreSetting;
import ru.vyatsu.parselib.parser.Parser;
import ru.vyatsu.parselib.parser.ParserSetting;
import lombok.Getter;

/**
 * Enum для представления поддерживаемых сайтов парсера.
 */
@Getter
public enum Site {
    HABR("https://habr.com/ru/all", HabrParser.class, HabrSetting.class),
    THEATRE("https://kirovdramteatr.ru/afisha", TheatreParser.class, TheatreSetting.class),
    EKVU("https://ekvus-kirov.ru/afisha", EkvuParser.class, EkvuSetting.class);

    private final String url;
    private final Class<? extends Parser<?>> parserClass;
    private final Class<? extends ParserSetting> settingClass;

    /**
     * Конструктор для создания экземпляра Enum Site.
     *
     * @param url          URL сайта.
     * @param parserClass  Класс парсера для сайта.
     * @param settingClass Класс настроек парсера для сайта.
     */
    Site(String url, Class<? extends Parser<?>> parserClass, Class<? extends ParserSetting> settingClass) {
        this.url = url;
        this.parserClass = parserClass;
        this.settingClass = settingClass;
    }

    /**
     * Получает экземпляр Site по его URL.
     *
     * @param url URL сайта.
     * @return Экземпляр Site или null, если сайт не найден.
     */
    public static Site fromUrl(final String url) {
        for (Site site : values()) {
            if (site.getUrl().equals(url)) {
                return site;
            }
        }
        return null;
    }
}