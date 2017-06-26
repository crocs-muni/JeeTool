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

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import cz.muni.fi.crocs.JeeTool.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LukeMcNemee
 */
public class SerialPortListener implements SerialPortDataListener {

    private File file;
    private Long time;
    private BufferedWriter bw;
    private SerialPort port;

    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    public SerialPortListener(SerialPort port, File file) throws IOException {
        this.port = port;
        this.file = file;
        bw = new BufferedWriter(new FileWriter(file));

    }

    public void close() {
        try {
            bw.close();
        } catch (IOException ex) {

        }
    }



    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        int available = port.bytesAvailable();
        byte[] buffer = new byte[available + 2];
        port.readBytes(buffer, available);
        String text = new String(buffer);

        if (text != null) {
            try {
                bw.write(text);
                bw.flush();
                if (verbose) {
                    System.out.println("To file from serial " + port.getDescriptivePortName() + " " + Main.ANSI_CYAN + text + Main.ANSI_RESET);
                }
            } catch (IOException ex) {
                Logger.getLogger(SerialPortListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
