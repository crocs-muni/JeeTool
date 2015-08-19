/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc;

import cz.muni.fi.crocs.EduHoc.uploadTool.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author LukeMcNemee
 */
public class Main {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("JeeTool \n");

        Options options = OptionsMain.createOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("cannot parse parameters");
            OptionsMain.printHelp(options);
            System.err.println(ex.toString());
            return;
        }
        boolean silent = cmd.hasOption("s");
        boolean verbose = cmd.hasOption("v");

        if (!silent) {
            System.out.println(ANSI_GREEN + "EduHoc home is: " + System.getenv("EDU_HOC_HOME") + "\n" + ANSI_RESET);
        }

        //help
        if (cmd.hasOption("h")) {
            OptionsMain.printHelp(options);
            return;
        }

        String filepath;
        //path to config list of nodes
        if (cmd.hasOption("a")) {
            filepath = cmd.getOptionValue("a");
        } else {
            filepath = System.getenv("EDU_HOC_HOME") + "/config/motePaths.txt";
        }

        //create motelist
        MoteList moteList = new MoteList(filepath);
        if (verbose) {
            moteList.setVerbose();
        }
        if (silent) {
            moteList.setSilent();
        }

        if (verbose) {
            System.out.println("reading motelist from file " + filepath);
        }

        
        if(cmd.hasOption("i")){
            List<Integer> ids = new ArrayList<Integer>();
            String arg = cmd.getOptionValue("i");
            String [] IdArgs = arg.split(",");
            for(String s : IdArgs){
                if(s.contains("-")){
                    int start = Integer.parseInt(s.substring(0, s.indexOf("-")));
                    int end = Integer.parseInt(s.substring(s.indexOf("-")+1, s.length()));
                    for(int i = start; i <= end; i++){
                        ids.add(i);
                    }
                } else {
                    ids.add(Integer.parseInt(s));
                }
            }
            moteList.setIds(ids);
        }
        
        
        moteList.readFile();
        
        if (cmd.hasOption("d")) {
            //only detect nodes
            return;
        }

        //if make
        if (cmd.hasOption("m") || cmd.hasOption("c") || cmd.hasOption("u")) {
            UploadMain upload = new UploadMain(moteList, cmd);
            upload.runMake();
        }

        //if execute command
        if (cmd.hasOption("E")) {
            try {
                ExecuteShellCommand com = new ExecuteShellCommand();
                if (verbose) {
                    System.out.println("Executing shell command " + cmd.getOptionValue("E"));
                }

                com.executeCommand(cmd.getOptionValue("E"));
            } catch (IOException ex) {
                System.err.println("Execute command " + cmd.getOptionValue("E") + " failed");
            }
        }

        //if serial
        if (cmd.hasOption("l") || cmd.hasOption("w")) {
            SerialMain serial = new SerialMain(cmd, moteList);
            if (silent) {
                serial.setSilent();
            }
            if (verbose) {
                serial.setVerbose();
            }
            serial.startSerial();            
        }

    }

}
