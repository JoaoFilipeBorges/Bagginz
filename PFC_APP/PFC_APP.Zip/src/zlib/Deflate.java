/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zlib;
import com.google.common.io.Files;
import isel.pfc.api.Technique;
import isel.pfc.filesHandler.FileHandler;
import static isel.pfc.filesHandler.FileHandler.listFilesAtLocation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author João
 */
@ServiceProvider(service = Technique.class)
public class Deflate implements Technique{
    
    private final String name = "deflate";
    public Deflate(){}
    

    @Override
    public File compress(String path,String ext) {
        File output = null;
        try {
            List<File> files = (List<File>) listFilesAtLocation(path,(f)->{if(f.getName().endsWith(ext))return true;return false;});
            output = new File(path+"\\deflateXpto.bgz");
            File input = FileHandler.mergeFiles(files, path, this.name);
            byte [] buffer = Files.toByteArray(input);
            byte [] buffOut = new byte [buffer.length];
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(buffer);
            //FileOutputStream outputStream = new FileOutputStream(buffer);
            deflater.finish();
            int total = 0;
            while(!deflater.finished()){
                int p = deflater.deflate(buffOut);
                total += p;
            }
            byte [] aux = new byte [total];
            System.arraycopy(buffOut, 0, aux, 0, total);
            Files.write(aux, output);
            //Files.w
        } catch (IOException ex) {
            Logger.getLogger(Deflate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }


    @Override
    public File decompress(File f,int toSkip) {
        File file = null;
        try {
            byte [] data = Files.toByteArray(f);
            Inflater inflater = new Inflater();
            inflater.setInput(data);
        } catch (IOException ex) {
            Logger.getLogger(Deflate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }

    @Override
    public String getName() {
        return name;
    }


    
}
