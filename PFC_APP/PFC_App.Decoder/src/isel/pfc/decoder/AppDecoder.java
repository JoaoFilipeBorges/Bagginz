/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.decoder;

import com.google.common.io.Files;
import isel.pfc.DictSpecialization.LZWSpecial;
import isel.pfc.api.Technique;
import isel.pfc.filesHandler.FileHandler;
import isel.pfc.lz77.LZ77Technique;
import isel.pfc.lzw.LZW_Main;
import java.lang.Class;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import zlib.Deflate;

/**
 *
 * @author João
 */
public class AppDecoder {
    
    /*
    *   NO NEED!!
    *
    private static final class HeaderInfo{
        int numFiles;
        int headerEnd;
        HashMap<String, Integer> mergedFiles = new HashMap<>();
        
    }*/
    
    public static final int HEADER_MAX_SIZE = 1024;
    private static HashMap<String,Class> decMap = new HashMap<>();
    static{
        decMap.put("x", LZW_Main.class);//lzw    what about various dict sizes ???
        decMap.put("p", LZ77Technique.class);//lz77
        decMap.put("t", Deflate.class);//huffman
        decMap.put("o", null);//ahuffman
        decMap.put("i", null);
        decMap.put("s", null);
        decMap.put("e", null);
        decMap.put("l", null);
    }
    
    //WRONG!!!! INFO IS IN BINARY!!!
    //Cant receive a reader here. What if someone's readernot at the begining?
    /*
    public static Technique getTechnique(String path) throws IOException{
        BufferedReader reader = FileGen.readFile(path);
        return decMap.get(reader.readLine());
    }*/
    
    public static void processAutoGenSpecialization(File f) throws IOException{
        String [] vals = Files.readFirstLine(f, Charset.defaultCharset()).split(",");
        
        LZWSpecial technique = new LZWSpecial();
    }
    
    public static List<File> getCompressedFiles(File file) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        List<File> files = new ArrayList<>();
        byte [] headerInfo = new byte[60];
        FileInputStream in = new FileInputStream(file);
        in.read(headerInfo);
        String decHeaderInfo = new String(headerInfo);
        return files;
    }
    
    private static Class getTechnique(File f) throws IOException{
        byte [] t = new byte[1];
        FileInputStream in = new FileInputStream(f);
        in.read(t);
        return decMap.get(new String(t));
    }
    /**
     * 
     * Needs working!!!
     * 
     * 
     * @param f
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public static File decode(File f) throws IOException, InstantiationException, IllegalAccessException{
        Class<? extends Technique> c = getTechnique(f);
        Technique t = c.newInstance();
        //t.Execute(f.getPath());
        byte [] info = new byte[4];
        FileInputStream in = new FileInputStream(f);
        in.read(info);
        int toSkip = (info[2]<<8) | info[3];
        //String xpto = new String(info);
        File dec = t.decompress(f,toSkip);
        byte [] filesInfo = new byte [toSkip-4];
        in.read(filesInfo);
        String xpto = new String(filesInfo);
        HashMap<String,Integer> files = new HashMap<>();
        String [] vals = xpto.split("<");
        for(int i = 0; i<vals.length;i +=2){
            String fName = vals[i];
            int fLen = Integer.parseInt(vals[i+1]);
            files.put(fName, fLen);
        }
        List<File> list = unMergeFiles(dec, files);
        //dec.delete();
        java.nio.file.Files.delete(dec.toPath());
        return null;
    }
    
    private static List<File> unMergeFiles(File f,HashMap<String,Integer> fInfos) throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(f));
        //int num = fInfos.numFiles;
        File file;
        char [] buff;
        int offset = 0;//fInfos.headerEnd;
        ArrayList<File> files = new ArrayList<>();
        for(Entry<String,Integer> entry : fInfos.entrySet()){
            file = new File(f.getParent()+"\\"+entry.getKey());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            buff = new char [entry.getValue()];
            reader.read(buff, offset, buff.length);
            writer.write(buff, 0, buff.length);
            writer.close();
            files.add(file);
            offset += entry.getValue();//buff.length;
        }
        reader.close();
        return files;
    }
    
    /*
    private static HeaderInfo getMergedFilesInfo(File f) throws IOException{
        HeaderInfo info = new HeaderInfo();
        HashMap<String,Integer> map = new HashMap<>();
        char [] headerInfo = new char[HEADER_MAX_SIZE];
        BufferedReader r = FileHandler.readFile(f.getAbsolutePath());
        r.read(headerInfo);
        //String s = headerInfo.toString();
        String s = new String(headerInfo);
        
        int nFiles = s.charAt(1);  //get the number of files
        while(nFiles>0){
            nFiles--;
        }
        return info;
    }*/
    
    public static byte[] getCodedContentFromMerged(InputStream in, int fileSz) throws IOException{
        byte [] codedInfo = new byte[fileSz];
        in.read(codedInfo);
        return codedInfo;
    }
    /*
    private static int fileCycling(){
        
    }
    
    public static List<Byte[]> getCodedContentFromMergedAsList(InputStream in){
        
    }
    */
}
