/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.api;

import java.io.File;

/**
 *
 * @author João
 */
public class AbstractTechnique implements Technique{

    @Override
    public File compress(String path, String extension) {
        File f = new File(path);
        try{
            //try(){
                
            //}
        }catch(Exception e){
            
        }
        return null;
    }

    @Override
    public File decompress(File f, int toSkip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
