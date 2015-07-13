/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LukeMcNemee
 */
class ExecuteShellCommand {

    private List<String> commands;

    private boolean silent = false;
    private boolean verbose = false;
    
    public void setVerbose(){
        verbose = true;
    }
    
    public void setSilent(){
        silent = true;
    }
    
    public ExecuteShellCommand() {
        commands = new ArrayList<String>();
    }

    public void addCommand(String cmd) {
        commands.add(cmd);
    }

    public void executeCommand(String command) throws IOException {

        Process cmdProc = Runtime.getRuntime().exec(command);

        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
        String line;
        while ((line = stdoutReader.readLine()) != null) {
            System.out.println(line);
        }

        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
        while ((line = stderrReader.readLine()) != null) {
            System.err.println(line);
        }

        //int retValue = cmdProc.exitValue();

    }

}
