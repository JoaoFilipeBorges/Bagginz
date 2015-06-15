/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.filesHandler;

import isel.pfc.api.FileGroupInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.ClassLoader.getSystemResourceAsStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import com.google.common.io.*;
import java.util.stream.Collectors;
//import org.
/**
 *
 * @author João
 */
public class FileHandler {
    
    //Should just be one (theres something like this in AppDecoder)
    private static HashMap<String,String> map = new HashMap<>();
    static{
        map.put("LZW", "x");
        map.put("LZ77", "z");
    }
    
    public static BufferedReader readFile(String filePath){
        return new BufferedReader(new InputStreamReader(
                        getSystemResourceAsStream(filePath)));
    }
    //KInda stupid having two methods for the same thing, this one and listFiles at location
    public static List<File> getFilesAtLocation(String path){
        return Arrays.asList(new File(path).listFiles());
    }
    /**
     * Used for LZ77
     */
    public static String readFileToString(String path) throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
    
    public static List<String> getKeys(){
        return map.entrySet().stream().map( E -> E.getValue()).collect(Collectors.toList());
    }
    
    public static byte [] readFileToByteArray(String path) throws IOException{
        return Files.readAllBytes(Paths.get(path));
    }
    
    public static Iterable<File> listFilesAtLocation(String path, Predicate<File> ext) throws IOException{
        List<File> list = new LinkedList<>();
        if(new File(path).isFile()){
            list.add(new File(path));
            return list;
        }
        //Files.walk(Paths.get(path)).forEach(filePath -> {if(filePath.)list.add(filePath.getFileName().toString());});
        //Files.
        File directory = new File(path);
        //list = Arrays.asList(directory.listFiles());
        for(File f : Arrays.asList(directory.listFiles())){
            if(ext.test(f))
                list.add(f);
        }
        //list = list.forEach((f) -> {if(ext.test(f))list.remove(f)});//.removeIf((f)-> {if((File)f.getName().endsWith(".java"))return true;});//forEach((f)->{if(f.getName().endsWith(".java"))return true;});
        return list;
    }
    /**
     * To be used in file listing (NEEDS TESTING!!)
     * @param path
     * @return 
     */
    public static HashMap<String,FileGroupInfo> listFilesOrdByExt(String path){
        File directory = new File(path);
        HashMap<String,FileGroupInfo>  filesGrouped = new HashMap<>();
        com.google.common.io.Files.getFileExtension(path);
        for(File f : Arrays.asList(directory.listFiles())){
            String ext = com.google.common.io.Files.getFileExtension(f.getName());
            if(filesGrouped.containsKey(ext)){
                FileGroupInfo g = filesGrouped.get(ext);
                g.addFileToGroup(f);
            }else{
                FileGroupInfo g = new FileGroupInfo(ext);
                g.addFileToGroup(f);
                filesGrouped.put(ext, g);
            }
        }
        return filesGrouped;
    }
    
    //Include which algorithm in the future
    /*
    public static String buildHeader(List<File> files, String technique){
        StringBuilder info = new StringBuilder();
        info.append(map.get(technique)).append( files.size());
        StringBuilder sb = new StringBuilder();
        for(File f : files){
            String s = f.getName()+"<"+f.length()+"<";
            sb.append(s);
        }
        return sb.toString();
    }
    */
    public static String buildHeader(List<File> files, String technique){
        StringBuilder fInfo = new StringBuilder();
        for(File f : files){
            fInfo.append(f.getName()+"<"+f.length()+"<");
        }
        byte [] arr = new byte[4];
        int totalSz = 4+fInfo.length();
        arr [0] = (byte)map.get(technique).charAt(0);
        arr [1] = (byte)files.size();
        arr [2] = (byte)((0xff00 & totalSz) >> 8);
        arr [3] = (byte)(0xff & totalSz);
        byte [] fInfoBytes = fInfo.toString().getBytes();
        //byte [] all = ArrayUtils.addAll(arr,fInfoBytes);
        //int totalSz = 4+fInfo.length();
        //return map.get(technique)+files.size()+""+totalSz+""+fInfo;
        return new String(arr)+fInfo;
    }
    
    public static byte [] buildHeaderB(List<File> files, String technique){
        StringBuilder fInfo = new StringBuilder();
        for(File f : files){
            fInfo.append(f.getName()+"<"+f.length()+"<");
        }
        byte [] fInfoBytes = fInfo.toString().getBytes();
        byte [] arr = new byte[4+fInfoBytes.length];
        System.arraycopy(fInfoBytes, 0, arr, 4, fInfoBytes.length);
        int totalSz = 4+fInfo.length();
        arr [0] = (byte)map.get(technique).charAt(0);
        arr [1] = (byte)files.size();
        arr [2] = (byte)((0xff00 & totalSz) >> 8);
        arr [3] = (byte)(0xff & totalSz);
        //byte [] fInfoBytes = fInfo.toString().getBytes();
        //byte [] all = ArrayUtils.addAll(arr,fInfoBytes);
        //int totalSz = 4+fInfo.length();
        //return map.get(technique)+files.size()+""+totalSz+""+fInfo;
        return arr;
    }
    
    public static File mergeFiles(List<File> files) throws IOException{
        if(files.size()==1)
            return files.get(0);
        File f = new File(files.get(0).getParent()+"\\temp.tmp");
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f))){
            for(File file : files){
                bufferedWriter.write(readFileToString(file.getAbsolutePath()));
            }
            
        }
        return f;
    }
    
    //This wont need the two last params
    public static File mergeFiles(List<File> files, String path, String technique) throws IOException{
        if(files.size()==1)
            return files.get(0);
        //String header = buildHeader(files,technique);
        //String 
        File f = new File(path+"\\temp.tmp");
        
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
        //bufferedWriter.write(header);
        for(File file : files){
            bufferedWriter.write(readFileToString(file.getAbsolutePath()));
        }
        bufferedWriter.close();
        return f;
    }
    
}
