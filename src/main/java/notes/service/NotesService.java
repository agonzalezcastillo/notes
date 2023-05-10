package notes.service;

import notes.model.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NotesService {

    Optional<Note> findById(String id);

    Optional<Note> findByTitle(String title);

    List<Note> findAllNotes();

    String persistNote(Note note);

    Optional<Note> updateNote(String id, Note note);

    Boolean deleteNote(String id);

    Boolean populateDb();


}
