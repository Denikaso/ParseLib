package habr;

import model.Article;
import org.example.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class HabrParser implements Parser<ArrayList<Article>> {
    @Override
    public ArrayList<Article> Parse(Document document) {
        ArrayList<Article> articles = new ArrayList<Article>();

        Elements articleElements = document.select("div.post__body");

        for (Element articleElement : articleElements) {
            String title = articleElement.select("a.tm-title__link span").text();
            String text = articleElement.select("div.post__text").text();

            String imageUrl = articleElement.select("img.post__habracut-btn").attr("src");

            Article article = new Article(title, text, imageUrl);
            articles.add(article);
        }
        return articles;
    }
}
