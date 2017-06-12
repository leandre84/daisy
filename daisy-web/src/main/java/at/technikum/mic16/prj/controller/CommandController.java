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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Backing bean for executing commands (_admin_/index.xhtml)
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

    public String[] getAvailableCommands() {
        return commandService.getALLOWED_COMMANDS();
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
    
    /**
     * Execute a command read from a possibly user modified input field
     * Once again we fight against the framework...
     */
    public void runCommand() {
        
        // Fetch command to execute from request parameter, setting the beans's cmd didn't work out as intended, too much validation by JSF :(
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        // ID's from _admin_/index.xhtml
        cmd = request.getParameter("form1:selectonemenu1");      
        
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
