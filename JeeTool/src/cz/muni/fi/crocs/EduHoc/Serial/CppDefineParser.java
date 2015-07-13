/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.Serial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class CppDefineParser {

    private String filepath;
    private File file;

    private boolean silent = false;
    private boolean verbose = false;
    
    public void setVerbose(){
        verbose = true;
    }
    
    public void setSilent(){
        silent = true;
    }
    
    public CppDefineParser(String filepath) throws IOException {
        this.filepath = filepath;
        this.file = new File(filepath);
        if (!file.exists()) {
            System.err.println("file not found" + filepath);
            throw new IOException("file not found" + filepath);
        }

    }

    public String findDefine(String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine();
        while (line != null) {
            if (line.contains("#define") && line.contains(name)) {
                return line.substring(line.indexOf(name) + name.length()).trim();
            }
            line = br.readLine();
        }
        return "";
    }
    
}
