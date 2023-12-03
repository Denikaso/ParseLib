package kirovdramteatr;

import parser.ParserSetting;

public class TheatreSetting extends ParserSetting {
    public TheatreSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://kirovdramteatr.ru/afisha";
        PREFIX = "";
    }
}
