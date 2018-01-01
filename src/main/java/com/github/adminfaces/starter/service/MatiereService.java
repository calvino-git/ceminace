/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Enseignant_;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Matiere_;
import com.github.adminfaces.template.exception.BusinessException;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;

/**
 * @author rmpestano
 */
@Stateless
public class MatiereService extends CrudService<Matiere, Integer> implements Serializable {

    @Inject
    protected MatiereRepository matiereRepository;//you can create repositories to extract complex queries from your service

    @Override
    public void beforeInsert(Matiere matiere) {
        validate(matiere);
    }

    @Override
    public void beforeUpdate(Matiere matiere) {
        validate(matiere);
    }

    public void validate(Matiere matiere) {
        BusinessException be = new BusinessException();
        if (!matiere.hasCode()) {
            be.addException(new BusinessException("Le code est obligatoire."));
        }
        if (!matiere.hasLibelle()) {
            be.addException(new BusinessException("le libelle est obligatoire."));
        }
        if (!matiere.hasSpecialite()) {
            be.addException(new BusinessException("le specialite est obligatoire."));
        }

        if (count(criteria()
                .eqIgnoreCase(Matiere_.code, matiere.getCode())
                .eqIgnoreCase(Matiere_.libelle, matiere.getLibelle())
                .eqIgnoreCase(Matiere_.specialite, matiere.getSpecialite())
                .notEq(Matiere_.id, matiere.getId())) > 0) {

            be.addException(new BusinessException("La matiere " + matiere.getCode() 
                    + " domaine " + matiere.getSpecialite() + "  existe déjà."));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }


    public List<Matiere> listeParSpecialite(String spec) {
        return matiereRepository.liste(spec);
    }

    public List<String> getLibel(String query) {
        return criteria()
                .select(String.class, attribute(Matiere_.libelle))
                .likeIgnoreCase(Matiere_.libelle, "%" + query + "%")
                .distinct()
                .getResultList();
    }
    
    public List<String> getSpecialite(String query) {
        return criteria()
                .select(String.class, attribute(Matiere_.specialite))
                .likeIgnoreCase(Matiere_.specialite, "%" + query + "%")
                .distinct()
                .getResultList();
    }
    
    public List<Matiere> getLibelle(String query) {
        return criteria()
                .likeIgnoreCase(Matiere_.libelle, "%" + query + "%")
                .distinct()
                .getResultList();
    }
    
    public List<Matiere> liste(){
        return matiereRepository.liste();
    }


}
