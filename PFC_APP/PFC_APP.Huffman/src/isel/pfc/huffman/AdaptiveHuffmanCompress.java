package isel.pfc.huffman;

import isel.pfc.api.Technique;
import static isel.pfc.filesHandler.FileHandler.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import isel.pfc.huffman.utils.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;

//@ServiceProvider(service = Technique.class)
public final class AdaptiveHuffmanCompress implements Technique{
	
	public static void main(String[] args) throws IOException {
		// Show what command line arguments to use
		if (args.length == 0) {
			System.err.println("Usage: java AdaptiveHuffmanCompress InputFile OutputFile");
			System.exit(1);
			return;
		}
		
		// Otherwise, compress
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		
		InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
		BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
		try {
			compress(in, out);
		} finally {
			out.close();
			in.close();
		}
	}
	
	
	public static void compress(InputStream in, BitOutputStream out) throws IOException {
		int[] initFreqs = new int[257];
		Arrays.fill(initFreqs, 1);
		
		FrequencyTable freqTable = new FrequencyTable(initFreqs);
		HuffmanEncoder enc = new HuffmanEncoder(out);
		enc.codeTree = freqTable.buildCodeTree();  // We don't need to make a canonical code since we don't transmit the code tree
		int count = 0;
		while (true) {
			int b = in.read();
			if (b == -1)
				break;
			enc.write(b);
			
			freqTable.increment(b);
			count++;
			if (count < 262144 && isPowerOf2(count) || count % 262144 == 0)  // Update code tree
				enc.codeTree = freqTable.buildCodeTree();
			if (count % 262144 == 0)  // Reset frequency table
				freqTable = new FrequencyTable(initFreqs);
		}
		enc.write(256);  // EOF
	}
	
	
	private static boolean isPowerOf2(int x) {
		return x > 0 && (x & -x) == x;
	}

    @Override
    public File compress(String path, String exe) {
        
        
            File outputFile = null;
            try {
                outputFile = new File(path+"\\xpto.bgz");
                List<File> files = (List<File>) listFilesAtLocation(path,(f)->{if(f.getName().endsWith(".java"))return true;return false;});
                File inputFile = mergeFiles(files, path,getName());
                
                InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
                BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
                
                compress(in, out);
                out.close();
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(AdaptiveHuffmanCompress.class.getName()).log(Level.SEVERE, null, ex);
            }
            return outputFile;
    }

    @Override
    public String getName() {
        return "AHuffman";
    }

    @Override
    public File decompress(File f, int toSkip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


	
}
