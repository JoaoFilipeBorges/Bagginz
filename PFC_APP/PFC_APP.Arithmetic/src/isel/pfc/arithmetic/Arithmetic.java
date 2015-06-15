/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isel.pfc.arithmetic;

import isel.pfc.api.AbstractTechnique;
import isel.pfc.api.Technique;
import static isel.pfc.arithmetic.ArithmeticDecompress.decompress;
import static isel.pfc.arithmetic.ArithmeticDecompress.readFrequencies;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João
 */
public class Arithmetic implements Technique{
    
    private final String name = "arithmetic";
    
    @Override
    public File compress(String path,String ext) {      //FALTA HEADER
        try{
            // Read input file once to compute symbol frequencies
            File input = new File(path);
            FrequencyTable freq = getFrequencies(input);
            File outputFile = new File(path+System.currentTimeMillis()+".bgz");     //only if more than one, else get the name.bgz
            freq.increment(256);  // EOF symbol gets a frequency of 1
            try(InputStream in = new BufferedInputStream(new FileInputStream(input));
                    BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))){//Changed BitOutputStream to implement AutoCloseable               
                writeFrequencies(out, freq);
                compress(freq, in, out);            
            }
            return outputFile;
        }catch(Exception e){
            
        }
        return null; 
    }

    @Override
    public File decompress(File f, int toSkip) {
        try{
            String path = f.getParent()+"\\foo.temp";
            File outputFile = new File(path);
            try(BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(f)));
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));){
                FrequencyTable freq = readFrequencies(in);
		decompress(freq, in, out);
            }
            return outputFile;
        }catch(Exception e){
            
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public static FrequencyTable getFrequencies(File file) throws IOException {
        FrequencyTable freq = new SimpleFrequencyTable(new int[257]);
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
	
	
    private void writeFrequencies(BitOutputStream out, FrequencyTable freq) throws IOException {
        for (int i = 0; i < 256; i++)
            writeInt(out, 32, freq.get(i));
    }


    private void compress(FrequencyTable freq, InputStream in, BitOutputStream out) throws IOException {
        ArithmeticEncoder enc = new ArithmeticEncoder(out);
        while (true) {
            int b = in.read();
            if (b == -1)
                    break;
            enc.write(freq, b);
        }
        enc.write(freq, 256);  // EOF
        enc.finish();
    }

	
    private void writeInt(BitOutputStream out, int numBits, int value) throws IOException {
            if (numBits < 0 || numBits > 32)
                    throw new IllegalArgumentException();

            for (int i = 0; i < numBits; i++)
                    out.write(value >>> i & 1);  // Little endian
    }
    
    private FrequencyTable readFrequencies(BitInputStream in) throws IOException {
		int[] freqs = new int[257];
		for (int i = 0; i < 256; i++)
			freqs[i] = readInt(in, 32);
		freqs[256] = 1;  // EOF symbol
		return new SimpleFrequencyTable(freqs);
	}
	
	
    private void decompress(FrequencyTable freq, BitInputStream in, OutputStream out) throws IOException {
            ArithmeticDecoder dec = new ArithmeticDecoder(in);
            while (true) {
                    int symbol = dec.read(freq);
                    if (symbol == 256)  // EOF symbol
                            break;
                    out.write(symbol);
            }
    }


    private int readInt(BitInputStream in, int numBits) throws IOException {
            if (numBits < 0 || numBits > 32)
                    throw new IllegalArgumentException();

            int result = 0;
            for (int i = 0; i < numBits; i++)
                    result |= in.readNoEof() << i;  // Little endian
            return result;
    }
    
}
