package org.example;

import habr.HabrParser;
import habr.HabrSettings;
import model.Article;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ParserWorker<ArrayList<Article>>  parser = new ParserWorker<>(new HabrParser());
        // задайте значения переменным start и end
        parser.setParserSettings(new HabrSettings(1, 2));
        parser.onCompletedList.add(new Completed());
        parser.onNewDataList.add(new NewData());

        parser.Start();
        Thread.sleep(5000);
        parser.Abort();
    }
}