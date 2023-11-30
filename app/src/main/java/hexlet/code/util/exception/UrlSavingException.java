package hexlet.code.util.exception;

import lombok.Getter;

@Getter
public class UrlSavingException extends Exception {
    public enum State {
        URL_INCORRECT,
        URL_EXISTS;
    }

    private final State state;

    public UrlSavingException(String message, State state) {
        super(message);
        this.state = state;
    }
}
