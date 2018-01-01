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
import static com.github.adminfaces.persistence.util.Messages.addDetailMessage;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.service.EnseignantService;
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
@BeanService(EnseignantService.class)//use annotation instead of setter
public class EnseignantBean extends CrudMB<Enseignant> implements Serializable {

    @Inject
    EnseignantService enseignantService;

    @Inject
    @Service
    CrudService<Enseignant, Integer> enseignantCrudService; //generic injection example

    private List<Enseignant> listeEnseignant;

    @Inject
    public void initService() {
        setCrudService(enseignantService);
    }

    public List<Enseignant> findByLibelle(String query) {
        return enseignantService.getLibelle(query);
    }

    public List<Enseignant> getListeEnseignant() {
        return listeEnseignant;
    }

    public void setListeEnseignantx(List<Enseignant> listeEnseignant) {
        this.listeEnseignant = listeEnseignant;
    }

    @PostConstruct
    public void liste() {
        listeEnseignant = enseignantService.liste();
    }

    public void findEnseignantById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Enseignant ID to load");
        }
        Enseignant enseignantFound = enseignantCrudService.findById(id);
        if (enseignantFound == null) {
            throw new BusinessException(String.format("No enseignant found with id %s", id));
        }
        selectionList.add(enseignantFound);
        getFilter().addParam("id", id);
    }

    public void delete() {
        int numEnseignant = 0;
        for (Enseignant selectedEnseignant : selectionList) {
            numEnseignant++;
            enseignantService.remove(selectedEnseignant);
        }
        selectionList.clear();
        addDetailMessage(numEnseignant + " enseignantx deleted successfully!");
        clear();
    }

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        String nomParam = null;
        String prenomParam = null;
        Enseignant enseignantFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("nom")) {
            nomParam = filter.getStringParam("nom");
        } else if (has(enseignantFilter) && enseignantFilter.getNom() != null) {
            nomParam = enseignantFilter.getNom();
        }

        if (has(nomParam)) {
            sb.append("<b>nom</b>: ").append(nomParam).append(", ");
        }

        if (filter.hasParam("prenom")) {
            prenomParam = filter.getStringParam("prenom");
        } else if (has(enseignantFilter) && enseignantFilter.getPrenom() != null) {
            prenomParam = enseignantFilter.getPrenom();
        }

        if (has(prenomParam)) {
            sb.append("<b>prenom</b>: ").append(prenomParam).append(", ");
        }
        int commaIndex = sb.lastIndexOf(",");

        if (commaIndex != -1) {
            sb.deleteCharAt(commaIndex);
        }

        if (sb.toString().trim().isEmpty()) {
            return "Pas de critère";
        }

        return sb.toString();
    }

    @Override
    public void afterRemove() {
        try {
            addDetailMsg("Enseignant " + entity.getNom() + " " + entity.getPrenom()
                    + " removed successfully");
            clear();
            sessionFilter.clear(EnseignantBean.class.getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Enseignant " + entity.getNom() + " " + entity.getPrenom() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Enseignant " + entity.getNom() + " " + entity.getPrenom() + " updated successfully");
    }

    public void onRowSelect(SelectEvent event) {
        this.entity = this.selection;
//        this.init();

        System.out.println("Enseignant : " + this.selection.getNom() + " " + this.selection.getPrenom());
    }

}
