/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.DictSpecialization;

import isel.pfc.api.Specialization;
import static isel.pfc.filesHandler.FileHandler.listFilesAtLocation;
import isel.pfc.lzw.LZW;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author João
 */
public class LZWSpecial implements Specialization{
    
    private final String name = "LZWSpecial";
    private final String configPath = "";
    private List<String> formats = null;
    private LZW lzw = new LZW();
    private File tokens;
    @Override
    public String getName() {
        return name;
    }
    
    public LZWSpecial(){}
    
    public LZWSpecial(File file){
        tokens = file;
    }

    @Override
    public List<String> getFormatsApplied(){
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(configPath)).stream().map( s -> s.split(",")[1]).distinct().collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(LZWSpecial.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }

    @Override
    public String getFormat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File executeTechnique(String path) {
        try{
            File inputFile = new File(path);
            List<File> files = (List<File>) listFilesAtLocation(path,(x)->{if(x.getName().endsWith(".java"))return true;return false;});
            BufferedReader r = new BufferedReader(new FileReader(tokens));
            r.readLine();   //skip the line with commas
            lzw.setDictionary(r);
            
            File outpuFile = new File(path+"\\LZWSpecial.bgz");
            lzw.compress(null, null);
            //in.close();
            //out.flush();
            //out.close();    
        }catch(IOException ex) {
            //Logger.getLogger(JLZW.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;    
    }
    
}
