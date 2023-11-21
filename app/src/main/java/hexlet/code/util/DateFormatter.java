package hexlet.code.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {

    public static String timestampToString(Timestamp timestamp, String format) {
        return timestampToString(timestamp.toLocalDateTime(), format);
    }

    public static String timestampToString(LocalDateTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }
}
