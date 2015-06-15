/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.api;

import java.io.File;
import java.util.List;

/**
 *
 * @author João
 */
public interface Specialization {
    
    String getName();
    //File fetch();
    List<String> getFormatsApplied();
    String getFormat();
    //Technique getTechnique();
    File executeTechnique(String path);
    //boolean exists(String format);
}
