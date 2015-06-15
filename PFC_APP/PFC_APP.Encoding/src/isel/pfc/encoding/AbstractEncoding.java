/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.encoding;

import isel.pfc.api.Technique;
import java.util.Collection;
import java.util.HashMap;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.util.lookup.*;
/**
 *
 * @author João
 */
public class AbstractEncoding extends TopComponent{
    private InstanceContent content;
    public final int HEADER_MAX_SIZE = 1024;
    public final HashMap<String,Technique> encMap = new HashMap<>();
    
    public AbstractEncoding(){
        content = new InstanceContent();
        associateLookup(new AbstractLookup(content));
        Collection<? extends Technique> techniques = Lookup.getDefault().lookupAll(Technique.class);
        for(Technique t : techniques){
            encMap.put(t.getName(), t);
        }
    }
}
