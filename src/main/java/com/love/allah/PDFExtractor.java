package com.love.allah;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFExtractor {
     private static String dirInput = "/Users/<USER>/Downloads/feed_pdf/";
    private static String dirOutput = "/Users/<USER>/Downloads/output_files/";
    private static String extInput = ".pdf";
    private static String extOutput = ".txt";
    public static void main(String[] args){

        try (Stream<Path> stream = Files.walk(Paths.get(dirInput))) {
            Set<Path> files = stream.filter(Files::isRegularFile)
                    .collect(Collectors.toSet());
            System.out.println(files.toArray());
            files.stream()
                    .forEach(item->{
                        if(item.getFileName()
                                .toString().endsWith(extInput)) {
                            PDFExtractor.processFile(String.valueOf(item.getFileName())
                                    .replace(dirInput,"")
                                    .replace(extInput,"")
                            );
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * PROCESS THE FILE , EXTRACT TEXT FROM THE PDF AND SAVE INTO A TXT FILE
     * @param filename
     */
    public static void processFile(String filename){
        PDDocument pd;
        BufferedWriter wr;
        try {
            File input = new File(dirInput+filename+extInput);  // The PDF file from where you would like to extract
            File output = new File(dirOutput+filename+extOutput); // The text file where you are going to store the extracted data
            pd = Loader.loadPDF(input);
            System.out.println(pd.getNumberOfPages());
            System.out.println(pd.isEncrypted());
            pd.save(dirOutput+"_backup_"+filename+extInput); // Creates a copy called "CopyOfInvoice.pdf"
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1); //Start extracting from page 3
            stripper.setEndPage(1); //Extract till page 5
            wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
            stripper.writeText(pd, wr);
            if (pd != null) {
                pd.close();
            }
            // I use close() to flush the stream.
            wr.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}