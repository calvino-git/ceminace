/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.service.ClasseService;
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
public class ClasseConverter implements Serializable, Converter {

    @Inject
    private ClasseService classeService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<Classe> classes = classeService.liste();
        for (Classe classe : classes) {
            if (classe.getLibelle().equals(value)) {
                return classe;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Classe) value).getLibelle();
    }
}
