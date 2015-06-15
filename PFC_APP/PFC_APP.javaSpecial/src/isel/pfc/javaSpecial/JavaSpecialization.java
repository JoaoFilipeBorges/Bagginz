/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.javaSpecial;

import isel.pfc.api.Specialization;
import isel.pfc.api.Technique;
import isel.pfc.lzw.LZW_Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author João
 */
@ServiceProvider(service = Specialization.class)
public class JavaSpecialization implements Specialization{
    
    private String [] _patterns = {"this.","for(","while(","catch(","throws","public","class","public class","){","private","import java."};
    private final String SPECIALIZATION_FILE = "javaTokens.txt";
    Technique technique = new LZW_Main();
    @Override
    public String getName() {
        return ".java";
    }
    
    private File buildTokenFile() throws IOException{  //FileHandler module could do this
        File f = new File(SPECIALIZATION_FILE);
        BufferedWriter w = new BufferedWriter(new FileWriter(f));
        for(String pattern : _patterns){    //must be atleast 2 of length
            StringBuilder sb = new StringBuilder();
            for(int i = 1;i<=pattern.length();++i){
                String s = pattern.substring(0,i)+"\n"; //ou writeLine noutro tipo de writer
                w.write(s);
            }
        }
        return f;
    }
    /*
    @Override
    public File fetch() {
        File f = new File(SPECIALIZATION_FILE);
        if(!f.exists()){
            try {
                f = buildTokenFile();
            } catch (IOException ex) {
                Logger.getLogger(JavaSpecialization.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return f;
    }

    @Override
    public Technique getTechnique() {
        return technique;
    }
    */
    @Override
    public String getFormat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File executeTechnique(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
