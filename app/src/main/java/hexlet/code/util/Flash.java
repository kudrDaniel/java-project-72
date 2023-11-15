package hexlet.code.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Flash {
    private String flashType;
    private String flashMessage;

    public static String empty() {
        return "";
    }

    public static String alertInfo() {
        return "alert-info";
    }

    public static String alertDanger() {
        return "alert-danger";
    }

    public static String alertSuccess() {
        return "alert-success";
    }
}
