/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Discipline;
import com.github.adminfaces.starter.model.Discipline_;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.template.exception.BusinessException;
import org.apache.deltaspike.data.api.criteria.Criteria;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;
import java.util.stream.Collectors;

/**
 * @author rmpestano
 */
@Stateless
public class DisciplineService extends CrudService<Discipline, Integer> implements Serializable {

    @Inject
    protected DisciplineRepository repo;//you can create repositories to extract complex queries from your service

    @Override
    public void beforeInsert(Discipline discipline) {
        validate(discipline);
    }

    @Override
    public void beforeUpdate(Discipline discipline) {
        validate(discipline);
    }

    public void validate(Discipline discipline) {
        BusinessException be = new BusinessException();
        if (!discipline.hasMatiere()) {
            be.addException(new BusinessException("La matière est obligatoire."));
        }
        if (!discipline.hasEnseignant()) {
            be.addException(new BusinessException("L'enseignant est obligatoire."));
        }
        if (!discipline.hasClasse()) {
            be.addException(new BusinessException("La discipline est obligatoire."));
        }
        if (count(criteria()
                .eq(Discipline_.matiere, discipline.getMatiere())
                .eq(Discipline_.enseignant, discipline.getEnseignant())
                .eq(Discipline_.classe, discipline.getClasse())
                .notEq(Discipline_.id, discipline.getId())) > 0) {

            be.addException(new BusinessException("La discipline " + discipline.getMatiere().getLibelle()
                    + " de la classe " + discipline.getClasse().getLibelle() + " enseigné par  "
                    + discipline.getEnseignant().getNom() + " " + discipline.getEnseignant().getPrenom() + "  existe déjà."));
        }

        if (has(be.getExceptionList())) {
            throw be;
        }
    }

    public List<Discipline> liste() {
        return repo.liste();
    }
    
    public List<Discipline> findByClasseMatiere(Classe classe, Matiere matiere){
        return repo.listeByMatiereClasse(matiere, classe);
    }

    public List<Discipline> getLibelle(String query) {
        List<Discipline> list = criteria()
                .distinct()
                .getResultList();
        return list.stream()
                .filter(d -> d.getClasse().getCode().contains(query)
                || d.getMatiere().getCode().contains(query)
                || (d.getEnseignant().getNom() + " " + d.getEnseignant().getPrenom()).contains(query))
                .collect(Collectors.toList());
    }
}
