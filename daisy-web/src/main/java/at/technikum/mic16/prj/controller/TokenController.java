package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import at.technikum.mic16.prj.exception.DaisyPointsEncryptionException;
import at.technikum.mic16.prj.service.InitBean;
import at.technikum.mic16.prj.service.WebshopService;
import at.technikum.mic16.prj.util.MessageUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/*
TODO: display of error messages not working yet
*/

/**
 *
 * @author leandros
 */
@ManagedBean(name = "tokenController")
@ApplicationScoped
public class TokenController implements Serializable {

    @EJB
    private WebshopService backend;
    @EJB
    private InitBean initBean;
    
    private String installationToken;
    

    public String getInstallationToken() {
        return installationToken;
    }

    public void setInstallationToken(String installationToken) {
        this.installationToken = installationToken;
    }
    
    @PostConstruct
    public void init() {
        try {
            installationToken = backend.retrieveInstallationToken();
        } catch (FileNotFoundException e) {
            // Tokenfile not existing yet
        } catch (IOException e) {
            // error during read
            MessageUtil.putError("Error reading token file", e.getMessage());
        }
    }
    
    public String commitToken() {
        try {
            backend.persistInstallToken(installationToken);
            initBean.setInstallationToken(installationToken);
            initBean.insertVulnerabilityData();
            return "index.xhtml?faces-redirect=true";
        } catch (IOException ex) {
            MessageUtil.putError("Error saving token", ex.getMessage());
        } catch (Exception ex) {
            MessageUtil.putError("Unknown exception while saving token", ex.getMessage());
            Logger.getLogger(TokenController.class.getName()).log(Level.SEVERE, "Unknown exception occured:", ex);
        }
        deleteToken();
        return null;
    }

    public void deleteToken() {
        try {
            initBean.deleteVulnerabilityData();
            installationToken = null;
        } finally {
            if (backend.deleteInstallationToken()) {
                installationToken = null;
            } else {
                MessageUtil.putError("Unable to delete token file", "");
            }
        }
    }  
    
}
