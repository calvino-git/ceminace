/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Classe_;
import com.github.adminfaces.starter.model.Niveau;
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
public class ClasseService extends CrudService<Classe, Integer> implements Serializable {

    @Inject
    protected ClasseRepository repo;//you can create repositories to extract complex queries from your service

    @Override
    public void beforeInsert(Classe classe) {
        validate(classe);
    }

    @Override
    public void beforeUpdate(Classe classe) {
        validate(classe);
    }

    public void validate(Classe classe) {
        BusinessException be = new BusinessException();
        if (!classe.hasCode()) {
            be.addException(new BusinessException("Le code est obligatoire."));
        }
        if (!classe.hasLibelle()) {
            be.addException(new BusinessException("Le libellé est obligatoire."));
        }
        if (!classe.hasCycle()) {
            be.addException(new BusinessException("Le cycle est obligatoire."));
        }
        if (!classe.hasNiveau()) {
            be.addException(new BusinessException("Le niveau d'étude est obligatoire."));
        }

        if (count(criteria()
                .eqIgnoreCase(Classe_.code, classe.getCode())
                .eqIgnoreCase(Classe_.libelle, classe.getLibelle())
                .notEq(Classe_.id, classe.getId())) > 0) {

            be.addException(new BusinessException("La classe " + classe.getCode() 
                    + ": " +classe.getLibelle() + " au " + classe.getCycle() + "  existe déjà."));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }

    public List<Classe> liste(){
        return repo.liste();
    }

    public List<Classe> listeParCycle(String cycle) {
        return criteria()
                .likeIgnoreCase(Classe_.cycle, cycle)
                .getResultList();
    }
    
    public List<Classe> listeParNiveau(Niveau niveau) {
        return criteria()
                .eq(Classe_.niveau, niveau)
                .getResultList();
    }

    public List<Classe> getLibelle(String query) {
        return criteria()
                .likeIgnoreCase(Classe_.libelle, "%" + query + "%")
                .distinct()
                .getResultList();
    }
    
    public List<Niveau> listeNiveau() {
        return criteria()
                .select(Niveau.class, attribute(Classe_.niveau))
                .distinct()
                .getResultList();
    }

}
