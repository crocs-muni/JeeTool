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

package cz.muni.fi.crocs.EduHoc.Serial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author LukeMcNemee
 */
public class CppDefineParser {

    private String filepath;
    private File file;

    private boolean silent = false;
    private boolean verbose = false;
    
    public void setVerbose(){
        verbose = true;
    }
    
    public void setSilent(){
        silent = true;
    }
    
    public CppDefineParser(String filepath) throws IOException {
        this.filepath = filepath;
        this.file = new File(filepath);
        if (!file.exists()) {
            System.err.println("file not found" + filepath);
            throw new IOException("file not found" + filepath);
        }

    }

    public String findDefine(String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = br.readLine();
        while (line != null) {
            if (line.contains("#define") && line.contains(name)) {
                return line.substring(line.indexOf(name) + name.length()).trim();
            }
            line = br.readLine();
        }
        return "";
    }
    
}
