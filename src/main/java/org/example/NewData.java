package org.example;

import model.Article;

import java.util.ArrayList;

public class NewData implements ParserWorker.OnNewDataHandler<ArrayList<Article>> {
    @Override
    public void OnNewData(Object sender, ArrayList<Article> articles) {
        for (Article s : articles) {
            System.out.println(s.getTitle());
            System.out.println(s.getText());
            System.out.println(s.getImageUrl());
        }
    }
}
