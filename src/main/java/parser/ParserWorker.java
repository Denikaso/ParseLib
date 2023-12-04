package parser;

import lombok.Data;
import lombok.val;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParserWorker<T> {
    Parser<T> parser;
    ParserSetting parserSetting;
    HtmlLoader loader;
    boolean isActive;
    protected List<OnNewDataHandler<T>> onNewDataList = new ArrayList<>();
    protected List<OnCompleted> onCompletedList = new ArrayList<>();
    public ParserWorker(Parser<T> parser) {
        this.parser = parser;
    }

    private void worker() throws IOException {
        for (int i = parserSetting.getStartPoint(); i <= parserSetting.getEndPoint(); i++) {
            if (!isActive) {
                onCompletedList.get(0).onCompleted(this);
                return;
            }
            val document = loader.getSourceByPageId(i);
            T result = parser.parse(document);
            onNewDataList.get(0).onNewData(this,result);
        }
        onCompletedList.get(0).onCompleted(this);
        isActive = false;
    }

    public void start() throws IOException {
        isActive = true;
        worker();
    }

    public void abort() {
        isActive = false;
    }
    public void setParserSetting(ParserSetting parserSetting) {
        this.parserSetting = parserSetting;
        loader = new HtmlLoader(parserSetting);
    }

    public interface OnNewDataHandler<T> {
        void onNewData(Object sender, T e);
    }

    public interface OnCompleted {
        void onCompleted(Object sender);
    }
}
