package org.example.ekvus;

import org.example.parser.ParserSetting;

public class EkvusSetting extends ParserSetting {
    public EkvusSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        baseUrl = "https://ekvus-kirov.ru/afisha";
        prefix = "";
    }
}
