package ekvus;

import parser.ParserSetting;

public class EkvusSetting extends ParserSetting {
    public EkvusSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://ekvus-kirov.ru/afisha";
        PREFIX = "";
    }
}
