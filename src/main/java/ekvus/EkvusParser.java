package ekvus;

import ekvus.model.Poster;
import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.Parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class EkvusParser implements Parser<ArrayList<Poster>> {

    @Override
    public ArrayList<Poster> parse(Document document) {
        ArrayList<Poster> posters = new ArrayList<>();
        val postersElements = document.getElementsByClass("page_box")
                .get(0)
                .getElementsByTag("table")
                .get(0)
                .getElementsByTag("tr");
        val folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Element poster : postersElements){
            if (poster.getElementsByTag("td").size() < 2){
                continue;
            }
            val date = poster.getElementsByTag("font").text();
            val title = poster.select("td:eq(1)").select("a").textNodes().get(0).text();
            val ageLimit = poster.select("[class=\"al_s\"], [class=\"al\"]").text();

            Document posterDocument = null;
            try {
                posterDocument = loadPerfomance(poster);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            val duration = getDuration(posterDocument);
            val imageUrl = getImageUrl(posterDocument);
            posters.add(Poster.builder()
                    .title(title)
                    .imageUrl(imageUrl)
                    .ageLimit(ageLimit)
                    .date(date)
                    .duration(duration)
                    .build());

            if (imageUrl.startsWith("https")) {
                try {
                    val url = new URL(imageUrl);

                    try (val in = url.openStream()) {
                        val fileName = imageUrl
                                .substring(imageUrl.lastIndexOf('/') + 1);
                        val imagePath = Paths
                                .get(folderPath.toString(), fileName);
                        Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        return posters;
    }
    private static Document loadPerfomance( Element afisha) throws IOException {
        String href = afisha.getElementsByTag("a").get(1).attr("href");
        return Jsoup.connect("https://ekvus-kirov.ru"+href).get();
    }
    private static String getDuration(Document doc){
        String dur = "Продолжительность спектакля:";
        Elements durations = doc.getElementsMatchingText(dur);
        String duration="";
        if (!durations.isEmpty()){
            duration = durations.get(8).text().substring(dur.length());
        }
        return duration;
    }
    private static String getImageUrl(Document doc){
        String imageUrl="";
        Element image = doc.getElementById("photo_osnova");
        if (image != null){
            imageUrl=image.absUrl("src");
        }
        else if (doc.getElementsByClass("img_right").first() != null){
            imageUrl = doc.getElementsByClass("img_right").first().absUrl("src");
        }
        return imageUrl;
    }
}
