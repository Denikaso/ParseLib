package habr;

import org.example.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HabrParser implements Parser<ArrayList<String>> {
    @Override
    public ArrayList<String> Parse(Document document) {
        ArrayList<String> list = new ArrayList<String>();

        Elements titleElements = document.select("a.tm-title__link span");

        // Итерируем по каждому найденному элементу и добавляем текст в список
        for (Element titleElement : titleElements) {
            String title = titleElement.text();
            list.add(title);
        }
        return list;
    }
}
