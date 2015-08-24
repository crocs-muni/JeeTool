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

import cz.muni.fi.crocs.EduHoc.Serial.SerialPortHandler;
import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private Long time;

    public SerialMain(CommandLine cmd, MoteList motelist) {
        this.cmd = cmd;
        this.motelist = motelist;

        //get value of time, if not set, use default 15
        time = TimeUnit.MINUTES.toMillis(Integer.parseInt(cmd.getOptionValue("T")));

    }

    public void startSerial() {
        List<SerialPortHandler> handlers = new ArrayList<SerialPortHandler>();
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
                handlers.add(handler);

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
                    handler.write(file);
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
        

        for (SerialPortHandler h : handlers) {
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
