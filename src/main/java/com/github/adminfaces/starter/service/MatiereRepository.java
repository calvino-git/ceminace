package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.Matiere;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface MatiereRepository extends EntityRepository<Matiere,Integer> {
    @Query("SELECT m FROM Matiere m where m.specialite :specialite")
    List<Matiere> liste(@QueryParam("specialite") String specialite);

    @Query("SELECT m FROM Matiere m")
    List<Matiere> liste();

}
