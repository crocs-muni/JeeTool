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
package cz.muni.fi.crocs.JeeTool;

import cz.muni.fi.crocs.JeeTool.upload.MoteList;
import cz.muni.fi.crocs.EduHoc.uploadTool.MakeThread;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author LukeMcNemee
 */
public class UploadMain {

    private MoteList motelist;
    private File makefile;
    private String projectPath;
    private CommandLine cmd;

    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    //interface version
    public UploadMain(MoteList motelist, String projectPath) {
        this.motelist = motelist;
        this.projectPath = projectPath;

        if (projectPath.charAt(projectPath.length() - 1) == '/') {
            makefile = new File(projectPath + "Makefile");
        } else {
            makefile = new File(projectPath + "/Makefile");
        }

        if (!makefile.exists()) {
            System.err.println("makefile not found");
            return;
        }
    }

    //cmd version
    public UploadMain(MoteList motelist, CommandLine cmd) {
        this.motelist = motelist;
        this.cmd = cmd;

        if (cmd.hasOption("m")) {
            //System.out.println("running make");
            projectPath = cmd.getOptionValue("m");
        }
        if (cmd.hasOption("c")) {
            //System.out.println("running make clean");
            projectPath = cmd.getOptionValue("c");
        }
        if (cmd.hasOption("u")) {
            //System.out.println("running make upload");
            projectPath = cmd.getOptionValue("u");
        }

        if (projectPath.charAt(projectPath.length() - 1) == '/') {
            makefile = new File(projectPath + "Makefile");
        } else {
            makefile = new File(projectPath + "/Makefile");
        }
        if (!silent) {
            System.out.println("processing makefile for project at " + projectPath);
        }

        if (!makefile.exists()) {
            System.err.println("makefile not found");
            return;
        }
        if (!silent) {
            System.out.println(Main.ANSI_GREEN + "Makefile " + makefile.getPath() + " found" + Main.ANSI_RESET);
        }
    }

    //interface version
    public void make() {
        MakeThread t1 = new MakeThread(projectPath);
        t1.select(0);
        t1.run();
    }

    //interface version
    public void makeClean() {
        MakeThread t1 = new MakeThread(projectPath);
        t1.select(2);
        t1.run();
    }

    //interface version
    public void makeUpload() {
        List<Thread> threads = new ArrayList<Thread>();
        make();

        for (String motePath : motelist.getMotes().keySet()) {
            MakeThread t1 = new MakeThread(projectPath, motePath);
            if (silent) {
                t1.setSilent();
            }
            if (verbose) {
                t1.setVerbose();
            }
            t1.select(1);
            //if specified, create new thread for every mote
            Thread t = new Thread(t1);
            t.start();
            threads.add(t);
        }
        for (Thread t1 : threads) {
            try {
                //System.out.println("waiting for thread "+ t1.toString());
                t1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(UploadMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //cmd version
    public void runMake() {
        List<Thread> threads = new ArrayList<Thread>();
        //System.out.println("run Make initiated");
        if (cmd.hasOption("m")) {
            //System.out.println("running make");
            MakeThread t1 = new MakeThread(projectPath);
            if (silent) {
                t1.setSilent();
            }
            if (verbose) {
                t1.setVerbose();
            }
            t1.select(0);
            t1.run();

        } else if (cmd.hasOption("c")) {
            //System.out.println("running make clean"); 
            MakeThread t1 = new MakeThread(projectPath);
            if (silent) {
                t1.setSilent();
            }
            if (verbose) {
                t1.setVerbose();
            }
            t1.select(2);
            t1.run();

        } else if (cmd.hasOption("u") && (!cmd.hasOption("t"))) {
            //System.out.println("running make upload");
            for (String motePath : motelist.getMotes().keySet()) {
                MakeThread t1 = new MakeThread(projectPath, motePath);
                if (silent) {
                    t1.setSilent();
                }
                if (verbose) {
                    t1.setVerbose();
                }
                t1.select(1);
                t1.run();
            }

        } else if (cmd.hasOption("u") && cmd.hasOption("t")) {
            //System.out.println("running make upload threads");
            MakeThread t0 = new MakeThread(projectPath);
            if (silent) {
                t0.setSilent();
            }
            if (verbose) {
                t0.setVerbose();
            }
            t0.select(0);
            t0.run();

            //System.out.println("motes:" + motelist.getMotes().toString());
            for (String motePath : motelist.getMotes().keySet()) {
                MakeThread t1 = new MakeThread(projectPath, motePath);
                if (silent) {
                    t1.setSilent();
                }
                if (verbose) {
                    t1.setVerbose();
                }
                t1.select(1);
                //if specified, create new thread for every mote
                Thread t = new Thread(t1);
                t.start();
                threads.add(t);
            }
        }
        for (Thread t1 : threads) {
            try {
                //System.out.println("waiting for thread "+ t1.toString());
                t1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(UploadMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
