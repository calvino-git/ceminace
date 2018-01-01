/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.persistence.bean.BeanService;
import com.github.adminfaces.persistence.bean.CrudMB;
import static com.github.adminfaces.persistence.bean.CrudMB.addDetailMsg;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Matiere;
import static com.github.adminfaces.persistence.util.Messages.addDetailMessage;
import com.github.adminfaces.starter.service.MatiereService;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;

/**
 * @author rmpestano
 */
@Named
@ViewScoped
@BeanService(MatiereService.class)//use annotation instead of setter
public class MatiereBean extends CrudMB<Matiere> implements Serializable {

    @Inject
    MatiereService matiereService;

    @Inject
    @Service
    CrudService<Matiere, Integer> matiereCrudService; //generic injection example

    private List<Matiere> listeMatiere;

    @Inject
    public void initService() {
        setCrudService(matiereService);
    }

    public List<String> getSpecialite(String query) {
        List<String> result = matiereService.getSpecialite(query);
        return result;
    }

    public List<Matiere> findByLibelle(String query){
        return matiereService.getLibelle(query);
    }
    public List<Matiere> getListeMatiere() {
        return listeMatiere;
    }

    public void setListeMatierex(List<Matiere> listeMatiere) {
        this.listeMatiere = listeMatiere;
    }

    @PostConstruct
    public void liste() {
        listeMatiere = matiereService.liste();
    }

    public void findMatiereById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Matiere ID to load");
        }
        Matiere matiereFound = matiereCrudService.findById(id);
        if (matiereFound == null) {
            throw new BusinessException(String.format("No matiere found with id %s", id));
        }
        selectionList.add(matiereFound);
        getFilter().addParam("id", id);
    }

    public void delete() {
        int numMatiere = 0;
        for (Matiere selectedMatiere : selectionList) {
            numMatiere++;
            matiereService.remove(selectedMatiere);
        }
        selectionList.clear();
        addDetailMessage(numMatiere + " matiere deleted successfully!");
        clear();
    }
    @Override
    public void afterRemove() {
        try {
            addDetailMsg("Matiere " + entity.getLibelle()
                    + " removed successfully");
            clear();
            sessionFilter.clear(MatiereBean.class.getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Matiere " + entity.getLibelle() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Matiere " + entity.getLibelle() + " updated successfully");
    }

    public void onRowSelect(SelectEvent event) {
        this.entity = this.selection;
//        this.init();

        System.out.println("Matiere : " + this.selection.getLibelle());
    }

}
