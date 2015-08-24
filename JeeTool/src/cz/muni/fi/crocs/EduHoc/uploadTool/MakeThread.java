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

package cz.muni.fi.crocs.EduHoc.uploadTool;

import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class MakeThread implements Runnable {

    private String projectPath;
    private int mode = 0;
    private String mote;

    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    public MakeThread(String projectPath, String mote) {
        this.projectPath = projectPath;
        this.mote = mote;
    }

    public MakeThread(String projectPath) {
        this.projectPath = projectPath;
        this.mote = "";
    }

    /**
     * set mode of make
     *
     * @param mode mode 0 for make mode 1 for make upload mode 2 for make clean
     * default mode is 0
     */
    public void select(int mode) {
        this.mode = mode;
    }

    @Override
    public void run() {
        try {
            ExecuteShellCommand com = new ExecuteShellCommand();
            FileWriter writer = new FileWriter();
            if (silent) {
                com.setSilent();
                writer.setSilent();
            }
            if (verbose) {
                com.setVerbose();
                writer.setVerbose();
            }

            writer.appendToFile("#./bin/bash");
            writer.appendToFile("cd " + projectPath);
            switch (mode) {
                case 0:
                    writer.appendToFile("make");
                    if (verbose) {
                        System.out.println("Calling make on target " + projectPath);
                    }
                    break;
                case 1:
                    writer.appendToFile("make upload MONITOR_PORT=" + mote);
                    if (verbose) {
                        System.out.println("Calling make upload on target " + projectPath);
                    }
                    break;
                case 2:
                    writer.appendToFile("make clean");
                    if (verbose) {
                        System.out.println("Calling make clean on target " + projectPath);
                    }
                    break;
                default:
                    writer.appendToFile("make");
                    if (verbose) {
                        System.out.println("Calling make on target " + projectPath);
                    }
                    break;
            }

            //writer.appendToFile("ls");
            writer.close();
            com.executeCommand("chmod a+x " + writer.getFilename());
            com.executeCommand("./" + writer.getFilename());
            if (verbose) {
                com.executeCommand("echo \"shell finished\"");
            }
        } catch (IOException ex) {
            if (!silent) {
                System.err.println("Make thread failed" + ex.toString());
            }
        }
    }

}
