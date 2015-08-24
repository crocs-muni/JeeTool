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

package cz.muni.fi.crocs.EduHoc.uploadTool;

import cz.muni.fi.crocs.EduHoc.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class FileWriter {

    private String filename;
    private File file;
    private BufferedWriter bw;

    private boolean silent = false;
    private boolean verbose = false;
    
    public void setVerbose(){
        verbose = true;
    }
    
    public void setSilent(){
        silent = true;
    }
    
    public FileWriter() throws IOException {
        filename = "tmpScript_" + Thread.currentThread().getName().toString() + ".sh";
        if(verbose){
            System.out.println("Tmp script created at: " + filename + ", will be deleted on exit.");
        }
        file = new File(filename);
        file.deleteOnExit();
        bw = new BufferedWriter(new java.io.FileWriter(file));
    }

    public void appendToFile(String text) throws IOException{
        bw.write(text);
        bw.newLine();
        if(verbose){
            System.out.println("Text added to script: " + Main.ANSI_CYAN + text + Main.ANSI_RESET);
        }
    }
    
    public void close() throws IOException {
        bw.close();
    }

    public String getFilename() {
        return filename;
    }

    
    
}
