/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Enseignant;
import com.github.adminfaces.starter.service.EnseignantService;
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
public class EnseignantConverter implements Serializable, Converter {

    @Inject
    private EnseignantService enseignantService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Enseignant> enseignants = enseignantService.liste();
        for (Enseignant enseignant : enseignants) {
            if (value.equals(enseignant.getNom() + " " + enseignant.getPrenom())) {
                return enseignant;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Enseignant enseignant = ((Enseignant) value);
        return enseignant.getNom() + " " + enseignant.getPrenom();
    }
}
