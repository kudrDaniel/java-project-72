package hexlet.code.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {
    public static String getOnlyYear(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy"));
    }

    public static String getDateBySlashWithTime(Timestamp timestamp) {
        return getDateBySlashWithTime(timestamp.toLocalDateTime());
    }

    public static String getDateBySlashWithTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm"));
    }
}
