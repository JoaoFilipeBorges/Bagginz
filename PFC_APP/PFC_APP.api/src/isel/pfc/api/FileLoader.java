/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.api;

/**
 *
 * @author João
 */
public interface FileLoader<File> {
    public Iterable<File> process(String path);
}
