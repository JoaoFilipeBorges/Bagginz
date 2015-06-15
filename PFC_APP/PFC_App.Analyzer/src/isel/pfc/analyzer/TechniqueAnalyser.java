/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.analyzer;

import isel.pfc.api.Specialization;
import isel.pfc.api.Technique;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import org.openide.util.Lookup;

import org.openide.util.lookup.*;
import org.openide.windows.TopComponent;

/**
 *
 * @author João
 */
public class TechniqueAnalyser extends TopComponent {
    
    private InstanceContent content;
    //final Lookup.Result<Specialization> result;
    
    public TechniqueAnalyser(){
        //result = org.openide.util.Utilities.actionsGlobalContext().lookupResult(Specialization.class);
        //result.addLookupListener(this);
        content = new InstanceContent();
        associateLookup(new AbstractLookup(content));
        
    }
    
    /*
    public HashMap<String,? extends Technique> analyseExtensionsForSpecialization(Iterable<File> files){
        HashMap<String,? extends Technique> spMap = new HashMap<>();
        Collection<? extends Specialization> specials = Lookup.getDefault().lookupAll(Specialization.class);
        
        for(File f : files){
            
        }
        return spMap;
    }
    */
    
    public HashMap<Specialization,List<File>> analyseFileExtensionsForSpecialization(List<File> files){
        HashMap<Specialization,List<File>> spMap = new HashMap<>();
        Collection<? extends Specialization> specials = Lookup.getDefault().lookupAll(Specialization.class);
        
        for(Specialization s : specials){
            LinkedList<File> fList = new LinkedList<>();
            for(File f : files){
                if(f.getName().contains(s.getFormat())){
                    fList.add(f);
                }
            }
            if(fList.size()>0){
                spMap.put(s, fList);
            }
        }
        return spMap;
    }
    
    /*
    public Supplier AnalyseForSpecialization(String extension){
        //result.addLookupListener(this);
        //Collection<? extends Specialization> specials = result.allInstances();
        Collection<? extends Specialization> specials = Lookup.getDefault().lookupAll(Specialization.class);
        
        for(Specialization sp : specials){
            if(sp.getName().equals(extension)){
                return () -> sp.fetch();
            }
        }
        return null;
    }
    */
}
