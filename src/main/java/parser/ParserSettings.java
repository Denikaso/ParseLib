package parser;

import lombok.Builder;
import lombok.Data;
import lombok.val;

@Data
public abstract class ParserSettings {
    public static String BASE_URL;
    public static String PREFIX;
    protected int startPoint;
    protected int endPoint;
}
