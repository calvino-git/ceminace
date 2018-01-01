/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.model.Examen;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 *
 * @author calviniloki
 */
public class ExamenAction implements ActionListener {
    @Override
    public void processAction(ActionEvent ev) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        Examen ex = (Examen) app.evaluateExpressionGet(context, "#{ex}",
                Examen.class);
        ELContext elContext = context.getELContext();
        ValueExpression ve = app.getExpressionFactory().createValueExpression(
                elContext, "#{noteBean.exa}", Examen.class);
        ve.setValue(elContext, ex);
    }
}
