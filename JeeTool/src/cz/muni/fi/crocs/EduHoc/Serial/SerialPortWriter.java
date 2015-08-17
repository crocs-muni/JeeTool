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
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialPortWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = "1 10 5\n";
            PrintStream printStream = new PrintStream(outStream);

            //while (line != null) {
                //if (verbose) {
                    System.out.println("To serial from file " + filePath + " " + Main.ANSI_CYAN + line + Main.ANSI_RESET);
                //}
                printStream.print(line);
                printStream.flush();
                //line = br.readLine();
            //}
            printStream.close();

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
