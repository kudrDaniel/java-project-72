package hexlet.code.provider;

import lombok.Getter;

import java.util.StringJoiner;

@Getter
public class FlashProvider {
    public static final String NONE = "";
    public static final String ALERT_INFO = "alert-info";
    public static final String ALERT_DANGER = "alert-danger";
    public static final String ALERT_SUCCESS = "alert-success";

    public record Flash(String type, String message) {
        @Override
        public String toString() {
            var strJnr = new StringJoiner("\n");

            strJnr.add(this.getClass().getName() + ":{");
            strJnr.add("\t" + type.getClass().getName() + " type=" + type);
            strJnr.add("\t" + message.getClass().getName()  + " message=" + message);
            strJnr.add("}");

            return strJnr.toString();
        }
    }
}
