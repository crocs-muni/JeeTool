/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.Serial;

import cz.muni.fi.crocs.EduHoc.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author LukeMcNemee
 */
public class SerialPortWriter implements Runnable {

    private boolean silent = false;
    private boolean verbose = false;
    private File filePath;
    private OutputStream outStream;

    public SerialPortWriter(File filePath, OutputStream outStream) {
        this.filePath = filePath;
        this.outStream = outStream;
    }

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();

            while (line != null) {
                if (verbose) {
                    System.out.println("To serial from file " + filePath + " " + Main.ANSI_CYAN + line + Main.ANSI_RESET);
                }
                outStream.write(line.getBytes());
                outStream.flush();
                line = br.readLine();
            }

        } catch (FileNotFoundException ex) {
            if (!silent) {
                System.err.println("File not found " + ex.toString());
            }
        } catch (IOException ex) {
            if (!silent) {
                System.err.println("Port write failed " + ex.toString());
            }
        }
    }
}
