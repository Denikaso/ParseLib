package org.example;

import habr.HabrParser;
import habr.HabrSettings;
import habr.model.Article;
import kirovdramteatr.TheatreParser;
import kirovdramteatr.TheatreSettings;
import kirovdramteatr.model.Poster;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        ParserWorker<ArrayList<Poster>>  parser = new ParserWorker<>(new TheatreParser());
        parser.setParserSettings(new TheatreSettings(1, 1));
        parser.onCompletedList.add(new Completed());
        parser.onNewDataList.add(new NewData());

        parser.Start();
        Thread.sleep(5000);
        parser.Abort();
    }
}