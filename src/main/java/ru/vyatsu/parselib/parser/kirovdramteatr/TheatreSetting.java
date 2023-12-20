package ru.vyatsu.parselib.parser.kirovdramteatr;

import ru.vyatsu.parselib.parser.ParserSetting;

/**
 * Класс TheatreSetting предоставляет настройки для парсера, специфичные для сайта "<a href="https://kirovdramteatr.ru/afisha">...</a>".
 */
public class TheatreSetting extends ParserSetting {
    /**
     * Конструктор класса TheatreSetting инициализирует параметры для парсинга афиши театра.
     *
     * @param start Начальная страница для парсинга.
     * @param end   Конечная страница для парсинга.
     */
    public TheatreSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        baseUrl = "https://kirovdramteatr.ru/afisha";
        prefix = "";
    }
}