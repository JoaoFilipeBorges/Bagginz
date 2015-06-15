/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.encoding;

import isel.pfc.api.Technique;
import static isel.pfc.filesHandler.FileHandler.listFilesAtLocation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author João
 */
public class Encoder extends AbstractEncoding{  //File GEn will be obsolete
    
    public Encoder(){
        super();
    }
    
    private String buildHeader(List<File> files, String technique){
        StringBuilder sb = new StringBuilder();
        sb.append(super.encMap.get(technique)).append( files.size());
        for(File f : files){
            String s = f.getName()+"<"+f.length()+"<";
            sb.append(s);
        }
        return sb.toString();
    }
    
    public void encode(Technique t, String path) throws IOException{
        List<File> files = (List<File>) listFilesAtLocation(path,(f)->{if(f.getName().endsWith(".java"))return true;return false;});
        String header = buildHeader(files, t.getName());
        String tempName = path+"\\temp"+System.currentTimeMillis()+".tmp";
        byte [] codHeader = header.getBytes();
        File encFile = new File(tempName);
        FileOutputStream out = new FileOutputStream(encFile);
        out.write(codHeader);
    }
    
}
