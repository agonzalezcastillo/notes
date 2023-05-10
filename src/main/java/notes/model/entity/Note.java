package notes.model.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note extends PanacheMongoEntity {

    private ObjectId id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

}
