package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Eleve;
import com.github.adminfaces.starter.model.Examen;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface EleveRepository extends EntityRepository<Eleve,Integer> {
    @Query("SELECT e FROM Eleve e where e.classe =:classe")
    List<Eleve> listeParClasse(@QueryParam("classe") Classe classe);

    @Query("SELECT n.eleve FROM Note n WHERE n.examen =: examen ")
    List<Eleve> listeParExamen(@QueryParam("examen") Examen examen);
    
    @Query("SELECT e FROM Eleve e")
    List<Eleve> liste();

}
