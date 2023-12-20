package ru.vyatsu.parselib.parser.ekvu;

import ru.vyatsu.parselib.parser.ParserSetting;

public class EkvuSetting extends ParserSetting {
    public EkvuSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        baseUrl = "https://ekvus-kirov.ru/afisha";
        prefix = "";
    }
}
