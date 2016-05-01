/*
 * The MIT License
 *
 * Copyright 2016 lukemcnemee.
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
package cz.muni.fi.crocs.EduHoc;

import static cz.muni.fi.crocs.EduHoc.Main.ANSI_GREEN;
import static cz.muni.fi.crocs.EduHoc.Main.ANSI_RESET;
import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;

/**
 *
 * @author lukemcnemee
 */
public class runAndRepeat {

    public static final String PATH = System.getenv("EDU_HOC_HOME") + "/src/Scenarios/01";
    public static final String WRITE_FILES = System.getenv("EDU_HOC_HOME") + "/AdditionalScripts/";

    public static void main(String[] args) {
        System.out.println(ANSI_GREEN + "EduHoc home is: " + System.getenv("EDU_HOC_HOME") + "\n" + ANSI_RESET);

        run();

    }

    public static void run() {
        //make upload
        String filepath = System.getenv("EDU_HOC_HOME") + "/config/motePathHome.txt";
        MoteList motes = new MoteList(filepath);
        System.out.println("reading motelist from file " + filepath);
        motes.setVerbose();
        motes.readFile();
        UploadMain upload = new UploadMain(motes, PATH);
        upload.setVerbose();
        upload.makeUpload();

        //listen + write
        SerialMain serial = new SerialMain(motes, (long) 15);
        serial.connect();
        serial.write(WRITE_FILES, (long) 1);

    }
}
