/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.uploadTool;

import java.io.File;
import java.io.IOException;
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
        
        UploadThread t1 = new UploadThread(projectPath);
        
        
        //if specified, create new thread for every 
        new Thread(t1).start();
                
        
    }

    public static void help() {
        System.out.println("Arduino mote tool");
        System.out.println("help:");
        System.out.println("provide path to makefile as first parameter");
        /*
         System.out.println("and optionally other options as follows");
         System.out.println("-t  use multithreaded upload");
         System.out.println("-v  verbose");
         System.out.println("-m  mute");        
         */
    }
}
