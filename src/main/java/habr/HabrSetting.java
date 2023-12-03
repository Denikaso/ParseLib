package habr;

import parser.ParserSetting;

public class HabrSetting extends ParserSetting {
    public HabrSetting(int start, int end) {
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://habr.com/ru/all";
        PREFIX = "page{CurrentId}";
    }
}