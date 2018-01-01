package com.github.adminfaces.starter.bean;

import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import com.github.adminfaces.starter.model.Car;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.stream.IntStream;
import javax.naming.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class InitAppMB implements Serializable {

    @Inject
    @Service
    private CrudService<Car, Integer> crudService;


    @PostConstruct
    public void init() {

//        IntStream.rangeClosed(1, 50).forEach(i -> create(i));
    }

    private void create(int i) {
        crudService.insert(new Car().model("model " + i).name("name" + i).price(Double.valueOf(i)));
    }

    public static Map toMap(Context ctx) throws NamingException {
        String namespace = ctx instanceof InitialContext ? ctx.getNameInNamespace() : "";
        HashMap<String, Object> map = new HashMap();
        System.out.println("> Listing namespace: " + namespace);
        NamingEnumeration<NameClassPair> list = ctx.list(namespace);
        while (list.hasMoreElements()) {
            NameClassPair next = list.next();
            String name = next.getName();
            String jndiPath = namespace + name;
            Object lookup;
            try {
                System.out.println("> Looking up name: " + jndiPath);
                Object tmp = ctx.lookup(jndiPath);
                if (tmp instanceof Context) {
                    System.out.println("OK: " + tmp.getClass().getName());
                    lookup = toMap((Context) tmp);
                } else {
                    System.out.println("NO : " + tmp.getClass().getName());
                    lookup = tmp.toString();
                }
            } catch (Throwable t) {
                System.err.println("ERROR : " + jndiPath + " : " + t.getLocalizedMessage());
                lookup = t.getMessage();
            }
            map.put(name, lookup);
        }
        return map;
    }
    
    

}
