/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.service;

import at.technikum.mic16.prj.daisypoints.DaisyPointsCrypter;
import at.technikum.mic16.prj.data.CommandResult;
import at.technikum.mic16.prj.exception.CommandExecutionException;
import at.technikum.mic16.prj.exception.DaisyPointsEncryptionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author leandros
 */
@Named
@Stateless
@LocalBean
public class CommandService {

    private static final Logger LOG = LoggerFactory.getLogger(CommandService.class.getName());

    // seconds for wait for elapsed command execution
    private final static int CMD_WAIT_FOR = 3;

    @Inject
    private WebshopService webshopService;
    
    private String rewardToken = null;

    public CommandResult runArbitraryCommand(String cmd) throws CommandExecutionException {

        CommandResult result = new CommandResult();

        Runtime rt = Runtime.getRuntime();
        Process p;
        try {
            p = rt.exec(cmd);
            p.waitFor(CMD_WAIT_FOR, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            throw new CommandExecutionException(e.getMessage());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String cmdOutput = "";
        String line;
        try {
            while ((line = br.readLine()) != null) {
                cmdOutput = cmdOutput.concat(line).concat(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new CommandExecutionException(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException ignore) {
            }
        }

        try {
            result.setExitCode(p.exitValue());
        } catch (IllegalThreadStateException e) {
            throw new CommandExecutionException("Command '" + cmd + "'did not return after " + CMD_WAIT_FOR + " seconds");
        }
        result.setStdOut(cmdOutput);

        LOG.info("Executed command: {}", cmd);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Output: {}", cmdOutput);
        }

        return result;
    }
    
    public String getRewardToken() {
        if (rewardToken == null) {
            try {
                String installToken = webshopService.retrieveInstallationToken();
                rewardToken = DaisyPointsCrypter.encryptMessage(installToken, "Vulnerability|3");
            } catch (DaisyPointsEncryptionException | IOException ignore) {
                // bad luck
            } 
        }
        return rewardToken;
    }

}
