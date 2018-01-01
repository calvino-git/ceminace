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
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.service.EleveService;
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
@BeanService(EleveService.class)//use annotation instead of setter
public class EleveBean extends CrudMB<Eleve> implements Serializable {

    @Inject
    EleveService eleveService;

    @Inject
    @Service
    CrudService<Eleve, Integer> eleveCrudService; //generic injection example

    private List<Eleve> listeEleve;

    @Inject
    public void initService() {
        setCrudService(eleveService);
    }


    public List<Eleve> getListeEleve() {
        return listeEleve;
    }

    public void setListeElevex(List<Eleve> listeEleve) {
        this.listeEleve = listeEleve;
    }

    @PostConstruct
    public void liste() {
        listeEleve = eleveService.liste();
    }
    
    public int nbrEleveParClasse(Classe classe){
        return eleveService.listeParClasse(classe).size();
    }
    

    public void findEleveById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Eleve ID to load");
        }
        Eleve eleveFound = eleveCrudService.findById(id);
        if (eleveFound == null) {
            throw new BusinessException(String.format("No eleve found with id %s", id));
        }
        selectionList.add(eleveFound);
        getFilter().addParam("id", id);
    }

    public void delete() {
        int numEleve = 0;
        for (Eleve selectedEleve : selectionList) {
            numEleve++;
            eleveService.remove(selectedEleve);
        }
        selectionList.clear();
        addDetailMessage(numEleve + " elevex deleted successfully!");
        clear();
    }

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        String nomParam = null;
        String prenomParam = null;
        Eleve eleveFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("nom")) {
            nomParam = filter.getStringParam("nom");
        } else if (has(eleveFilter) && eleveFilter.getNom() != null) {
            nomParam = eleveFilter.getNom();
        }

        if (has(nomParam)) {
            sb.append("<b>nom</b>: ").append(nomParam).append(", ");
        }
        
        if (filter.hasParam("prenom")) {
            prenomParam = filter.getStringParam("prenom");
        } else if (has(eleveFilter) && eleveFilter.getPrenom() != null) {
            prenomParam = eleveFilter.getPrenom();
        }

        if (has(prenomParam)) {
            sb.append("<b>prenom</b>: ").append(prenomParam).append(", ");
        }

        String classeParam = null;
        if (filter.hasParam("classe")) {
            classeParam = filter.getStringParam("classe");
        } else if (has(eleveFilter) && eleveFilter.getClasse() != null) {
            classeParam = eleveFilter.getClasse().getLibelle();
        }

        if (has(classeParam)) {
            sb.append("<b>classe</b>: ").append(classeParam).append(", ");
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
            addDetailMsg("Eleve " + entity.getNom() + " " + entity.getPrenom()
                    + " removed successfully");
            clear();
            sessionFilter.clear(EleveBean.class.getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Eleve " + entity.getNom() + " " + entity.getPrenom() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Eleve " + entity.getNom() + " " + entity.getPrenom() + " updated successfully");
    }

    public void onRowSelect(SelectEvent event) {
        this.entity = this.selection;
//        this.init();

        System.out.println("Eleve : " + this.selection.getNom() + " " + this.selection.getPrenom());
    }

}
