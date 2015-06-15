package isel.pfc.huffman;

import isel.pfc.api.Technique;
import static isel.pfc.filesHandler.FileHandler.listFilesAtLocation;
import static isel.pfc.filesHandler.FileHandler.mergeFiles;
import static isel.pfc.huffman.HuffmanDecompress.decompress;
import static isel.pfc.huffman.HuffmanDecompress.readCode;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import isel.pfc.huffman.utils.*;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
// Uses static Huffman coding to compress an input file to an output file. Use HuffmanDecompress to decompress.
// Uses 257 symbols - 256 for byte values and 1 for EOF. The compressed file format contains the code length of each symbol under a canonical code, followed by the Huffman-coded data.
@ServiceProvider(service = Technique.class)
public final class Huffman_Main implements Technique{
	
	public static FrequencyTable getFrequencies(File file) throws IOException {
		FrequencyTable freq = new FrequencyTable(new int[257]);
		InputStream input = new BufferedInputStream(new FileInputStream(file));
		try {
			while (true) {
				int b = input.read();
				if (b == -1)
					break;
				freq.increment(b);
			}
		} finally {
			input.close();
		}
		return freq;
	}
	
	
	public static void writeCode(BitOutputStream out, CanonicalCode canonCode) throws IOException {
		for (int i = 0; i < canonCode.getSymbolLimit(); i++) {
			int val = canonCode.getCodeLength(i);
			// For this file format, we only support codes up to 255 bits long
			if (val >= 256)
				throw new RuntimeException("The code for a symbol is too long");
			
			// Write value as 8 bits in big endian
			for (int j = 7; j >= 0; j--)
				out.write((val >>> j) & 1);
		}
	}
	
	
	public static void compress(CodeTree code, InputStream in, BitOutputStream out) throws IOException {
		HuffmanEncoder enc = new HuffmanEncoder(out);
		enc.codeTree = code;
		while (true) {
			int b = in.read();
			if (b == -1)
				break;
			enc.write(b);
		}
		enc.write(256);  // EOF
	}

    @Override
    public File compress(String path,String ext) {
        File outputFile = null;
        try {
            //File inputFile = new File(path);
            //String inName = inputFile.getName();
            //String ext = inName.substring(inName.lastIndexOf("."));
            //String newName = inName.replace(ext, ".bgz");
            outputFile = new File(path+"HuffComp.bgz");
            List<File> files = (List<File>) listFilesAtLocation(path,(f)->{if(f.getName().endsWith(ext))return true;return false;});
            File inputFile = mergeFiles(files, path,getName());
            // Read input file once to compute symbol frequencies
            // The resulting generated code is optimal for static Huffman coding and also canonical
            FrequencyTable freq = getFrequencies(inputFile);
            freq.increment(256);  // EOF symbol gets a frequency of 1
            CodeTree code = freq.buildCodeTree();
            CanonicalCode canonCode = new CanonicalCode(code, 257);
            code = canonCode.toCodeTree();  // Replace code tree with canonical one. For each symbol, the code value may change but the code length stays the same.

            // Read input file again, compress with Huffman coding, and write output file
            InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
            BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
            writeCode(out, canonCode);
            compress(code, in, out);
            out.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Huffman_Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputFile;
    }

    @Override
    public String getName() {
        return "huffman";
    }

   
    @Override
    public File decompress(File f, int toSkip) {
        File decompressed = null;
        try{
            String path = f.getParent()+"\\foo.temp";
            decompressed = new File(path);
            try(BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(f)));
                    FileOutputStream out = new FileOutputStream(decompressed)){
                CanonicalCode canonCode = readCode(in);
		CodeTree code = canonCode.toCodeTree();
		decompress(code, in, out);
            }
        }catch(IOException e){
            
        }
        return decompressed;
    }
    
    private CanonicalCode readCode(BitInputStream in) throws IOException {
        int[] codeLengths = new int[257];
        for (int i = 0; i < codeLengths.length; i++) {
                // For this file format, we read 8 bits in big endian
                int val = 0;
                for (int j = 0; j < 8; j++) 
                        val = val << 1 | in.readNoEof();
                codeLengths[i] = val;
        }
        return new CanonicalCode(codeLengths);
    }


    private void decompress(CodeTree code, BitInputStream in, OutputStream out) throws IOException {
        HuffmanDecoder dec = new HuffmanDecoder(in);
        dec.codeTree = code;
        while (true) {
                int symbol = dec.read();
                if (symbol == 256)  // EOF symbol
                        break;
                out.write(symbol);
        }
    }
	
}
