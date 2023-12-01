package parser;

import lombok.Data;

@Data
public abstract class ParserSettings {
    public static String BASE_URL;
    public static String PREFIX;
    protected int startPoint;
    protected int endPoint;
}
