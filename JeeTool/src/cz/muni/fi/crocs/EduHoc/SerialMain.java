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
package cz.muni.fi.crocs.EduHoc;

import cz.muni.fi.crocs.EduHoc.Serial.Generator;
import cz.muni.fi.crocs.EduHoc.Serial.SerialPortHandler;
import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;
import java.io.File;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;

/**
 *
 * @author LukeMcNemee
 */
public class SerialMain {

    private boolean silent = false;
    private boolean verbose = false;
    private CommandLine cmd;
    private MoteList motelist;
    private Long runTime;
    private Map<String, SerialPortHandler> handlersMap;
    //private Generator g;

    public SerialMain(CommandLine cmd, MoteList motelist) {
        this.cmd = cmd;
        this.motelist = motelist;

        //get value of time, if not set, use default 15
        runTime = TimeUnit.MINUTES.toMillis(Integer.parseInt(cmd.getOptionValue("T", "15")));

    }

    public SerialMain(MoteList motelist, Long time) {
        this.motelist = motelist;
        this.runTime = time;
    }

    /*
     public Generator getG() {
     return g;
     }
     */
    public void connect() {
        //g = new Generator();

        System.out.println("Connect to ports");
        handlersMap = new TreeMap<String, SerialPortHandler>();
        for (String mote : motelist.getMotes().keySet()) {
            SerialPortHandler handler = null;
            try {
                //open serial port and connect to it
                handler = new SerialPortHandler();
                if (verbose) {
                    handler.setVerbose();
                }
                if (silent) {
                    handler.setSilent();
                }
                handler.connect(motelist.getMotes().get(mote));
                //handler.setGenerator(g);
                handlersMap.put(mote, handler);
                System.out.println("port " + mote + " connected");
            } catch (IOException ex) {
                System.err.println("port connection to " + mote + " failed");
            }
        }
    }

    public void listen(PipedOutputStream stream) {
        //TODO
        //create new serial port listener with PipedOutputStream
    }

    public void write(String dirPath, Long delay) {
        //TODO
        if (dirPath.charAt(dirPath.length() - 1) == '/') {
            dirPath = dirPath.substring(0, dirPath.length() - 1);
        }
        System.out.println("starting write from " + dirPath);
        for (String mote : motelist.getMotes().keySet()) {
            SerialPortHandler handler = handlersMap.get(mote);
            File file = new File(dirPath, mote.substring(mote.lastIndexOf("/")));
            if (file.exists()) {
                System.out.println("file " + file.getAbsolutePath() + " found, starting write");
                if (delay != 0) {
                    handler.write(file, delay);
                } else {
                    handler.write(file);
                }
            } else {
                System.err.println("file " + file.getAbsolutePath() + " not found!");

            }
        }

    }

    public void close() {
        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(runTime));
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (SerialPortHandler h : handlersMap.values()) {
            h.closePort();
        }
    }

    public void startSerial() {
        //g = new Generator();
        List<SerialPortHandler> handlersList = new ArrayList<SerialPortHandler>();
        for (String mote : motelist.getMotes().keySet()) {
            SerialPortHandler handler = null;
            try {
                //open serial port and connect to it
                handler = new SerialPortHandler();
                handler.connect(motelist.getMotes().get(mote));
                if (verbose) {
                    handler.setVerbose();
                }
                if (silent) {
                    handler.setSilent();
                }
                //handler.setGenerator(g);
                handlersList.add(handler);

            } catch (IOException ex) {
                System.err.println("port connection to " + mote + " failed");
                continue;
            }

            if (cmd.hasOption("l")) {
                //create file for each node
                File file = new File(cmd.getOptionValue("l"), mote.substring(mote.lastIndexOf("/")));

                if (verbose) {
                    System.out.println("File " + file.getAbsolutePath() + " created");
                }
                handler.listen(file);
            }

            if (cmd.hasOption("w")) {

                String prefix = cmd.getOptionValue("w");
                if (prefix.charAt(prefix.length() - 1) == '/') {
                    prefix = prefix.substring(0, prefix.length() - 1);
                }

                File file = new File(prefix, mote.substring(mote.lastIndexOf("/")));
                if (file.exists()) {
                    System.out.println("file " + file.getAbsolutePath() + " found, starting write");
                    if (cmd.hasOption("D")) {
                        handler.write(file, Long.parseLong(cmd.getOptionValue("D")));
                    } else {
                        handler.write(file);
                    }
                }
            }
        }
        try {

            if (cmd.hasOption("T")) {
                if (verbose) {
                    System.out.println("Going to sleep for " + cmd.getOptionValue("T") + " minutes");
                }
                Thread.sleep(TimeUnit.MINUTES.toMillis(Integer.parseInt(cmd.getOptionValue("T"))));
            } else {
                if (verbose) {
                    System.out.println("Going to sleep for " + 15 + " minutes");
                }
                Thread.sleep(TimeUnit.MINUTES.toMillis(15));
            }

        } catch (InterruptedException ex) {

        }

        for (SerialPortHandler h : handlersList) {
            h.closePort();
        }
        System.out.println("All closed");
        System.exit(0);
    }

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }
}
