/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.calculus;

import isel.pfc.filesHandler.FileHandler;
import static isel.pfc.filesHandler.FileHandler.readFileToByteArray;
import isel.pfc.filesHandler.LoadFiles;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author João
 */
public class SymbolFrequency {
    public final int DEFAULT_FREQUENCY = 20;
    File file;
    int occur = DEFAULT_FREQUENCY;
    //private HashMap<String,Integer> userCustom;
    String [] userCustoms;
    Stream<String> customs;
    public SymbolFrequency(File f){
        file = f;
    }
    
    public SymbolFrequency(File f, int occur){
        file = f;
        this.occur=occur;
    }
    
    public SymbolFrequency(String path, String ext) throws IOException{
        List<File> files = (List<File>) FileHandler.listFilesAtLocation(path, (f)->{if(f.getName().endsWith(ext))return true;return false;});
        file = FileHandler.mergeFiles(files);
        
    }
    
    /*
    private HashMap<Integer,Integer> getFreqSymsWithNSz(Byte [] s, int nChars){
        
        HashMap<Integer,Integer> freqs = new HashMap<>();
        Byte [] slide = new Byte[nChars];
        for(int i = 0;i<s.length;++i){
            
        }
        return null;
    }*/
    
    private HashMap<String,Integer> getFreqSymsWithNSz(String s, int nChars){
        
        HashMap<String,Integer> freqs = new HashMap<>();
        String subStr = "";
        for(int l = 0, r=nChars;r<s.length();++l,++r){
            subStr = s.substring(l, r);
            if(freqs.containsKey(subStr)){
                freqs.replace(subStr, freqs.get(subStr)+1);
            }else{
                freqs.put(subStr, 1);
            }
        }
        return freqs;
    }
    
    private HashMap<String,Integer> filterOccurs(HashMap<String,Integer> map){
        Iterator<Map.Entry<String,Integer>> iter = map.entrySet().iterator();
        iter.forEachRemaining( e -> {if(e.getValue()<occur)iter.remove();});
        return map;
    }
    
    
    //CHANGE THIS UGLY CODE!!!
    public List<HashMap<String,Integer>> findMostFrequentSymbols() throws IOException{
        String fileContent = FileHandler.readFileToString(file.getAbsolutePath());
        List<HashMap<String,Integer>> list = new ArrayList<>();
        HashMap<String,Integer> freq2Symbs = getFreqSymsWithNSz(fileContent, 2);
        filterOccurs(freq2Symbs);
        list.add(freq2Symbs);
        HashMap<String,Integer> freq3Symbs = getFreqSymsWithNSz(fileContent, 3);
        filterOccurs(freq3Symbs);
        list.add(freq3Symbs);
        HashMap<String,Integer> freq4Symbs = getFreqSymsWithNSz(fileContent, 4);
        filterOccurs(freq4Symbs);
        list.add(freq4Symbs);
        return list;
    }
    /**
     * 
     * @param words String with words seperated by a space 
     */
    public void addCustomWords(String words){
        userCustoms = words.split("\\s+");
        customs = Arrays.stream(userCustoms);
    }
    
    //depending on the length we could parallelize
    //Files or Paths streams to do this.
    public void symbolUniverseInFile(String path) throws IOException{
        byte [] symbs = readFileToByteArray(path);
        //Arrays.str
        HashSet<Character> chars = new HashSet();
        for(int i = 0; i<symbs.length;++i){
            chars.add((char)symbs[i]);
        }
    }
    
    private void sortedFrequences(HashMap<String,Integer> map){
        List<Integer> freqs = map.entrySet().stream().map(E -> E.getValue()).sorted().collect(Collectors.toList());
        //Collection<Integer> col = map.values();
        //Collections.so
        //map.entrySet().stream().
        //LinkedList<Entry<String,Integer>> x = map.entrySet().stream()///sorted(comparing(Entry::getValue));//collect(Collectors.toList()).sort(Entry.comparingByKey());
        //map.entrySet().stream().sorted(Entry.comparingByValue()).red//.collect(Collectors.toList());
    //();
                //.collect(Collectors.toList());//.sort(Entry.comparingByKey()).;//.sort(Entry<String,Integer>::);//.sort();
        //Entry<String,Integer>.comparingByKey()::
    }
    
    /* Module specialization
    public void buildFile(String fileName){
        
        //FileWriter writer = new FileWriter(new File("")); 
        
        //customs.collect(new LinkedList<String>(),
    }
    */
}
