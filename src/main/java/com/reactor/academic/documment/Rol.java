package com.reactor.academic.documment;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Data
@Document(collection = "roles")
public class Rol {

    @Id
    private String id;
    private String name;

}
