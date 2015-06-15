/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.api;

import java.io.File;



/**
 *
 * @author João
 */
public interface Technique {
    public File compress(String path,String extension);
    public File decompress(File f,int toSkip);
    String getName();
}
