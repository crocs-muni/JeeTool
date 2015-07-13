/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import cz.muni.fi.crocs.EduHoc.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class FileWriter {

    private String filename;
    private File file;
    private BufferedWriter bw;

    private boolean silent = false;
    private boolean verbose = false;
    
    public void setVerbose(){
        verbose = true;
    }
    
    public void setSilent(){
        silent = true;
    }
    
    public FileWriter() throws IOException {
        filename = "tmpScript_" + Thread.currentThread().getName().toString() + ".sh";
        if(verbose){
            System.out.println("Tmp script created at: " + filename + ", will be deleted on exit.");
        }
        file = new File(filename);
        file.deleteOnExit();
        bw = new BufferedWriter(new java.io.FileWriter(file));
    }

    public void appendToFile(String text) throws IOException{
        bw.write(text);
        bw.newLine();
        if(verbose){
            System.out.println("Text added to script: " + Main.ANSI_CYAN + text + Main.ANSI_RESET);
        }
    }
    
    public void close() throws IOException {
        bw.close();
    }

    public String getFilename() {
        return filename;
    }

    
    
}
