/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc;

import cz.muni.fi.crocs.EduHoc.Serial.SerialPortHandler;
import cz.muni.fi.crocs.EduHoc.Serial.SerialPortListener;
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

            if (cmd.hasOption("w")) {
                File file = new File(cmd.getOptionValue("w"), mote.substring(mote.lastIndexOf("/")));
                if (file.exists()) {

                }
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
        }
    }

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }
}
