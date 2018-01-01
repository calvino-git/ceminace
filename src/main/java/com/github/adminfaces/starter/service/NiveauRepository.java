package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.Niveau;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface NiveauRepository extends EntityRepository<Niveau,Integer> {
    @Query("SELECT n FROM Niveau n where n.titre :titre")
    List<Niveau> liste(@QueryParam("titre") String titre);

    @Query("SELECT n FROM Niveau n")
    List<Niveau> liste();

}
