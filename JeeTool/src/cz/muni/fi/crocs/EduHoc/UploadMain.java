/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc;

import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;
import cz.muni.fi.crocs.EduHoc.uploadTool.MakeThread;
import java.io.File;
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

    public UploadMain(MoteList motelist, CommandLine cmd) {
        this.motelist = motelist;
        this.cmd = cmd;

        if (cmd.hasOption("m")) {
            projectPath = cmd.getOptionValue("m");
        }
        if (cmd.hasOption("c")) {
            projectPath = cmd.getOptionValue("c");
        }
        if (cmd.hasOption("u")) {
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

    public void runMake() {
        if (cmd.hasOption("m")) {
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
            for (String motePath : motelist.getMotes()) {
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
            MakeThread t0 = new MakeThread(projectPath);
            if (silent) {
                t0.setSilent();
            }
            if (verbose) {
                t0.setVerbose();
            }
            t0.select(0);
            t0.run();

            for (String motePath : motelist.getMotes()) {
                MakeThread t1 = new MakeThread(projectPath, motePath);
                if (silent) {
                    t1.setSilent();
                }
                if (verbose) {
                    t1.setVerbose();
                }
                t1.select(1);
                //if specified, create new thread for every mote
                new Thread(t1).start();
            }
        }
    }
}
