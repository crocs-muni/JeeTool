/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LukeMcNemee
 */
public class MoteList {

    private String filepath;
    private List<String> motes;

    public MoteList(String filepath) {
        this.filepath = filepath;
        motes = new ArrayList<String>();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null) {
                motes.add(line);
                line = reader.readLine();
            }

        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            Logger.getLogger(MoteList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getMotes() {
        return motes;
    }
}
