import lombok.val;
import parser.Completed;
import parser.NewData;
import parser.ParserWorker;

import java.io.IOException;

public class ParserLauncher {
    private final String siteUrl;
    private int startPage;
    private int endPage;

    public ParserLauncher(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public final void launchSite() throws IOException {
        val site = Site.fromUrl(siteUrl);

        if (site == null) {
            System.out.println("Unknown site: " + siteUrl);
            return;
        }

        ParserWorker<?> parser = null;
        try {
            parser = new ParserWorker<>(site.getParserClass().getDeclaredConstructor().newInstance());
            parser.setParserSetting(site.getSettingClass().getDeclaredConstructor(int.class, int.class)
                    .newInstance(startPage, endPage));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        parser.onCompletedList.add(new Completed());
        parser.onNewDataList.add(new NewData());
        parser.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        parser.abort();
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

}
