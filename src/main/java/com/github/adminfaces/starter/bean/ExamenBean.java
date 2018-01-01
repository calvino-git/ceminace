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
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Note;
import com.github.adminfaces.starter.service.EleveService;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.starter.service.NoteService;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;

/**
 * @author rmpestano
 */
@Named
@SessionScoped
@BeanService(ExamenService.class)//use annotation instead of setter
public class ExamenBean extends CrudMB<Examen> implements Serializable {

    @Inject
    ExamenService examenService;

    @Inject
    @Service
    CrudService<Examen, Integer> examenCrudService; //generic injection example

    @Inject
    EleveService eleveService;

    @Inject
    NoteService noteService;

    private List<Examen> listeExamen;

    @Inject
    public void initService() {
        setCrudService(examenService);
    }

    public void createNewExam() {
        this.save();
        Classe classe = this.entity.getDiscipline().getClasse();
        List<Eleve> eleves = eleveService.listeParClasse(classe);
        eleves.forEach(e -> {
            Note note = new Note();
            note.setEleve(e);
            note.setExamen(this.entity);
            noteService.insert(note);
        });

    }

    public void toNote() {
        Faces.redirect("note.xhtml");
    }

    public List<Examen> getListeExamen() {
        return listeExamen;
    }

    public void setListeExamen(List<Examen> listeExamen) {
        this.listeExamen = listeExamen;
    }

    @PostConstruct
    public void liste() {
        listeExamen = examenService.liste();
    }

    public Examen findExamenById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Examen ID to load");
        }
        return examenCrudService.findById(id);
    }

    public void delete() {
        int numExamen = 0;
        for (Examen selectedExamen : selectionList) {
            numExamen++;
            examenService.remove(selectedExamen);
        }
        selectionList.clear();
        addDetailMessage(numExamen + " examenx deleted successfully!");
        clear();
    }

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        Discipline disciplineParam = null;
        Examen examenFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("discipline")) {
            disciplineParam = (Discipline) filter.getParam("discipline");
        } else if (has(examenFilter) && examenFilter.getDiscipline() != null) {
            disciplineParam = examenFilter.getDiscipline();
        }

        if (has(disciplineParam)) {
            sb.append("<b>discipline</b>: ").append(disciplineParam.getMatiere().getLibelle()).append(", ");
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
            addDetailMsg("Examen " + entity.getTitre()
                    + " removed successfully");
            clear();
            sessionFilter.clear(ExamenBean.class
                    .getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Examen " + entity.getTitre() + " created successfully");
    }

    @Override
    public void afterUpdate() {
        addDetailMsg("Examen " + entity.getTitre() + " updated successfully");
    }

    public void onRowSelect(SelectEvent event) {
        this.entity = this.selection;
//        this.init();

        System.out.println("Examen : " + entity.getTitre());
    }
}
