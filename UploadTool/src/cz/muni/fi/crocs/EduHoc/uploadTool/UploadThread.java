/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LukeMcNemee
 */
public class UploadThread implements Runnable {

    private String projectPath;
    private int mode = 0;

    public UploadThread(String projectPath) {
        this.projectPath = projectPath;
    }

    public void select(int mode) {
        this.mode = mode;
    }

    @Override
    public void run() {
        try {
            ExecuteShellCommand com = new ExecuteShellCommand();
            FileWriter writer = new FileWriter();
            writer.appendToFile("#./bin/bash");
            writer.appendToFile("cd " + projectPath);
            switch (mode) {
                case 0:
                    writer.appendToFile("make");
                    break;
                case 1:
                    writer.appendToFile("make upload");
                    break;
                case 2:
                    writer.appendToFile("make clean");
                    break;
                default:
                    writer.appendToFile("make");
                    break;

            }

            //writer.appendToFile("ls");
            writer.close();
            com.executeCommand("chmod a+x " + writer.getFilename());
            com.executeCommand("./" + writer.getFilename());
            com.executeCommand("echo \"shell finished\"");
        } catch (IOException ex) {
            Logger.getLogger(UploadThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
