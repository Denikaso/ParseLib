package org.example;

import habr.model.Article;

import java.util.ArrayList;

public class NewData<T> implements ParserWorker.OnNewDataHandler<T> {
    @Override
    public void OnNewData(Object sender, T args) {
            System.out.println(args);
    }
}
