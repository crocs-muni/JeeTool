/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LukeMcNemee
 */
public class MoteList {

    private String filepath;
    private List<String> motes;
    private boolean silent = false;
    private boolean verbose = false;
    
    public void setVerbose(){
        verbose = true;
    }
    
    public void setSilent(){
        silent = true;
    }
    
    public MoteList(String filepath) {
        this.filepath = filepath;
        motes = new ArrayList<String>();
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null) {
                
                if(line.charAt(0)!='#'){
                    
                    
                    File mote = new File(line);
                    
                    if(mote.exists()){
                        motes.add(line);
                        
                    } else {
                        //System.err.println("mote " + line + " not found");
                    }
                }
                line = reader.readLine();
            }
            System.out.println(motes.size() + " motes available");

        } catch (FileNotFoundException ex) {
            System.err.println("file not found");
        } catch (IOException ex) {
            System.err.println("read line failed");
        }
    }

    public List<String> getMotes() {
        return motes;
    }
}
