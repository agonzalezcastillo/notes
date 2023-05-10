package notes.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;
import notes.model.entity.Note;
import notes.repository.NoteRepository;
import org.bson.types.ObjectId;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NotesServiceImpl implements NotesService{

    @Inject
    private NoteRepository noteRepository;

    @Override
    public Optional<Note> findById(String id) {
        if(id.isBlank() || id.isEmpty()){
            return Optional.empty();
        }
        return noteRepository.findByIdOptional(new ObjectId(id));
    }

    @Override
    public Optional<Note> findByTitle(String title) {
        if(title.isBlank() || title.isEmpty()){
            return Optional.empty();
        }
        return noteRepository.find("title",title).firstResultOptional();
    }

    @Override
    public List<Note> findAllNotes() {
        return noteRepository.listAll();
    }

    @Override
    public String persistNote(Note note) throws InvalidParameterException {
        if(note.getTitle().isEmpty() || note.getBody().isEmpty()){
            return null;
        }
        note.setCreatedAt(LocalDateTime.now());
        note.setLastModifiedAt(LocalDateTime.now());
        noteRepository.persist(note);
        return note.getId().toString();
    }

    @Override
    public Optional<Note> updateNote(String id,Note note) throws InvalidParameterException{
        if(note.getTitle().isEmpty() || note.getBody().isEmpty()){
            throw new InvalidParameterException();
        }

        var optionalNote = this.findById(id);
        if(optionalNote.isPresent()) {
            note.setLastModifiedAt(LocalDateTime.now());
            noteRepository.persistOrUpdate(note);
            return Optional.of(note);
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteNote(String id) throws InvalidParameterException {
        if(id.isBlank() || id.isEmpty()){
            throw new InvalidParameterException();
        }
        return noteRepository.deleteById(new ObjectId(id));
    }

    @Override
    public Boolean populateDb() {
        Log.info("starting population of DB");
        long startInMillis = System.currentTimeMillis();
        new Thread(() -> {
            for (int i = 0; i < 49_999; i++) {
                Note note = Note.builder()
                        .title("title"+i)
                        .body("body"+i)
                        .createdAt(LocalDateTime.now())
                        .lastModifiedAt(LocalDateTime.now())
                        .build();
                noteRepository.persistOrUpdate(note);
            }
        }).start();
        for (int i = 50_000; i < 99_999; i++) {
            Note note = Note.builder()
                    .title("title"+i)
                    .body("body"+i)
                    .createdAt(LocalDateTime.now())
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            noteRepository.persistOrUpdate(note);
        }
        long endInMillis = System.currentTimeMillis();
        Log.info(" population of DB finished in :" + (endInMillis-startInMillis));

        return true;
    }


}
