package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;
    private Check last;
}
