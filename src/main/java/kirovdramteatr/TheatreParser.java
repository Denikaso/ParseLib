package kirovdramteatr;

import kirovdramteatr.model.Poster;
import parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class TheatreParser implements Parser<ArrayList<Poster>> {
    @Override
    public ArrayList<Poster> Parse(Document document) {
        ArrayList<Poster> posters = new ArrayList<Poster>();
        Elements postersElements = document.select("div.t_afisha");
        Path folderPath = Paths.get(System.getProperty("user.dir"), "images");

        try{
            Files.createDirectories(folderPath);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        for(Element poster : postersElements){
            String duration = poster.select(".td3 .td2 .td1 div").first().textNodes().get(0).text();
            Element td2Element = poster.select(".td2").first();
            String imageUrl = td2Element.select("img").first().absUrl("src");
            Element tInfoAfishaElement = poster.select(".t_info_afisha").first();
            String date = tInfoAfishaElement.select(".td1 .date_afisha").text();
            String title = tInfoAfishaElement.select("h3 a").textNodes().get(0).text();
            String ageLimit = tInfoAfishaElement.select(".value_limit").text();
            posters.add(new Poster(title, imageUrl, date, duration, ageLimit));

            if(imageUrl.startsWith("https"))
            {
                try
                {
                    URL url = new URL(imageUrl);
                    try (InputStream in = url.openStream()) {
                        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                        Path imagePath = Paths.get(folderPath.toString(), fileName);
                        Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return posters;
    }
}
