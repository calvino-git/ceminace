package com.github.adminfaces.starter.bean;

import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Car;
import com.github.adminfaces.starter.model.Car_;
import com.github.adminfaces.starter.model.Niveau;
import com.github.adminfaces.starter.model.Niveau_;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;

@Named
@ApplicationScoped
public class Lists implements Serializable {

    @Inject
    @Service
    private CrudService<Car, Integer> crudService;
    @Inject
    @Service
    private CrudService<Niveau, Integer> niveauCrudService;

    private SimpleDateFormat format;

    @PostConstruct
    public void init() {
        format = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    }

    @Produces
    @Named("models")
    public List<String> models() {
        return crudService.criteria()
                .select(String.class, crudService.attribute(Car_.model))
                .getResultList();
    }

    @Produces
    @Named("cycles")
    public List<String> cycles() {
        return niveauCrudService.criteria()
                .select(String.class, niveauCrudService.attribute(Niveau_.cycle))
                .getResultList();
    }

    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

}
