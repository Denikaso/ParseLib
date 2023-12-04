package org.example.kirovdramteatr;

import org.example.parser.ParserSetting;

public class TheatreSetting extends ParserSetting {
    public TheatreSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        baseUrl = "https://kirovdramteatr.ru/afisha";
        prefix = "";
    }
}
