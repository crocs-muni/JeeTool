/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.crocs.EduHoc.Serial;

import gnu.io.*;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Embedded Freaks
 * https://embeddedfreak.wordpress.com/2008/08/08/how-to-open-serial-port-using-rxtx/
 */
public class SerialPortHandler {

    private SerialPort serialPort;
    private OutputStream outStream;
    private InputStream inStream;

    private boolean silent = false;
    private boolean verbose = false;

    public void setVerbose() {
        verbose = true;
    }

    public void setSilent() {
        silent = true;
    }

    public void connect(String portName) throws IOException {
        try {
            // Obtain a CommPortIdentifier object for the port you want to open
            File f = new File(portName);
            
            System.setProperty("gnu.io.rxtx.SerialPorts", f.getCanonicalPath());
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(f.getCanonicalPath());

            // Get the port's ownership
            serialPort = (SerialPort) portId.open("JeeTool", 5000);

            // Set the parameters of the connection.
            setSerialPortParameters();

            // Open the input and output streams for the connection. If they won't
            // open, close the port before throwing an exception.
            outStream = serialPort.getOutputStream();
            inStream = serialPort.getInputStream();
        } catch (NoSuchPortException e) {
            throw new IOException(e.getMessage());
        } catch (PortInUseException e) {
            throw new IOException(e.getMessage());
        } catch (IOException e) {
            serialPort.close();
            throw e;
        }
    }

    public void closePort() {
        System.out.println("Closing serial port");

        new Thread(){
        @Override
        public void run(){
            try{
            inStream.close();
            outStream.close();
            serialPort.close();
            }catch (IOException ex) {}
        }
        }.start();
    
        System.out.println("Closed serial port");

    }

    /**
     * Get the serial port input stream
     *
     * @return The serial port input stream
     */
    public InputStream getSerialInputStream() {
        return inStream;
    }

    /**
     * Get the serial port output stream
     *
     * @return The serial port output stream
     */
    public OutputStream getSerialOutputStream() {
        return outStream;
    }

    /**
     * Sets the serial port parameters
     */
    private void setSerialPortParameters() throws IOException {
        CppDefineParser cpp = new CppDefineParser(System.getenv("EDU_HOC_HOME") + "/src/common.h");
        int baudRate = Integer.parseInt(cpp.findDefine("SERIAL_FREQUENCY"));

        try {
            // Set serial port to 57600bps-8N1..my favourite
            serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        } catch (UnsupportedCommOperationException ex) {
            throw new IOException("Unsupported serial port parameter");
        }
    }
}
