package habr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Article {
    private String title;
    private String text;
    private String imageUrl;

    @Override
    public String toString(){
        return  "title=" + title + "\n" +
                "text=" + text + "\n" +
                "imageUrl=" + imageUrl + "\n" +
                "\n";
    }
}
