import lombok.NoArgsConstructor;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class InputHandler {
    public void handleArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar ваш_файл.jar siteUrl [startPage endPage]");
            return;
        }

        val siteUrl = args[0];
        val parserLauncher = new ParserLauncher(siteUrl);

        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, () -> {
            try {
                parserLauncher.launchSiteWithoutPagination();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        actions.put(3, () -> {
            val startPage = Integer.parseInt(args[1]);
            val endPage = Integer.parseInt(args[2]);
            try {
                parserLauncher.launchSiteWithPagination(startPage, endPage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        actions.getOrDefault(args.length, () -> System.out.println("Invalid number of arguments")).run();
    }
}
