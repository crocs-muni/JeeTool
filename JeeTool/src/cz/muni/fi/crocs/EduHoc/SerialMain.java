/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc;

import cz.muni.fi.crocs.EduHoc.Serial.SerialPortHandler;
import cz.muni.fi.crocs.EduHoc.Serial.SerialPortListener;
import cz.muni.fi.crocs.EduHoc.Serial.SerialPortWriter;
import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;
import java.io.File;
import java.io.IOException;
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
    private int time;

    public SerialMain(CommandLine cmd, MoteList motelist) {
        this.cmd = cmd;
        this.motelist = motelist;

        //get value of time, if not set, use default 15
        time = Integer.parseInt(cmd.getOptionValue("T", "15"));

    }

    public void startSerial() {
        for (String mote : motelist.getMotes()) {
            SerialPortHandler handler = null;
            try {
                //open serial port and connect to it
                handler = new SerialPortHandler();
                handler.connect(mote);

            } catch (IOException ex) {
                System.err.println("port connection to " + mote + " failed");
                continue;
            }

            if (cmd.hasOption("l")) {
                //create file for each node
                File file = new File(cmd.getOptionValue("l"), mote.substring(mote.lastIndexOf("/")));

                SerialPortListener listener = new SerialPortListener(handler.getSerialInputStream(), file, time);
                if (verbose) {
                    listener.setVerbose();
                    System.out.println("File " + file.getAbsolutePath() + " created");
                }
                if (silent) {
                    listener.setSilent();
                }
                new Thread(listener).start();
            }
            
            if (cmd.hasOption("w")) {
                String prefix = cmd.getOptionValue("w");
                if (prefix.charAt(prefix.length() - 1) == '/') {
                    prefix = prefix.substring(0, prefix.length() - 1);
                }

                File file = new File(prefix, mote.substring(mote.lastIndexOf("/")));
                if (file.exists()) {
                    SerialPortWriter writer = new SerialPortWriter(file, handler.getSerialOutputStream());
                    if (verbose) {
                        writer.setVerbose();
                    }
                    if (silent) {
                        writer.setSilent();
                    }
                    new Thread(writer).start();                    
                }
            }

            
        }
    }

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }
}
