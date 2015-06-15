/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.lzw;

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
public class LZW_Main implements Technique{
    
    public LZW_Main(){}
    
    String path;
    private final String name = "LZW";
    public LZW_Main(String path){
        this.path = path;
    }
    
    @Override
    public File compress(String path, String ext) { //need to check if path points to dirctory or file
        File outputFile = null;
        try {/*
            File inputFile = new File(path);
            String inName = inputFile.getName();
            String ext = inName.substring(inName.lastIndexOf("."));
            String newName = inName.replace(ext, ".bgz");
                */
            outputFile = new File(path+"\\xpto.bgz");
            File inputFile = null;
            List<File> files = (List<File>) listFilesAtLocation(path,(f)->{if(f.getName().endsWith(ext))return true;return false;});
            if(files.size()==1){
                inputFile = files.get(0);
                String name = inputFile.getName().replace(ext, "bgz");         //This will give problems if name contains the extension!
                outputFile = new File(path+"\\"+name);
            }
            else{
                inputFile = mergeFiles(files, path,getName());
                outputFile = new File(path+"\\"+ext+System.currentTimeMillis());
            }
            
            //String content = readFileToString(file.getAbsolutePath());
            //String header = buildHeader(files, this.name);
            byte [] header = buildHeaderB(files, this.name);
            LZW lzw = new LZW();
            FileInputStream in = new FileInputStream(inputFile);
            FileOutputStream out = new FileOutputStream(outputFile,true);
            //out.write(header.getBytes());
            out.write(header);
            lzw.compress(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(LZW_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputFile;
    }

    @Override
    public String getName() {
        return name;
    }

    

    @Override
    public File decompress(File f,int toSkip) {
        File decompressed = null;
        try{
            String path = f.getParent()+"\\foo.temp";
            decompressed = new File(path);
            LZW lzw = new LZW();
            try (FileInputStream in = new FileInputStream(f);FileOutputStream out = new FileOutputStream(decompressed)) {                
                in.skip(toSkip);
                lzw.decompress(in, out);
            }
            
        }catch(IOException ex){
            
        }
        return decompressed;
    }


}
