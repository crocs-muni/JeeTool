/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc;

import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;
import cz.muni.fi.crocs.EduHoc.uploadTool.UploadThread;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("JeeTool \n");        
        
        
        Options options = OptionsMain.createOptions();
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException ex) {
            System.err.println("cannot parse parameters");
            OptionsMain.printHelp(options);
            System.err.println(ex.toString());
            return;
        }
        boolean silent = cmd.hasOption("s");
        boolean verbose = cmd.hasOption("v");
        
        if(!silent) System.out.println("EduHoc home is: " + System.getenv("EDU_HOC_HOME") + "\n");
        
        if(cmd.hasOption("h")){
            OptionsMain.printHelp(options);
            return;
        }        
        
        String filepath;
        if(cmd.hasOption("a")){
            filepath = cmd.getOptionValue("a");
        } else {
            filepath = System.getenv("EDU_HOC_HOME") + "/config/motePaths.txt";        
        }
        MoteList moteList = new MoteList(filepath);
        
        if(verbose) System.out.println("reading motelist from file " + filepath);
        
        
        UploadMain upload = new UploadMain(moteList, args[0]);
        File makefile;
        String projectPath = args[0];
        if (projectPath.charAt(projectPath.length() - 1) == '/') {
            makefile = new File(projectPath + "Makefile");
        } else {
            makefile = new File(projectPath + "/Makefile");
        }
        System.out.println("processing makefile for project at " + projectPath);
        if (!makefile.exists()) {
            System.err.println("makefile not found");
            //help();
            return;
        }
        System.out.println("Makefile " + makefile.getPath() + " found");

        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("-m")) {
            UploadThread t1 = new UploadThread(projectPath);
            t1.select(0);
            t1.run();

        } else if (argsList.contains("-c")) {
            UploadThread t1 = new UploadThread(projectPath);
            t1.select(2);
            t1.run();

        } else if (argsList.contains("-u")) {
            for (String motePath : moteList.getMotes()) {
                UploadThread t1 = new UploadThread(projectPath, motePath);
                t1.select(1);
                t1.run();
            }

        } else if (argsList.contains("-t")) {
            UploadThread t0 = new UploadThread(projectPath);
            t0.select(0);
            t0.run();
            
            for (String motePath : moteList.getMotes()) {
                UploadThread t1 = new UploadThread(projectPath, motePath);
                t1.select(1);
                //if specified, create new thread for every 
                new Thread(t1).start();
            }
        }
               
        
        
               
    }


    
}
