/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.encoding.utils;

import java.util.HashMap;

/**
 *
 * @author João
 */
public final class HeaderUtils {
    
    public static final class HeaderInfo{
        int numFiles;
        int headerEnd;
        HashMap<String, Integer> mergedFiles = new HashMap<>();
        
    }
    
    public static final int HEADER_MAX_SIZE = 1024;
    
    /*
    private static HashMap<String,Class> decMap = new HashMap<>();
    static{
        decMap.put("x", LZW.class);//lzw    what about various dict sizes ???
        decMap.put("p", null);//lz77
        decMap.put("t", null);//huffman
        decMap.put("o", null);//ahuffman
    }
    */
}
