package org.example;

import habr.model.Article;

import java.util.ArrayList;

public class NewData implements ParserWorker.OnNewDataHandler<ArrayList<Article>> {
    @Override
    public void OnNewData(Object sender, ArrayList<Article> articles) {
        for (Article article : articles) {
            System.out.println(article.toString());
        }
    }
}
