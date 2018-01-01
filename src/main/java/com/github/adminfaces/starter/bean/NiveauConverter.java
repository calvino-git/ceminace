/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Niveau;
import com.github.adminfaces.starter.service.NiveauService;
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
public class NiveauConverter implements Serializable, Converter {

    @Inject
    private NiveauService niveauService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Niveau> niveaux = niveauService.liste();
        for (Niveau niveau : niveaux) {
            if (niveau.getTitre().equals(value)) {
                return niveau;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Niveau) value).getTitre();
    }
}
