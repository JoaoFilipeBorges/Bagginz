/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.filesHandler;

import isel.pfc.api.FileLoader;
import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;
import org.openide.util.lookup.ServiceProvider;
/**
 *
 * @author João
 */
@ServiceProvider(service = FileLoader.class)
public class LoadFiles implements FileLoader{

        @Override
    public Iterable<File> process(String path) {
        return Arrays.asList(new File(path).listFiles());
    }
    
    public void buildHeader(Iterable<File> files, OutputStream out){ //Change to behaviour param.
        StringBuilder sb = new StringBuilder();
        for(File f : files){
            String s = f.getName()+"%"+f.length()+"\n";
            
        }
        
    }
    
}
