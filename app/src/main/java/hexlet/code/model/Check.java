package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Check {
    private Long id;
    private Long urlId;
    private Integer status;
    private String title;
    private String header;
    private String description;
    private Timestamp createdAt;
}
