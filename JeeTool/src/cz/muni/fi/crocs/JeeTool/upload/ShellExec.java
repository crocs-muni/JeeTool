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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lukemcnemee
 */
public final class ShellExec {

    //static class, only private constructor (not publicly accessible)
    private ShellExec() {
    }

    private static final String ARDUINO_PATH = "/opt/arduino-1.8.3/arduino";
    private static final String BOARD = "--board arduino:avr:mini:cpu=atmega328";

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

    public static void verify(String file) throws IOException {
        String command = ARDUINO_PATH;
        //TODO build path?
        command = command.concat(" --verify");
        command = command.concat(" " + file);
        executeCommand(command);
    }
    
    public static void upload(String mote, String file, boolean silent) throws IOException {
        String command = ARDUINO_PATH;
        //TODO build path?
        command = command.concat(" " + BOARD);
        command = command.concat(" --port " + mote);
        command = command.concat(" --upload");
        command = command.concat(" " + file);
        executeCommand(command, silent);
    }

    public static void upload(String mote, String file) throws IOException {
        upload(mote, file, true);
    }

}
