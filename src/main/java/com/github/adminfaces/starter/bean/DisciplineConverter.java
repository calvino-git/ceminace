/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Discipline;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.service.DisciplineService;
import com.github.adminfaces.starter.service.MatiereService;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author calviniloki
 */
@Named
@SessionScoped
public class DisciplineConverter implements Serializable, Converter {

    @Inject
    private DisciplineService service;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Discipline> discs = service.liste();
        for (Discipline disc : discs) {
            if ((disc.getMatiere().getCode() + " " + disc.getClasse().getCode()).equals(value)) {
                return disc;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Discipline disc = (Discipline) value;
        return disc.getMatiere().getCode() + " " + disc.getClasse().getCode();
    }
}
