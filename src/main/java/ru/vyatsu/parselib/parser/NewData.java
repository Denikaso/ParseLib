package ru.vyatsu.parselib.parser;

import static java.lang.System.*;
public class NewData<T> implements ParserWorker.OnNewDataHandler<T> {
    @Override
    public void onNewData(final Object sender, final T args) {
            out.println(args);
    }
}
