/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.jlzw;

import isel.pfc.api.Specialization;
import isel.pfc.api.Technique;
import static isel.pfc.filesHandler.FileHandler.listFilesAtLocation;
import static isel.pfc.filesHandler.FileHandler.mergeFiles;
import isel.pfc.lzw.LZW;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.ClassLoader.getSystemResourceAsStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author João
 */
@ServiceProvider(service = Specialization.class)
public class JLZW implements Specialization{
    private String name = "jlzw";
    private String tokFileName = new File("").getAbsolutePath()+"\\javaTokens.txt";
    private String [] _patterns = {"*/\r\n",";\r\n","}\r\n","{\r\n",".\r\n","();\r","\n\r\n","this.","for(","while(","catch(","throws","class","public class","){","private class","\nimport java.", "if(","else{"," static","private static","public static",
    "String","int","char","boolean","return"," [] ","= new ","new","private String","public String","private int","public int","private char","public char","priate boolean","public boolean"," = ", "void", "public void","private void","static void",
    "false","true","));\n","extends","implements","final"};
    LZW lzw = new LZW();
    
    
    
    private File buildTokenFile() throws IOException{  //FileHandler module could do this
        File f = new File(tokFileName);
        BufferedWriter w = new BufferedWriter(new FileWriter(f));
        for(String pattern : _patterns){    //must be atleast 2 of length
            StringBuilder sb = new StringBuilder();
            for(int i = 2;i<pattern.length();++i){
                String s = pattern.substring(0,i)+"\n"; //ou writeLine noutro tipo de writer
                w.write(s);
            }
        }
        w.close();
        return f;
    }
    

    @Override
    public String getName() {
        return name;
    }

    /*
    private File fetch() {
        File f = new File(tokFileName);
        if(!f.exists()){
            try {
                f = buildTokenFile();
            } catch (IOException ex) {
                Logger.getLogger(JLZW.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return f;
    }
    */
    //Move this to genFiles
    public static byte[] readFileToString(String path) throws IOException{
        return Files.readAllBytes(Paths.get(path));
    }
    
    @Override
    public List<String> getFormatsApplied(){return null;}

    @Override
    public File executeTechnique(String path) {
        File outputFile = null;
        try {
            File f = new File(tokFileName);
            if(!f.exists()){
                f = buildTokenFile();  
            }
            BufferedReader r = new BufferedReader(new FileReader(f.getAbsolutePath()));
            lzw.setDictionary(r);//readFileToString(f.getAbsolutePath()));   //just the name has the absolute path
            outputFile = new File(path+"\\xpto.bgz");
            List<File> files = (List<File>) listFilesAtLocation(path,(x)->{if(x.getName().endsWith(".java"))return true;return false;});
            File inputFile = mergeFiles(files, path,name);
            FileInputStream in = new FileInputStream(inputFile);
            FileOutputStream out = new FileOutputStream(outputFile);
            lzw.compress(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(JLZW.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputFile;
    }

    @Override
    public String getFormat() {
        return ".java";
    }
    
}
