/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.specialization;

import isel.pfc.filesHandler.FileHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author João
 */
public class Creator {
    
    private final String dir = "D:\\OneDrive\\Projecto final de curso 1415I\\Compressao Especializada\\CompressionApp\\configFiles";//System.getProperty("user.dir")+"\\src\\data";
    String genConfig = "D:\\OneDrive\\Projecto final de curso 1415I\\Compressao Especializada\\CompressionApp\\configFiles\\genConfig.txt";//System.getProperty("user.dir")+"\\src\\data\\config.txt";
    public Creator(){}
    
    private String buildHeader(){return "";}
    
    private char genKey(){
        List<String> currKeys = FileHandler.getKeys();
        for(;;){
            String rngSymb = "";
            if(!currKeys.contains(rngSymb)){
                return rngSymb.charAt(0);           //<-------- change!!!!!
            }
        }
    }
    
    private void addToGeneralConfig(String spName, String ext, String desc) throws IOException{
        File config = new File(genConfig); //<---- General config
        try(FileWriter writer = new FileWriter(config, true)){
            writer.append(System.lineSeparator());
            writer.append(spName+","+ext+","+desc);
        }
    }
    
    public void publish(String spName, String techn, String ext, String desc, List<String> words){
        File newSpecial = new File(dir+"\\"+spName+".csv");
        try{
            addToGeneralConfig(spName,ext,desc);
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newSpecial))){
                String header = spName+","+techn;//buildHeader();
                bufferedWriter.write(header, 0, header.length());
                bufferedWriter.newLine();
                for(String w : words){
                    bufferedWriter.write(w, 0, w.length());
                    bufferedWriter.newLine();
                }
            }
        }catch(IOException ex){
            
        }
        
    }
    
}
