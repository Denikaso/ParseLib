package org.example.parser;

import static java.lang.System.*;
public class NewData<T> implements ParserWorker.OnNewDataHandler<T> {
    @Override
    public void onNewData(Object sender, T args) {
            out.println(args);
    }
}
