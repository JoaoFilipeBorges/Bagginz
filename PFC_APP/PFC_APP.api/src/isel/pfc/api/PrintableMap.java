/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.api;

import java.util.Map;

/**
 *
 * @author João
 */
public class PrintableMap<K,V> {
    public Map<K,V> map;
    public PrintableMap(Map<K,V> m){
        map = m;
    }
    
    @Override
    public String toString(){
        return map.entrySet().stream().map(E -> E.getKey()+"   "+E.getValue()).reduce("", (a,b) -> a.concat(System.lineSeparator()).concat(b));
    }
}
