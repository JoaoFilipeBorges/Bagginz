/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.analyzer;

//import isel.pfc.api.FileGroupInfo;
import java.util.HashMap;

/**
 *
 * @author João
 */
public class SpecializationBuilder {
    
    //Move to calculus module(??)
    private static HashMap<String,Integer> findMostCommonSymbolsWithNChars(String s, int nChars){
        HashMap<String,Integer> symsOcurr = new HashMap<>();
        
        for(int i = 0;i<s.length();++i){
            
        }
        return symsOcurr;
    }
    
    public static void build(FileGroupInfo group){
        
        //Merge files here
        
        HashMap<String,Integer> mostFreq2CharSymb = findMostCommonSymbolsWithNChars(null, 2);
        
    }
    
}
