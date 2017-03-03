/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.controller;

import at.technikum.mic16.prj.data.CommandResult;
import at.technikum.mic16.prj.exception.CommandExecutionException;
import at.technikum.mic16.prj.service.CommandService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author leandros
 */
@Named(value = "commandController")
@SessionScoped
public class CommandController implements Serializable {
        
    @EJB
    CommandService commandService;

    private String cmd;
    private String output = "";
    private int returnValue;

    public CommandController() {
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmdInput) {
        this.cmd = cmdInput;
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
    
    public void runCommand() {
        try {
            CommandResult result = null;
            result = commandService.runArbitraryCommand(cmd);
            output = result.getStdOut();
            // TODO: what to do with stderr?
            returnValue = result.getExitCode();
        } catch (CommandExecutionException e) {
            output = e.getMessage();
        }
        
    }

}
