package kirovdramteatr;

import models.Poster;
import lombok.val;
import parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class TheatreParser implements Parser<ArrayList<Poster>> {
    @Override
    public final ArrayList<Poster> parse(Document document) {
        ArrayList<Poster> posters = new ArrayList<>();
        val postersElements = document.select("div.t_afisha");
        val folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try {
            Files.createDirectories(folderPath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        for(Element poster : postersElements){
            val duration = poster
                    .select(".td3 .td2 .td1 div")
                    .first()
                    .textNodes()
                    .get(0)
                    .text();
            val td2Element = poster
                    .select(".td2")
                    .first();
            val imageUrl = td2Element
                    .select("img")
                    .first()
                    .absUrl("src");
            val tInfoAfishaElement = poster
                    .select(".t_info_afisha")
                    .first();
            val date = tInfoAfishaElement
                    .select(".td1 .date_afisha").text();
            val title = tInfoAfishaElement.select("h3 a")
                    .textNodes()
                    .get(0)
                    .text();
            val ageLimit = tInfoAfishaElement.select(".value_limit")
                    .text();
            posters.add(Poster.builder()
                    .title(title)
                    .imageUrl(imageUrl)
                    .date(date)
                    .duration(duration)
                    .ageLimit(ageLimit)
                    .build());

            if(imageUrl.startsWith("https")) {
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
}
