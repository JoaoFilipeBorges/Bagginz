/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.lz77;

import isel.pfc.api.Technique;
import static isel.pfc.filesHandler.FileHandler.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
/**
 *
 * @author João
 */
@ServiceProvider(service = Technique.class)
public class LZ77Technique implements Technique{
    
    private final String name = "LZ77";
    
    @Override
    public File compress(String path,String ext) {
        File file = null;
        try {
            List<File> files = (List<File>) listFilesAtLocation(path,(f)->{if(f.getName().endsWith(".java"))return true;return false;});
            file = mergeFiles(files, path,getName());
            String content = readFileToString(file.getAbsolutePath());
            LZ77 lz77 = new LZ77();
            String comp = lz77.compress(content);
            FileOutputStream out = new FileOutputStream(file);
            out.write(comp.getBytes());
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(LZ77Technique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File decompress(File f, int toSkip) {
        File decomp = null;
        try{
            String path = f.getParent()+"\\foo.temp";
            decomp = new File(path);
            LZ77 lz77 = new LZ77();
            try(FileInputStream in = new FileInputStream(f);FileOutputStream out = new FileOutputStream(decomp)){
                
            }
        }catch(IOException ex){
            
        }
        return decomp;
    }

   
    
}
