package hexlet.code.util;

import java.util.Calendar;
import java.util.Date;

public final class Timestamp {
    public static String getPageTimestamp() {
        var now = new Date(System.currentTimeMillis());
        var cal = Calendar.getInstance();
        cal.setTime(now);
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }
}
