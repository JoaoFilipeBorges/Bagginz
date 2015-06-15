/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.specialization;

import com.google.common.io.Files;
import isel.pfc.api.Technique;
import java.io.File;
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author João
 */
public class TechniqueAnalyser extends TopComponent{
    
    
    //Usar o collect to map com
    public String benchMarkFile(File f){
        File comp;
        Collection<? extends Technique> techns = Lookup.getDefault().lookupAll(Technique.class);
        String ext = "txt";//Files.getFileExtension(f.getAbsolutePath());
        String tName = null;
        long prevLen = 0;
        for(Technique t : techns){
            comp = t.compress(f.getAbsolutePath(), ext);
            if(prevLen == 0){
                prevLen = comp.length();
                tName = t.getName();
                continue;
            }
            if(comp!=null){
                if(comp.length()<prevLen){
                    tName = t.getName();
                    prevLen = comp.length();
                }
            }
        }
        //comp.delete();
        return tName;
    }
    
}
