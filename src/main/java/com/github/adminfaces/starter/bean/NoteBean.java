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
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Note;
import com.github.adminfaces.starter.service.NoteService;
import com.github.adminfaces.template.exception.BusinessException;
import static com.github.adminfaces.template.util.Assert.has;
import org.omnifaces.cdi.ViewScoped;

import javax.inject.Named;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.RowEditEvent;

/**
 * @author rmpestano
 */
@Named
@ViewScoped
@BeanService(NoteService.class)//use annotation instead of setter
public class NoteBean extends CrudMB<Note> implements Serializable {

    @Inject
    NoteService noteService;

    @Inject
    @Service
    CrudService<Note, Integer> noteCrudService; //generic injection example

    @Inject
    @Service
    CrudService<Examen, Integer> examenCrudService;

    ExamenBean examenBean;

    private List<Note> listeNote;

    private int currentExamenId;

    private static Examen exa;

    @Inject
    public void initService() {
        setCrudService(noteService);
    }

    @PostConstruct
    public void initNoteBean() {
        System.out.println("com.github.adminfaces.starter.bean.NoteBean.initNoteBean()");
    }

    public void arrangerParNote() {
//        entity.setExamen(exa);
//        filter.setEntity(entity);
//        list.setFilter(filter);
        List<Note> liste = list.getWrappedData();

        Comparator<Note> comp = (o1, o2) -> {
            Double d = o2.getNote() - o1.getNote();
            if (Math.abs(d) > 1) {
                return d.intValue();
            } else {
                if (d > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };

        int i = 0;
        liste.sort(comp);
        for (Note n : liste) {
            n.setRang(++i);
            noteService.update(n);
        }
    }

    public int nbrNoteParExamen(Examen examen) {
        return noteService.listeParExamen(examen).size();
    }

    public Examen findExamen(int examen) {
        return examenCrudService.findById(examen);
    }

    public void update() {
        if (exa != null && exa.getId() != null && exa.getId() != 0) {

            if ((exa.getId() == currentExamenId && currentExamenId != 0) || (exa.getId() != currentExamenId && currentExamenId == 0)) {
                entity.setExamen(exa);
                filter.setEntity(entity);
            }
            if (exa.getId() != currentExamenId && currentExamenId != 0) {
                exa = findExamen(currentExamenId);
                entity.setExamen(exa);
                filter.setEntity(entity);
            }
        } else {
            if (currentExamenId != 0) {
                exa = findExamen(currentExamenId);
                entity.setExamen(exa);
                filter.setEntity(entity);
            }
        }
//        arrangerParNote();

    }

    public void findNoteById(Integer id) {
        if (id == null) {
            throw new BusinessException("Provide Note ID to load");
        }
        Note noteFound = noteCrudService.findById(id);
        if (noteFound == null) {
            throw new BusinessException(String.format("No note found with id %s", id));
        }
        selectionList.add(noteFound);
        getFilter().addParam("id", id);
    }

    public void delete() {
        int numNote = 0;
        for (Note selectedNote : selectionList) {
            numNote++;
            noteService.remove(selectedNote);
        }
        selectionList.clear();
        addDetailMessage(numNote + " notex deleted successfully!");
        clear();
    }

    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);

        Examen examenParam = null;
        Note noteFilter = filter.getEntity();

        Integer idParam = null;
        if (filter.hasParam("id")) {
            idParam = filter.getIntParam("id");
        }

        if (has(idParam)) {
            sb.append("<b>id</b>: ").append(idParam).append(", ");
        }

        if (filter.hasParam("examen")) {
            examenParam = (Examen) filter.getParam("examen");
        } else if (has(noteFilter) && noteFilter.getExamen() != null) {
            examenParam = noteFilter.getExamen();
        }

        if (has(examenParam)) {
            sb.append("<b>examen</b>: ").append(examenParam.getDiscipline().getMatiere().getLibelle()).append(", ");
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
            addDetailMsg("Note " + entity.getId()
                    + " removed successfully");
            clear();
            sessionFilter.clear(NoteBean.class.getName());//removes filter saved in session for CarListMB.
        } catch (Exception e) {
            log.log(Level.WARNING, "", e);
        }
    }

    @Override
    public void afterInsert() {
        addDetailMsg("Note " + entity.getId() + " created successfully");
    }

    @Override
    public void afterUpdate() {
//        addDetailMsg("Note " + entity.getId() + " updated successfully");
    }

    public void onRowEdited(RowEditEvent event) {
        Note note = (Note) event.getObject();
        System.out.println("Note : " + note.getId());
        noteService.update(note);
//        arrangerParNote();
    }

    public int getCurrentExamenId() {
        return currentExamenId;
    }

    public void setCurrentExamenId(int currentExamenId) {
        this.currentExamenId = currentExamenId;
    }

    public Examen getExa() {
        return exa;
    }

    public void setExa(Examen aExa) {
        exa = aExa;
    }

    public List<Note> getListeNote() {
        return listeNote;
    }

    public void setListeNotex(List<Note> listeNote) {
        this.listeNote = listeNote;
    }

}
