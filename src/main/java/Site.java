import ekvus.EkvusParser;
import ekvus.EkvusSetting;
import habr.HabrParser;
import habr.HabrSetting;
import kirovdramteatr.TheatreParser;
import kirovdramteatr.TheatreSetting;
import lombok.Getter;
import parser.Parser;
import parser.ParserSetting;

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
