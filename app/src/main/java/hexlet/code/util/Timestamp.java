package hexlet.code.util;

import java.util.Calendar;
import java.util.Date;

public final class Timestamp {
    private static final Date FROM = new Date(1699946070569L);
    public static String getTimestamp() {
        var now = new Date(System.currentTimeMillis());
        var cal = Calendar.getInstance();
        cal.setTime(FROM);
        int fromYear = cal.get(Calendar.YEAR);
        cal.setTime(now);
        int toYear = cal.get(Calendar.YEAR);
        return fromYear + " - " + toYear;
    }
}
