/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import isel.pfc.api.Technique;
import java.io.File;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.tukaani.xz.lzma.*;
import org.tukaani.xz.rangecoder.RangeCoder;
import org.tukaani.xz.rangecoder.RangeEncoder;

/**
 *
 * @author João
 */
@ServiceProvider(service = Technique.class)
public class LZMA_Main implements Technique{
    
    final String name = "lzma";
    File input;
    public LZMA_Main(){}
    
    public LZMA_Main(File f){
        input = f;
    }
    
    @Override
    public File Execute(String path, Supplier<File> compDecomp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File Execute(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File compress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File decompress(File f) {
        File outFile = new File(input.getParent()+"\\lzma.bgz");
        try{
            RangeEncoder c = new RangeEncoder();
            LZMAEncoderNormal enc= new LZMAEncoderNormal();
            
        }catch (Exception ex) {
            Logger.getLogger(LZMA_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outFile;
    }

    @Override
    public String getName() {
        return name;
    }
    
}
