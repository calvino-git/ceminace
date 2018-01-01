/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Classe_;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Enseignant_;
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
public class EnseignantService extends CrudService<Enseignant, Integer> implements Serializable {

    @Inject
    protected EnseignantRepository enseignantRepository;//you can create repositories to extract complex queries from your service

    @Override
    public void beforeInsert(Enseignant enseignant) {
        validate(enseignant);
    }

    @Override
    public void beforeUpdate(Enseignant enseignant) {
        validate(enseignant);
    }

    public void validate(Enseignant enseignant) {
        BusinessException be = new BusinessException();
        if (!enseignant.hasNom()) {
            be.addException(new BusinessException("Le nom est obligatoire."));
        }
        if (!enseignant.hasPrenom()) {
            be.addException(new BusinessException("le prenom est obligatoire."));
        }
        if (!enseignant.hasUser()) {
            be.addException(new BusinessException("user est obligatoire."));
        }

        if (count(criteria()
                .eqIgnoreCase(Enseignant_.nom, enseignant.getNom())
                .eqIgnoreCase(Enseignant_.prenom, enseignant.getPrenom())
                .eq(Enseignant_.user, enseignant.getUser())
                .notEq(Enseignant_.id, enseignant.getId())) > 0) {

            be.addException(new BusinessException("L'enseignant " + enseignant.getNom() 
                    + " " + enseignant.getPrenom() + "  existe déjà."));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }
    
    public List<Enseignant> getLibelle(String query) {
        return criteria()
                .likeIgnoreCase(Enseignant_.description, "%" + query + "%")
                .distinct()
                .getResultList();
    }

    public List<Enseignant> liste(){
        return enseignantRepository.liste();
    }


    public List<Enseignant> login(String user, String pass){
        return enseignantRepository.login(user,pass);
    }
}
