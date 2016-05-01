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
package cz.muni.fi.crocs.EduHoc.Serial;

import cz.muni.fi.crocs.EduHoc.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author LukeMcNemee
 */
public class SerialPortWriter implements Runnable {

    private boolean silent = false;
    private boolean verbose = false;
    private final File filePath;
    private final SerialPort port;
    private Long delay;
    private int seed;
    //private Generator g;

    public SerialPortWriter(SerialPort port, File filePath) {
        this.filePath = filePath;
        this.port = port;
        this.delay = new Long(0);
        
        
        
    }


    public int getSeed() {
        return seed;
    }

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            //wait, before node is initialized, so that no characters are missed
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            if (!silent) {
                System.err.println(ex.toString());
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();

            while (line != null) {
                if (verbose) {
                    System.out.println("To serial from file " + filePath + " " + Main.ANSI_CYAN + line + Main.ANSI_RESET);
                }
                CppDefineParser cpp = new CppDefineParser(System.getenv("EDU_HOC_HOME") + "/src/common.h");
                int msgLength = Integer.parseInt(cpp.findDefine("MAX_MESSAGE_LENGTH"));

                if (line.length() > msgLength) {
                    line = line.substring(0, msgLength);
                }
                /*
                while(line.length() < msgLength){
                    line = line + "X";
                }
                */
                //line = g.getNextValue() + "#" + line + "#" ;
                port.writeString(line + "\n");
                int jitter = 0;
                if (delay != 0) {
                    jitter = (int) (Math.random() * 10) - 5;
                }
                Thread.sleep(TimeUnit.SECONDS.toMillis(delay) + jitter);
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException ex) {
            if (!silent) {
                System.err.println("File not found " + ex.toString());
            }
        } catch (IOException ex) {
            if (!silent) {
                System.err.println("Could not read file  " + ex.toString());
            }
        } catch (SerialPortException ex) {
            if (!silent) {
                System.err.println("Serial port error  " + ex.toString());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialPortWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
