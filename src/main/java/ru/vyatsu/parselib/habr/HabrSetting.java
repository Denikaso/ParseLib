package ru.vyatsu.parselib.habr;

import ru.vyatsu.parselib.parser.ParserSetting;

public class HabrSetting extends ParserSetting {
    public HabrSetting(int start, int end) {
        startPoint = start;
        endPoint = end;
        baseUrl = "https://habr.com/ru/all";
        prefix = "page{CurrentId}";
    }
}