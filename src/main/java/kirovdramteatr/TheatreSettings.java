package kirovdramteatr;

import lombok.val;
import parser.ParserSettings;

public class TheatreSettings extends ParserSettings {
    public TheatreSettings(int start, int end){
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://kirovdramteatr.ru/afisha";
        PREFIX = "";
    }
}
