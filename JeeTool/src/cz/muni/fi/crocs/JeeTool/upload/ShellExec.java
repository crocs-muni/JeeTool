/*
 * The MIT License
 *
 * Copyright 2017 lukemcnemee.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.muni.fi.crocs.JeeTool.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author lukemcnemee
 */
public final class ShellExec {

    //static class, only private constructor (not publicly accessible)
    private ShellExec() {
    }

    public static void executeCommand(String command, boolean silent) throws IOException {

        System.out.println("Executing shell command " + command);

        Process cmdProc = Runtime.getRuntime().exec(command);

        String line;
        if (!silent) {
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println(line);
            }
        }
        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
        while ((line = stderrReader.readLine()) != null) {
            System.err.println(line);
        }
    }

    public static void executeCommand(String command) throws IOException {
        executeCommand(command, true);
    }
    
    public static void verify(String file){
        
    }
    
    private static Thread upload(String mote, String file){
        
    }
    
    public static void upload(MoteList list, String file){
        
    }
    
}
