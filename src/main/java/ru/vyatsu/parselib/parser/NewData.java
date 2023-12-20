package ru.vyatsu.parselib.parser;

import static java.lang.System.*;

/**
 * Класс NewData реализует интерфейс OnNewDataHandler для обработки новых данных, полученных от парсера.
 * @param <T> Тип данных, который обрабатывается при получении новых данных.
 */
public class NewData<T> implements ParserWorker.OnNewDataHandler<T> {
    /**
     * Метод onNewData обрабатывает новые данные, выводя их в консоль.
     *
     * @param sender Объект-отправитель данных.
     * @param args Новые данные, которые необходимо обработать.
     */
    @Override
    public void onNewData(final Object sender, final T args) {
            out.println(args);
    }
}