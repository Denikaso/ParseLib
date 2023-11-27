package habr;

import habr.model.Article;
import org.example.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public ArrayList<Article> Parse(Document document) {
        ArrayList<Article> articles = new ArrayList<Article>();

        Elements articleElements = document.select("article");

        for (Element articleElement : articleElements) {
            String title = articleElement.select("h2.tm-title a.tm-title__link").text();
            String text = articleElement.select("div.article-formatted-body").text();

            String imageUrl = articleElement.select("div.article-formatted-body img").attr("src");

            Article article = new Article(title, text, imageUrl);
            articles.add(article);
        }
        return articles;
    }
}
