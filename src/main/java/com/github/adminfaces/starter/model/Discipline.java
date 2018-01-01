/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.model;

import com.github.adminfaces.persistence.model.BaseEntity;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author calviniloki
 */
@Entity
@Table(name = "DISCIPLINE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Discipline.findAll", query = "SELECT d FROM Discipline d"),
    @NamedQuery(name = "Discipline.findById", query = "SELECT d FROM Discipline d WHERE d.id = :id"),
    @NamedQuery(name = "Discipline.findByCoefficient", query = "SELECT d FROM Discipline d WHERE d.coefficient = :coefficient")})
public class Discipline extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COEFFICIENT")
    private int coefficient;
    @JoinColumn(name = "CLASSE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Classe classe;
    @JoinColumn(name = "ENSEIGNANT", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Enseignant enseignant;
    @JoinColumn(name = "MATIERE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Matiere matiere;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discipline")
    private Collection<Examen> examenCollection;

    public Discipline() {
    }

    public Discipline(Integer id) {
        this.id = id;
    }

    public Discipline(Integer id, int coefficient) {
        this.id = id;
        this.coefficient = coefficient;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    @XmlTransient
    public Collection<Examen> getExamenCollection() {
        return examenCollection;
    }

    public void setExamenCollection(Collection<Examen> examenCollection) {
        this.examenCollection = examenCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discipline)) {
            return false;
        }
        Discipline other = (Discipline) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "main.Discipline[ id=" + id + " ]";
    }

    public boolean hasMatiere() {
        return matiere != null && matiere.hasCode() && !"".equals(matiere.getCode().trim());
    }

    public boolean hasEnseignant() {
        return enseignant != null && enseignant.hasNom() && !"".equals(enseignant.getNom().trim());
    }

    public boolean hasClasse() {
return classe != null && classe.hasCode() && !"".equals(classe.getCode().trim());
    }

}
