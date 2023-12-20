package ru.vyatsu.parselib.parser;

import lombok.Data;

/**
 * Базовый класс настроек для парсера. Предоставляет основные параметры для настройки процесса парсинга.
 */
@Data
public abstract class ParserSetting {
    /**
     * Базовый URL, к которому будет добавлен префикс для формирования полного адреса страницы.
     */
    protected String baseUrl;
    /**
     * Префикс, добавляемый к базовому URL для формирования полного адреса страницы.
     */
    protected String prefix;
    /**
     * Начальная страница для парсинга.
     */
    protected int startPoint;
    /**
     * Конечная страница для парсинга.
     */
    protected int endPoint;
}