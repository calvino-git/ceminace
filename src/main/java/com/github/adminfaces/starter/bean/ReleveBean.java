/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Discipline;
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Examen;
import com.github.adminfaces.starter.model.Matiere;
import com.github.adminfaces.starter.model.Note;
import com.github.adminfaces.starter.service.DisciplineService;
import com.github.adminfaces.starter.service.EleveService;
import com.github.adminfaces.starter.service.ExamenService;
import com.github.adminfaces.starter.service.NoteService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author calviniloki
 */
@Named
@ViewScoped
public class ReleveBean implements Serializable{
    private Classe classe;
    private Matiere matiere;
    private Integer trimestre;
    private List<Eleve> list ;
     int i=0;
    
    @Inject
    ExamenService examenService;
    
    @Inject
    NoteService noteService;
    
    @Inject
    EleveService eleveService;
    
    @Inject
    DisciplineService disciplineService;

    @Inject
    @Service
    CrudService<Examen, Integer> examenCrudService;
    
    private Map<Integer,Double> listRang;
    @PostConstruct
    public void init(){
        list = new ArrayList<>();
    }
    
    public void genererReleve(){
        list = eleveService.listeParClasse(classe);
        List<Double> listMoy = new ArrayList<>();
        list.forEach(e->{
            double moy = (findDisciplineByClasseMatiere(classe, matiere))
                .getCoefficient() * ((noteExamenParEleve(e, "COMPOSITION")
                +(((noteExamenParEleve(e, "INTERRO 1")
                + noteExamenParEleve(e, "INTERRO 2"))/2)
                + noteExamenParEleve(e, "EVALUATION"))/2)/2);
            listMoy.add(moy);
        });
        listMoy.sort((o1, o2) -> {
            return o1.compareTo(o2); //To change body of generated lambdas, choose Tools | Templates.
        });
         listRang = new HashMap();
       
        
        listMoy.forEach(r->{
            listRang.put(++i, r);
        });
    }
    
    public Discipline findDisciplineByClasseMatiere(Classe classe, Matiere matiere){
        List<Discipline> liste = disciplineService.findByClasseMatiere(classe, matiere);
        return liste!=null && !liste.isEmpty()?liste.get(0):null;
    }

    public Double noteExamenParEleve(Eleve eleve,String typeExamen ){
        Examen examen = null;
        Note note =  null;
        List<Examen> listExamen = examenService.listeParClasseMatiereTrimestre(eleve.getClasse(), matiere,trimestre);
        if(listExamen !=null && (!listExamen.isEmpty())){
            List<Examen> liste = listExamen.stream().filter(e->e.getType().equalsIgnoreCase(typeExamen))
                    .collect(Collectors.toList());
            if(liste.size() == 1){
                examen = liste.get(0);
            }else{
                System.out.println("liste examen " + liste.size());
            }
        }
        List<Note> listNote = null;
        if(examen != null){
            listNote = noteService.listeNoteParExamenEleve(examen, eleve);
        }
        if(listNote !=null && !listNote.isEmpty()){
            return listNote.get(0).getNote();
        }else{
            return 0.0;
        }
    }
    

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Integer getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Integer trimestre) {
        this.trimestre = trimestre;
    }

    public List<Eleve> getList() {
        return list;
    }

    public void setList(List<Eleve> list) {
        this.list = list;
    }

    public Map<Integer,Double> getListRang() {
        return listRang;
    }

    public void setListRang(Map<Integer,Double> listRang) {
        this.listRang = listRang;
    }
    
    
}
