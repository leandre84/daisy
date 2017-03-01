/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author leandros
 */
@Named(value = "commandController")
@SessionScoped
public class CommandController implements Serializable {
    
    private final static int WAIT_SECONDS = 3;

    private String cmdInput;
    private String output = "";
    private int returnValue;

    public CommandController() {
    }

    public String getCmdInput() {
        return cmdInput;
    }

    public void setCmdInput(String cmdInput) {
        this.cmdInput = cmdInput;
    }

    public String getOutput() {
        return output.replace(System.lineSeparator(), "<br/>");
    }

    public int getReturnValue() {
        return returnValue;
    }

    private void clear() {
        output = "";
        returnValue = -1;
    }
   
    public void runArbitraryCommand(String cmd) {
        clear();
        Runtime rt = Runtime.getRuntime();
        Process p;
        try {
            p = rt.exec(cmd);
            p.waitFor(WAIT_SECONDS, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            output = "Error executing command: " + e.getMessage();
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String cmdOutput = "";
        String line;
        try {
            while ((line = br.readLine()) != null) {
                cmdOutput = cmdOutput.concat(line).concat(System.lineSeparator());
            }
        } catch (IOException e) {
            output = "Error reading output from command: " + e.getMessage();
        } finally {
            try {
                br.close();
            } catch (IOException ignore) {
            }
        }

        try {
            returnValue = p.exitValue();
        } catch (IllegalThreadStateException e) {
            output = "Command did not return after " + WAIT_SECONDS + " seconds.";
            return;
        }
        output = cmdOutput;
        
        
        System.out.println("Output from command: " + cmd);
        System.out.println(output);

    }
    
    public void runCommand() {
        runArbitraryCommand(cmdInput);
    }

}
