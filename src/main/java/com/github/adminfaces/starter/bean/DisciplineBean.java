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
import com.github.adminfaces.starter.model.Discipline;
import com.github.adminfaces.starter.model.Discipline_;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Matiere_;
import com.github.adminfaces.starter.service.DisciplineService;
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
@BeanService(DisciplineService.class)//use annotation instead of setter
public class DisciplineBean extends CrudMB<Discipline> implements Serializable {

    @Inject
    DisciplineService disciplineService;

    @Inject
    @Service
    CrudService<Discipline, Integer> disciplineCrudService; //generic injection example

    private List<Discipline> listeDiscipline;

    @Inject
    public void initService() {
        setCrudService(disciplineService);
    }

public List<Discipline> findByLibelle(String query) {
        return disciplineService.getLibelle(query);
    }
    public List<Discipline> getListeDiscipline() {
        return listeDiscipline;
    }

    public void setListeDisciplinex(List<Discipline> listeDiscipline) {
        this.listeDiscipline = listeDiscipline;
    }

    @PostConstruct
    public void liste() {
        listeDiscipline = disciplineService.liste();
    }

    public void findDisciplineById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Discipline ID to load");
        }
        Discipline disciplineFound = disciplineCrudService.findById(id);
        if (disciplineFound == null) {
            throw new BusinessException(String.format("No discipline found with id %s", id));
        }
        selectionList.add(disciplineFound);
        getFilter().addParam("id", id);
    }

    public void delete() {
        int numDiscipline = 0;
        for (Discipline selectedDiscipline : selectionList) {
            numDiscipline++;
            disciplineService.remove(selectedDiscipline);
        }
        selectionList.clear();
        addDetailMessage(numDiscipline + " disciplinex deleted successfully!");
        clear();
    }

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        Matiere matiereParam = null;
        Enseignant enseignantParam = null;
        Classe classeParam = null;
        Discipline disciplineFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("classe")) {
            classeParam = (Classe) filter.getParam("classe");
        } else if (has(disciplineFilter) && disciplineFilter.getClasse() != null) {
            classeParam = disciplineFilter.getClasse();
        }

        if (has(classeParam)) {
            sb.append("<b>classe</b>: ").append(classeParam.getCode()).append(", ");
        }
        
        if (filter.hasParam("enseignant")) {
            enseignantParam = (Enseignant) filter.getParam("enseignant");
        } else if (has(disciplineFilter) && disciplineFilter.getEnseignant()!= null) {
            enseignantParam = disciplineFilter.getEnseignant();
        }

        if (has(enseignantParam)) {
            sb.append("<b>enseignant</b>: ").append(enseignantParam.getNom()).append(", ");
        }
        
        if (filter.hasParam("matiere")) {
            matiereParam = (Matiere) filter.getParam("matiere");
        } else if (has(disciplineFilter) && disciplineFilter.getMatiere()!= null) {
            matiereParam = disciplineFilter.getMatiere();
        }

        if (has(matiereParam)) {
            sb.append("<b>matiere</b>: ").append(matiereParam.getCode()).append(", ");
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
            addDetailMsg("Discipline " + entity.getClasse().getCode()+ "-" + entity.getMatiere().getCode()
                    + " removed successfully");
            clear();
            sessionFilter.clear(DisciplineBean.class.getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Discipline " + entity.getClasse().getCode()+ "-" + entity.getMatiere().getCode() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Discipline " + entity.getClasse().getCode()+ "-" + entity.getMatiere().getCode() + " updated successfully");
    }

    public void onRowSelect(SelectEvent event) {
        this.entity = this.selection;
//        this.init();

        System.out.println("Discipline : " + entity.getClasse().getCode()+ "-" + entity.getMatiere().getCode());
    }

}
