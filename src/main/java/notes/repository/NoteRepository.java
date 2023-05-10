package notes.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import notes.model.entity.Note;

@ApplicationScoped
public class NoteRepository implements PanacheMongoRepository<Note> {

}
