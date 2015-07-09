/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.crocs.EduHoc.Serial;

import cz.muni.fi.crocs.EduHoc.uploadTool.MoteList;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LukeMcNemee
 */
public class NodesListener {
    private MoteList motes;
    private String filePath;
    
    
    public NodesListener(MoteList motes, String filePath) {
        this.motes = motes;
        this.filePath = filePath;
    }
    
    public void Start(){
        for(String mote: motes.getMotes()){
            try {
                SerialPortHandler handler = new SerialPortHandler();
                handler.connect(mote);
                InputStream is = handler.getSerialInputStream();
                String file = null;
                SerialPortListener listener = new SerialPortListener(is, file);
                
                
            } catch (IOException ex) {
                System.err.println("port connection to " + mote + " failed");
            }
            
        }
    }
}
