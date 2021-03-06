package com.dianping.wizard.repo;

import com.dianping.wizard.config.Configuration;
import com.dianping.wizard.repo.db.WidgetDBRepo;
import com.dianping.wizard.repo.local.WidgetLocalRepo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ltebean
 */
public class WidgetRepoFactory {

    private static final ConcurrentMap<String,WidgetRepo> repos;

    static {
        repos=new ConcurrentHashMap<String, WidgetRepo>();
        String mode= Configuration.get("mode","local",String.class);
        WidgetRepo localRepo=new WidgetLocalRepo();
        WidgetRepo dbRepo=new WidgetDBRepo();
        repos.put("local",localRepo);
        repos.put("db",dbRepo);
        if(mode.equals("local")){
            repos.put("default",localRepo);
        }else{
            repos.put("default",dbRepo);
        }
    }

    public static WidgetRepo getRepo(String name){
        WidgetRepo repo=repos.get(name);
        if (repo == null) {
            throw new WizardRepoException("widget repo not found: "+name);
        }
        return repo;
    }

    public static void registerRepo(String name, WidgetRepo repo){
        if(name==null||repo==null){
            throw new IllegalArgumentException("name or repo cannot be null");
        }
        repos.putIfAbsent(name,repo);
    }

    public static void replaceRepo(String name, WidgetRepo repo){
        if(name==null||repo==null){
            throw new IllegalArgumentException("name or repo cannot be null");
        }
        repos.replace(name,repo);
    }
}
