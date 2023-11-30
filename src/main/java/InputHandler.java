import java.io.IOException;

public class InputHandler {
    public static void handleArguments(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java -jar ваш_файл.jar siteUrl [startPage endPage]");
            return;
        }

        String siteUrl = args[0];
        ParserLauncher parserLauncher = new ParserLauncher(siteUrl);

        if (args.length == 1) {
            parserLauncher.launchSiteWithoutPagination();
        } else if (args.length == 3) {
            int startPage = Integer.parseInt(args[1]);
            int endPage = Integer.parseInt(args[2]);
            parserLauncher.launchSiteWithPagination(startPage, endPage);
        } else {
            System.out.println("Invalid number of arguments");
        }
    }
}