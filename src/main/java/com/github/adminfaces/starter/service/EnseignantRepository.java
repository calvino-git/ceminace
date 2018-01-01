package com.github.adminfaces.starter.service;

import com.github.adminfaces.starter.model.Enseignant;
import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.QueryParam;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface EnseignantRepository extends EntityRepository<Enseignant,Integer> {

    @Query("SELECT e FROM Enseignant e")
    List<Enseignant> liste();
    
    @Query("SELECT e FROM Enseignant e where e.user=:user and e.pass=:pass")
    List<Enseignant> login(@QueryParam("user") String user, @QueryParam("pass") String pass);

}
