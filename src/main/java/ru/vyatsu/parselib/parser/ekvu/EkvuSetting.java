package ru.vyatsu.parselib.parser.ekvu;

import ru.vyatsu.parselib.parser.ParserSetting;

/**
 * Класс EkvuSetting предоставляет настройки для парсера сайта "<a href="https://ekvus-kirov.ru/afisha">...</a>".
 */
public class EkvuSetting extends ParserSetting {
    /**
     * Конструктор класса.
     *
     * @param start Начальная страница для парсинга.
     * @param end   Конечная страница для парсинга.
     */
    public EkvuSetting(int start, int end){
        startPoint = start;
        endPoint = end;
        baseUrl = "https://ekvus-kirov.ru/afisha";
        prefix = "";
    }
}