package ru.vyatsu.parselib.parser;

import static java.lang.System.*;

public class Completed implements ParserWorker.OnCompleted{
    @Override
    public void onCompleted(final Object sender) {
        out.println("Загрузка закончена");
    }
}
