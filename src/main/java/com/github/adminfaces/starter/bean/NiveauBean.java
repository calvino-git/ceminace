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
import com.github.adminfaces.starter.model.Niveau;
import com.github.adminfaces.starter.service.NiveauService;
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
@BeanService(NiveauService.class)//use annotation instead of setter
public class NiveauBean extends CrudMB<Niveau> implements Serializable {

    @Inject
    NiveauService niveauService;

    @Inject
    @Service
    CrudService<Niveau, Integer> niveauCrudService; //generic injection example

    private List<Niveau> listeNiveaux;

    @Inject
    public void initService() {
        setCrudService(niveauService);
    }

    public List<String> completeCycle(String query) {
        List<String> result = niveauService.getCycles(query);
        return result;
    }

    public List<Niveau> getListeNiveaux() {
        return listeNiveaux;
    }

    public void setListeNiveaux(List<Niveau> listeNiveaux) {
        this.listeNiveaux = listeNiveaux;
    }

    @PostConstruct
    public void liste() {
        listeNiveaux = niveauService.liste();
    }

    public void findNiveauById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Niveau ID to load");
        }
        Niveau niveauFound = niveauCrudService.findById(id);
        if (niveauFound == null) {
            throw new BusinessException(String.format("No niveau found with id %s", id));
        }
        selectionList.add(niveauFound);
        getFilter().addParam("id", id);
    }

    public void delete() {
        int numNiveaux = 0;
        for (Niveau selectedNiveau : selectionList) {
            numNiveaux++;
            niveauService.remove(selectedNiveau);
        }
        selectionList.clear();
        addDetailMessage(numNiveaux + " niveaux deleted successfully!");
        clear();
    }

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        String titreParam = null;
        Niveau niveauFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("titre")) {
            titreParam = filter.getStringParam("titre");
        } else if (has(niveauFilter) && niveauFilter.getTitre() != null) {
            titreParam = niveauFilter.getTitre();
        }

        if (has(titreParam)) {
            sb.append("<b>titre</b>: ").append(titreParam).append(", ");
        }

        String cycleParam = null;
        if (filter.hasParam("cycle")) {
            cycleParam = filter.getStringParam("cycle");
        } else if (has(niveauFilter) && niveauFilter.getCycle() != null) {
            cycleParam = niveauFilter.getCycle();
        }

        if (has(cycleParam)) {
            sb.append("<b>cycle</b>: ").append(cycleParam).append(", ");
        }
        int commaIndex = sb.lastIndexOf(",");

        if (commaIndex != -1) {
            sb.deleteCharAt(commaIndex);
        }

        if (sb.toString().trim().isEmpty()) {
            return "No search criteria";
        }

        return sb.toString();
    }

    @Override
    public void afterRemove() {
        try {
            addDetailMsg("Niveau " + entity.getTitre()
                    + " removed successfully");
            clear();
            sessionFilter.clear(NiveauBean.class.getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Niveau " + entity.getTitre() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Niveau " + entity.getTitre() + " updated successfully");
    }

    public void onRowSelect(SelectEvent event) {
        this.entity = this.selection;
//        this.init();

        System.out.println("Niveau : " + this.selection.getTitre());
    }

}
