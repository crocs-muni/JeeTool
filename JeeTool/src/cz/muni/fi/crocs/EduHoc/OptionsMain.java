/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 *
 * @author LukeMcNemee
 */
public class OptionsMain {

    public static Options createOptions() {
        Option help = new Option("h", "help", false, "show help and exit");

        Option motes = new Option("a", "motes", true, "path to alternative file of motes, default setting is stored in confix/motePaths.txt");
        

        Option detect = new Option("d", "detect", false, "Perform node detection only");

        Option verbose = new Option("v", "verbose", false, "show extended text output");
        Option silent = new Option("s", "silent", false, "show limited text output");
        OptionGroup output = new OptionGroup();
        output.addOption(verbose);
        output.addOption(silent);

        Option make = new Option("m", "make", true, "make target at this path, directory must contain Makefile");
        Option makeClean = new Option("c", "make_clean", true, "make clean target at this path, directory must contain Makefile");
        Option makeUpload = new Option("u", "make_upload", true, "make target  at this path and upload to nodes, directory must contain Makefile");
        Option threads = new Option("t", "threads", false, "use threads for paralell upload");

        OptionGroup makeGroup = new OptionGroup();
        makeGroup.addOption(make);
        makeGroup.addOption(makeUpload);
        makeGroup.addOption(makeClean);

        Option listen = new Option("l", "listen", true, "connect to serial and save stream to files, set path to directory for files");
        Option write = new Option("w", "write", true, "write to serial now or after upload, directory must contain at least one file with same name as node");
        
        Option execute = new Option("E", "execute", true, "path to shell script to be executed in phase 3");
        
        Options options = new Options();

        options.addOption(help);
        options.addOption(motes);
        options.addOption(detect);

        options.addOptionGroup(output);

        options.addOptionGroup(makeGroup);

        options.addOption(threads);
        options.addOption(write);
        
        options.addOption(execute);

        return options;
    }

    public static void printHelp(Options options) {
        String header = "JeeTool, tool for mass management of arduino network\n\n"
                + "There are four phases : \n 1 - node detection \n 2 - make execution \n 3 - script execution \n 4 - serial listen / write\n\n";
        String footer = "";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("jeetool", header, options, footer, false);
    }
    
    
}
