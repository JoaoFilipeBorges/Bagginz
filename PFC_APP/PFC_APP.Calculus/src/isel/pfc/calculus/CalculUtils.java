/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.calculus;

import java.io.File;
import java.util.HashMap;

/**
 *
 * @author João
 */
public class CalculUtils {
    public static double calculateCompressionRate(File in, File out){
        return (out.length()/in.length())*100;
    }
    
    private static HashMap<Character,Integer> symbolsInFile(String str){
        HashMap<Character,Integer> probs = new HashMap<>();
        for(char c : str.toCharArray()){
            if (!probs.containsKey(c)) {
                probs.put(c, 0);
            }
            probs.put(c, probs.get(c) + 1);
        }
        return probs;
    }
    
    public static double calculateEntropy(String str){
        HashMap<Character,Integer> probs = symbolsInFile(str);
        
        double result = 0.0;
        for(Character c : probs.keySet()){
            Double frequency = (double) probs.get(c) / str.length();
            result -= frequency * (Math.log(frequency) / Math.log(2));
        }
 
        return result;
    }
}
