package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Discipline;
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Note;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends EntityRepository<Note,Integer> {
    @Query("SELECT n FROM Note n")
    List<Note> liste();
    
    @Query("SELECT n FROM Note n WHERE n.examen =:examen ")
    List<Note> listeParExamen(@QueryParam("examen") Examen examen);
    
    @Query("SELECT n FROM Note n WHERE n.examen =:examen and n.eleve=:eleve")
    List<Note> listeParExamenEleve(@QueryParam("examen") Examen examen,@QueryParam("eleve") Eleve eleve);
    

}
