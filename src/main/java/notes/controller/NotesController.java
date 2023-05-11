package notes.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import notes.model.entity.Note;
import notes.service.NotesService;

import java.util.List;


@Path("/notes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotesController {

    @Inject
    NotesService notesService;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id){
        return notesService.findById(id)
                .map(note -> Response.ok(note).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response get(){
        return Response.ok(notesService.findAllNotes()).build();
    }

    @GET
    @Path("/search")
    public Response getByTitle(@QueryParam("title") String title){
        List<Note> notes = notesService.findByTitle(title);

        if(notes == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (notes.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(notes).build();
    }

    @POST
    public Response create(Note note){
        String noteId = notesService.persistNote(note);
        return noteId == null
                ? Response.status(Response.Status.BAD_REQUEST).build()
                : Response.ok(noteId).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Note note){
        return notesService.updateNote(id,note)
                .map(updatedNote -> Response.ok(updatedNote).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id){
        if(notesService.deleteNote(id)){
            return Response.noContent().build();
        }
        return Response.status(404).build();
    }

    @POST
    @Path("/populate")
    public Response populate(){
        return Response.ok(notesService.populateDb()).status(Response.Status.CREATED).build();
    }

}
