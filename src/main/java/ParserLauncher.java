import habr.HabrParser;
import habr.HabrSettings;
import kirovdramteatr.TheatreParser;
import kirovdramteatr.TheatreSettings;
import parser.Completed;
import parser.NewData;
import parser.ParserWorker;
import java.io.IOException;

public class ParserLauncher {
    private final String siteUrl;
    private int startPage;
    private int endPage;
    public ParserLauncher(String siteUrl){
        this.siteUrl = siteUrl;
    }
    public final void launchSite() throws IOException {
        ParserWorker<?> parser = null;
        if (isHabr()) {
            parser = new ParserWorker<>(new HabrParser());
            parser.setParserSettings(new HabrSettings(startPage, endPage));
        } else if (isTheatre()) {
            parser = new ParserWorker<>(new TheatreParser());
            parser.setParserSettings(new TheatreSettings(startPage, endPage));
        } else {
            System.out.println("Unknown site: " + siteUrl);
            return;
        }
        parser.onCompletedList.add(new Completed());
        parser.onNewDataList.add(new NewData());
        parser.Start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        parser.Abort();
    }

    public final void launchSiteWithoutPagination() throws IOException {
        startPage = 1;
        endPage = 1;
        launchSite();
    }

    public final void launchSiteWithPagination(int startPage, int endPage) throws IOException {
        this.startPage = startPage;
        this.endPage = endPage;
        launchSite();
    }
    private boolean isHabr() {
        return siteUrl.equals("https://habr.com/ru/all");
    }

    private boolean isTheatre() {
        return siteUrl.equals("https://kirovdramteatr.ru/afisha");
    }

}
