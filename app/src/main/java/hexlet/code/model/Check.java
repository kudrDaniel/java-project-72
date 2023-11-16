package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Check {
    private Long id;
    private Long urlId;
    private Integer status;

    @ToString.Include
    private String title;

    private String header;
    private String description;
    private Timestamp createdAt;
}
