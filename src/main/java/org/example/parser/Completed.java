package org.example.parser;

import static java.lang.System.*;

public class Completed implements ParserWorker.OnCompleted{
    @Override
    public void onCompleted(Object sender) {
        out.println("Загрузка закончена");
    }
}
