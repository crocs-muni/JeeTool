/*
* The MIT License (MIT)
*
* Copyright (c) 2015 CRoCS
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package cz.muni.fi.crocs.JeeTool.upload;

import cz.muni.fi.crocs.JeeTool.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

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
                        if (ids != null) {
                            int id = Integer.parseInt(line.replaceAll("[\\D]", ""));
                            if (ids.contains(id)) {
                                if (!silent) {
                                    System.out.println(Main.ANSI_GREEN + "mote " + line + " found" + Main.ANSI_RESET);
                                }
                                motes.put(line, mote.getCanonicalPath());
                            }
                        } else {

                            if (!silent) {
                                System.out.println(Main.ANSI_GREEN + "mote " + line + " found" + Main.ANSI_RESET);
                            }
                            motes.put(line, mote.getCanonicalPath());
                        }
                    } else {
                        if (ids != null) {
                            int id = Integer.parseInt(line.replaceAll("[\\D]", ""));
                            if (ids.contains(id)) {
                                if (verbose) {
                                    System.out.println(Main.ANSI_RED + "mote " + line + " not found" + Main.ANSI_RESET);
                                }
                            }
                        } else {
                            if (verbose) {
                                System.out.println(Main.ANSI_RED + "mote " + line + " not found" + Main.ANSI_RESET);
                            }
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
