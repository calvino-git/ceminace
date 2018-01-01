/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Eleve_;
import com.github.adminfaces.starter.model.Examen;
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
public class EleveService extends CrudService<Eleve, Integer> implements Serializable {

    @Inject
    protected EleveRepository eleveRepository;//you can create repositories to extract complex queries from your service

    @Override
    public void beforeInsert(Eleve eleve) {
        validate(eleve);
    }

    @Override
    public void beforeUpdate(Eleve eleve) {
        validate(eleve);
    }

    public void validate(Eleve eleve) {
        BusinessException be = new BusinessException();
        if (!eleve.hasNom()) {
            be.addException(new BusinessException("Le nom est obligatoire."));
        }
        if (!eleve.hasPrenom()) {
            be.addException(new BusinessException("le prenom est obligatoire."));
        }
        if (!eleve.hasClasse()) {
            be.addException(new BusinessException("la classe est obligatoire."));
        }

        if (count(criteria()
                .eqIgnoreCase(Eleve_.nom, eleve.getNom())
                .eqIgnoreCase(Eleve_.prenom, eleve.getPrenom())
                .eq(Eleve_.classe, eleve.getClasse())
                .notEq(Eleve_.id, eleve.getId())) > 0) {

            be.addException(new BusinessException("L'eleve " + eleve.getNom() 
                    + " " + eleve.getPrenom() + " de la classe " + eleve.getClasse().getLibelle() + "  existe déjà."));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }


    public List<Eleve> listeParClasse(Classe classe) {
        return eleveRepository.listeParClasse(classe);
    }
    
    public List<Eleve> liste(){
        return eleveRepository.liste();
    }


}
