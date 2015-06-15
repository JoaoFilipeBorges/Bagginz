/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.specialization;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

/**
 *
 * @author João
 */
public class Loader {
    
    private String DIR = "/config.txt";//Loader.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"//config.txt";
    String x = System.getProperty("user.dir")+"\\src\\data\\config.txt";
    private final String dir = "D:\\OneDrive\\Projecto final de curso 1415I\\Compressao Especializada\\CompressionApp\\configFiles\\genConfig.txt";
    //String y = "D:\\OneDrive\\Projecto final de curso 1415I\\Compressao Especializada\\CompressionApp\\configFiles"
    private String specificSpecDir = "D:\\OneDrive\\Projecto final de curso 1415I\\Compressao Especializada\\CompressionApp\\configFiles\\";
    /*
    public Loader(){
        DIR = new File("").getAbsolutePath()+"\\config.txt";
    }*/
    
    public File getAutoGenSpecialization(String name){
        return new File(specificSpecDir+name+".txt");
    }
    /**
     * 
     * @return 
     */
    public List<String> listAvailableSpecializations() throws URISyntaxException{
       /* String theDir = System.getProperty("user.dir")+"\\src\\data\\config.txt";
        URL u = getClass().getResource("config.txt");
        //File f1 = new File(u.toString());
        String xxx = u.getFile();
        File f1 = new File(xxx);
        //File f1 = new File("./config.txt");
        boolean bf1 = f1.isFile();
        //Path p0 = Paths.get(ClassLoader.getSystemResource(DIR).toURI());
        //boolean xpto = p0.isAbsolute();
        InputStream au = this.getClass().getResourceAsStream("config.txt");
        
        String p1 = u.getFile();//Paths.get(u.toURI());
        File f = new File("test");
        String mnmn = f.getAbsolutePath();
        String the1 = mnmn.substring(0, mnmn.lastIndexOf("\\"))+"\\PFC_APP.Specialization\\config.txt";
        File xx = new File(mnmn, DIR);
        boolean n = xx.isFile();
        boolean s= f.isFile();
        Path p = Paths.get(x);*/
        List<String> l = null;
        try(Stream<String> lines = Files.lines(Paths.get(dir))){
            l = lines.collect(toList());
        }catch(Exception ex){System.out.println(ex);}
        return l;
    }
    
}
