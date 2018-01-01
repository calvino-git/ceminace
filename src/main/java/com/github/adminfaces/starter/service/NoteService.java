/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.starter.model.Note;
import com.github.adminfaces.starter.model.Note_;
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Note_;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Note;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;


/**
 * @author rmpestano
 */
@Stateless
public class NoteService extends CrudService<Note, Integer> implements Serializable {

    @Inject
    protected NoteRepository repo;//you can create repositories to extract complex queries from your service

    public List<Note> listeParExamen(Examen examen) {
        return repo.listeParExamen(examen);
    }
    @Override
    protected Criteria<Note, Note> configRestrictions(Filter<Note> filter) {

        Criteria<Note, Note> criteria = criteria();

        //create restrictions based on parameters map
//        if (filter.hasParam("id")) {
//            criteria.eq(Note_.examen, (Examen) filter.getParam("examen"));
//        }
//
//        if (filter.hasParam("minPrice") && filter.hasParam("maxPrice")) {
//            criteria.between(Note_.price, filter.getDoubleParam("minPrice"), filter.getDoubleParam("maxPrice"));
//        } else if (filter.hasParam("minPrice")) {
//            criteria.gtOrEq(Note_.price, filter.getDoubleParam("minPrice"));
//        } else if (filter.hasParam("maxPrice")) {
//            criteria.ltOrEq(Note_.price, filter.getDoubleParam("maxPrice"));
//        }

        //create restrictions based on filter entity
        if (has(filter.getEntity())) {
            Note filterEntity = filter.getEntity();
            if (has(filterEntity.getExamen())) {
                criteria.eq(Note_.examen, filterEntity.getExamen());
            }
        }
        return criteria;
    }

    @Override
    public void beforeInsert(Note note) {
        validate(note);
    }

    @Override
    public void beforeUpdate(Note note) {
        validate(note);
    }

    public void validate(Note note) {
        BusinessException be = new BusinessException();
        if (!note.hasExamen()) {
            be.addException(new BusinessException("L'examen est obligatoire."));
        }
        if (!note.hasEleve()) {
            be.addException(new BusinessException("L'eleve est obligatoire."));
        }
        if (count(criteria()
                .eq(Note_.examen, note.getExamen())
                .eq(Note_.eleve, note.getEleve())
                .notEq(Note_.id, note.getId())) > 0) {

            be.addException(new BusinessException("La note de l'examen " + note.getExamen().getTitre()
                    + " donné par  " 
                    + note.getExamen().getDiscipline().getEnseignant().getNom() +  " " + note.getExamen().getDiscipline().getEnseignant().getPrenom() + "  existe déjà."));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }

    public List<Note> liste(){
        return repo.liste();
    }
    
  public List<Note> listeNoteParExamenEleve(Examen examen, Eleve eleve){
      return repo.listeParExamenEleve(examen, eleve);
  }
}
