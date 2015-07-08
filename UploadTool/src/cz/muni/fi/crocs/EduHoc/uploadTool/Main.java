/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import cz.muni.fi.crocs.EduHoc.CppCommon.CppFileParser;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LukeMcNemee
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filepath = System.getenv("EDU_HOC_HOME") + "/config/motePaths.txt";
        System.out.println(filepath);
        try {
            CppFileParser cpp = new CppFileParser(System.getenv("EDU_HOC_HOME") + "/src/common.h");
            System.out.println(Integer.parseInt(cpp.findDefine("SERIAL_FREQUENCY")));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*
        MoteList moteList = new MoteList(filepath);
        if (args.length == 0) {
            System.err.println("Please provide path to makefile");
            help();
            return;
        }
        File makefile = null;
        String projectPath = args[0];
        if (projectPath.charAt(projectPath.length() - 1) == '/') {
            makefile = new File(projectPath + "Makefile");
        } else {
            makefile = new File(projectPath + "/Makefile");
        }
        System.out.println("processing makefile for project at " + projectPath);
        if (!makefile.exists()) {
            System.err.println("makefile not found");
            help();
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
            */    
    }


    public static void help() {
        System.out.println("Arduino mote tool");
        System.out.println("help:");
        System.out.println("provide path to makefile as first parameter");

        System.out.println("and optionally one other options as follows");
        System.out.println("-m  make");
        System.out.println("-c  make clean");
        System.out.println("-u  make upload");
        System.out.println("-t  use multithreaded upload, -u is already included in -t");

    }
}
