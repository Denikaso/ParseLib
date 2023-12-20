package ru.vyatsu.parselib.parser.habr;

import ru.vyatsu.parselib.parser.ParserSetting;

/**
 * Класс HabrSetting предоставляет настройки для парсинга сайта "<a href="https://habr.com/ru/all">...</a>".
 */
public class HabrSetting extends ParserSetting {
    /**
     * Конструктор класса HabrSetting.
     *
     * @param start Начальная страница для парсинга.
     * @param end   Конечная страница для парсинга.
     */
    public HabrSetting(int start, int end) {
        startPoint = start;
        endPoint = end;
        baseUrl = "https://habr.com/ru/all";
        prefix = "page{CurrentId}";
    }
}