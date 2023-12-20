package ru.vyatsu.parselib.parser;

import static java.lang.System.*;

/**
 * Класс Completed реализует интерфейс OnCompleted и представляет событие "Загрузка завершена".
 */
public class Completed implements ParserWorker.OnCompleted{
    /**
     * Метод onCompleted вызывается при завершении загрузки данных и выводит сообщение о завершении в консоль.
     *
     * @param sender Объект, инициировавший событие.
     */
    @Override
    public void onCompleted(final Object sender) {
        out.println("Загрузка закончена");
    }
}