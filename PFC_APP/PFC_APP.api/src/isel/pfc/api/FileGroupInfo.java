/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author João
 */
public final class FileGroupInfo {
    public final String extension;
    private List<File> filesInGroup;
    private int files = 0; 
    public FileGroupInfo(String s){
        extension=s;
        filesInGroup = new ArrayList<>();
    }
    
    public void addFileToGroup(File f){
        filesInGroup.add(f);
        files++;
    }
    
    public List<File> getFiles(){
        return filesInGroup;
    }
}
