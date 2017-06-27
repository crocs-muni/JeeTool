/*
* The MIT License (MIT)
*
* Copyright (c) 2015 CRoCS
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package cz.muni.fi.crocs.JeeTool;

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

        Option motes = new Option("a", "motes", true, "path to alternative file of motes, default setting is stored in /opt/motePaths.txt");
        Option ids = new Option("i", "id", true, "select IDs of nodes from motelist, use comma separated list of IDs or ID ranges, such as 1,3,5-7,9-15");

        Option detect = new Option("d", "detect", false, "Perform node detection only");

        Option verbose = new Option("v", "verbose", false, "show extended text output");
        Option silent = new Option("s", "silent", false, "show limited text output");
        OptionGroup output = new OptionGroup();
        output.addOption(verbose);
        output.addOption(silent);

        Option make = new Option("m", "make", true, "make target at this path, .ino file required");
        Option makeClean = new Option("c", "make_clean", true, "make clean target at this path, .ino file required");
        Option makeUpload = new Option("u", "make_upload", true, "make target  at this path and upload to nodes, .ino file required");
        Option threads = new Option("t", "threads", false, "use threads for paralell upload");

        OptionGroup makeGroup = new OptionGroup();
        makeGroup.addOption(make);
        makeGroup.addOption(makeUpload);
        makeGroup.addOption(makeClean);

        Options options = new Options();

        options.addOption(help);
        options.addOption(motes);
        options.addOption(ids);
        options.addOption(detect);

        options.addOptionGroup(output);
        options.addOptionGroup(makeGroup);

        options.addOption(threads);       
        return options;
    }

    public static void printHelp(Options options) {
        String header = "JeeTool, tool for mass management of arduino network\n\n";
        String footer = "";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("JeeTool", header, options, footer, false);
    }
    
    
}
