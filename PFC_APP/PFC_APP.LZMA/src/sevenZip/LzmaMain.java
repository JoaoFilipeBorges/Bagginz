/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sevenZip;
//
import isel.pfc.api.Technique;
import java.io.File;
import java.util.function.Supplier;
import LZMA.Encoder;
import LZMA.Decoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
/**
 *
 * @author João
 */
//@ServiceProvider(service = Technique.class)
public class LzmaMain implements Technique{
    
    private int DEFAULT_COMPRESSION_MODE = 1;
    private int DEFAULT_DICTIONARY_SIZE = 23; //8MB
    private int DEFAULT_FAST_BYTES = 128;
    private int ALGORITHM = 2;
    private int MATCH_FINDER = 1;
    File input;
    int dictionarySize = DEFAULT_DICTIONARY_SIZE;
    int fastBytes = DEFAULT_FAST_BYTES;
    int compressionMode;
    HashMap<String,Integer> dictMap;// = new HashMap<>();
    HashMap<String,Integer> dictMode;
    {
        dictMap = new HashMap<>();
        dictMap.put("4MB", 22);
        dictMap.put("8MB", 23);
        dictMap.put("32MB", 25);
        dictMap.put("1GB", 30);
        dictMode = new HashMap<>();
        dictMode.put("normal",1);
        dictMode.put("fast",0);
    }
    /*
    public LzmaMain(File file){
        
    }
    
    public LzmaMain(File file, int compMode, int dictSz){
        input = file;
        dictionarySize = dictSz;
    }
    */
    
    public LzmaMain(){}
    
    public LzmaMain(File file, String compMode, String dictSz){
        input = file;
        dictionarySize = dictMap.get(dictSz);
        compressionMode = dictMode.get(compMode);
    }
    
    //@Override
    public File compress() {
        File outFile = null;
        try {          
            outFile = new File(input.getParent()+"\\lzma.bgz");
            try(BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(input));
                    BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outFile));){
                Encoder enc = new Encoder();
                enc.SetAlgorithm(ALGORITHM);
                enc.SetDictionarySize(dictionarySize);
                enc.SetNumFastBytes(fastBytes);
                enc.SetMatchFinder(MATCH_FINDER);
                enc.SetLcLpPb(3, 0, 2);
                long fileSize = input.length();
                enc.Code(inStream, outStream, -1, -1, null);
                inStream.close();
                outStream.close();               
            }
        } catch (Exception ex) {
            Logger.getLogger(LzmaMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outFile;
    }
    
    
    //@Override
    public File decompress(File f) {
        try{
            BufferedInputStream inStream  = new java.io.BufferedInputStream(new java.io.FileInputStream(f));
            BufferedOutputStream outStream;// = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));
            int propertiesSize = 5;
            byte[] properties = new byte[propertiesSize];
            if (inStream.read(properties, 0, propertiesSize) != propertiesSize)
                    throw new Exception("input .lzma file is too short");
            Decoder decoder = new Decoder();
            if (!decoder.SetDecoderProperties(properties))
                                            throw new Exception("Incorrect stream properties");
            long outSize = 0;
            for (int i = 0; i < 8; i++){
                int v = inStream.read();
                if (v < 0)
                    throw new Exception("Can't read stream size");
                outSize |= ((long)v) << (8 * i);
            }
            //if (!decoder.Code(inStream, outStream, outSize))
                    //throw new Exception("Error in data stream");
            //outStream.flush();
            //outStream.close();
            inStream.close();
        }catch(Exception e){
            
        }
        
        return null;
    }
    

    @Override
    public File compress(String path,String ext) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public String getName() {
        return "lzma";
    }

    

    @Override
    public File decompress(File f, int toSkip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    
    
}
