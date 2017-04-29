/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.controller;

import at.technikum.mic16.prj.data.CommandResult;
import at.technikum.mic16.prj.exception.CommandExecutionException;
import at.technikum.mic16.prj.exception.DaisyPointsEncryptionException;
import at.technikum.mic16.prj.service.CommandService;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author leandros
 */
@ManagedBean(name = "commandController")
@RequestScoped
public class CommandController implements Serializable {
        
    @EJB
    private CommandService commandService;

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
    
    public String getRewardToken() {
        return commandService.getRewardToken();
    }

}
