/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.Serial;

import cz.muni.fi.crocs.EduHoc.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author LukeMcNemee
 */
public class SerialPortListener implements Runnable {

    private InputStream is;
    private File file;
    private int time;

    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    public SerialPortListener(InputStream is, File file, int time) {
        this.is = is;
        this.file = file;
        this.time = time;
    }

    public void streamToFile() throws IOException {
        OutputStream os = new FileOutputStream(file);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        Long startTime = System.nanoTime();
        int elapsed = 0;
        while ((bytesRead = is.read(buffer)) != -1 && elapsed <= time) {
            os.write(buffer, 0, bytesRead);
            if (verbose) {
                System.out.println("To file " + file.getAbsolutePath() + " written :" + Main.ANSI_CYAN + bytesRead + Main.ANSI_RESET);
            }
            os.flush();
            Long timeDelta = System.nanoTime() - startTime;
            elapsed = (int) TimeUnit.NANOSECONDS.toMinutes(timeDelta);
        }
        is.close();
        os.close();
        if (!silent) {
            System.out.println("Stream to file " + file.getAbsolutePath() + " closed");
        }
    }

    @Override
    public void run() {
        try {
            streamToFile();
        } catch (IOException ex) {
            System.err.println("Stream to file " + file.getAbsolutePath() + " failed");
        }
    }

}
