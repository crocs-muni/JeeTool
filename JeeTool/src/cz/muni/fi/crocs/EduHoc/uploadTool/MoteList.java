/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import cz.muni.fi.crocs.EduHoc.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LukeMcNemee
 */
public class MoteList {

    private String filepath;
    private Map<String, String> motes;
    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    public MoteList(String filepath) {
        this.filepath = filepath;
        motes = new HashMap<String, String>();

    }

    /**
     * read file line by line and test, if file exists. All existing files are
     * saved as found motes, lines begining with # are treated as comments
     */
    public void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null) {

                if (line.charAt(0) != '#') {

                    File mote = new File(line);
                    if (mote.exists()) {
                        if (!silent) {
                            System.out.println(Main.ANSI_GREEN + "mote " + line + " found" + Main.ANSI_RESET);
                        }
                        motes.put(line, mote.getCanonicalPath());

                    } else {
                        if (verbose) {
                            System.out.println(Main.ANSI_RED + "mote " + line + " not found" + Main.ANSI_RESET);
                        }
                    }
                }
                line = reader.readLine();
            }

            if (motes.size() == 1) {
                System.out.println(motes.size() + " mote available");
            } else {
                System.out.println(motes.size() + " motes available");
            }

        } catch (FileNotFoundException ex) {
            System.err.println("file not found");
        } catch (IOException ex) {
            System.err.println("read line failed");
        }
    }

    public Map<String, String> getMotes() {
        return motes;
    }
}
