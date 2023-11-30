package kirovdramteatr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Poster {
    private final String title;
    private final String imageUrl;
    private final String date;
    private final String duration;
    private final String ageLimit;

    @Override
    public String toString() {
        return  "title= " + title + "\n" +
                "imageUrl= " + imageUrl + "\n" +
                "date= " + date + "\n" +
                "duration=  " + duration + "\n" +
                "ageLimit= " + ageLimit + "\n" +
                "\n";
    }
}
