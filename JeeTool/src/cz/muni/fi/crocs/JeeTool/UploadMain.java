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
import cz.muni.fi.crocs.JeeTool.upload.ShellExec;
import cz.muni.fi.crocs.JeeTool.upload.Uploadthread;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author LukeMcNemee
 */
public class UploadMain {

    private MoteList motelist;
    private File makefile;
    private String projectFile;
    private CommandLine cmd;

    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    //cmd version
    public UploadMain(MoteList motelist, CommandLine cmd) {
        this.motelist = motelist;
        this.cmd = cmd;

        if (cmd.hasOption("m")) {
            //System.out.println("running make");
            projectFile = cmd.getOptionValue("m");
        }
        if (cmd.hasOption("u")) {
            //System.out.println("running make upload");
            projectFile = cmd.getOptionValue("u");
        }
        //TODO check file .ino
    }

    public void runMake() {
        List<Thread> threads = new ArrayList<Thread>();
        if (cmd.hasOption("m")) {
            try {
                ShellExec.verify(projectFile);
            } catch (IOException ex) {
                System.err.println("Error executing verify: " + ex.getMessage());
            }
        } else if (cmd.hasOption("u") && (!cmd.hasOption("t"))) {
            //System.out.println("running make upload");
            for (String motePath : motelist.getMotes().keySet()) {
                Uploadthread r = new Uploadthread(motePath, projectFile, silent);
                r.run();
            }

        } else if (cmd.hasOption("u") && cmd.hasOption("t")) {
            for (String motePath : motelist.getMotes().keySet()) {
                Uploadthread r = new Uploadthread(motePath, projectFile, silent);
                Thread t = new Thread(r);
                t.start();
                threads.add(t);
            }
        }
        for (Thread t1 : threads) {
            try {
                t1.join();
            } catch (InterruptedException ex) {
            }
        }
    }
}
