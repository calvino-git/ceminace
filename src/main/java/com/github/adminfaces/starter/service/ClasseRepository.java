package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.Classe;
import com.github.adminfaces.starter.model.Niveau;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;
import java.util.List;

@Repository
public interface ClasseRepository extends EntityRepository<Classe,Integer> {
    @Query("SELECT c FROM Classe c")
    List<Classe> liste();
    
    @Query("SELECT c FROM Classe c where c.niveau :niveau")
    List<Classe> liste(@QueryParam("niveau") Niveau niveau);


}
