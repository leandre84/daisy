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
    WebshopService backend;

    @EJB
    InitBean initBean;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @PostConstruct
    public void init() {
        try {
            token = backend.retrieveInstallToken();
        } catch (FileNotFoundException e) {
            // Tokenfile not existing yet
        } catch (IOException e) {
            // error during read
            MessageUtil.putError("Error reading token file", e.getMessage());
        }
    }

    public String commitToken() {
        try {
            backend.persistInstallToken(token);
            initBean.insertVulnerabilityData(token);
            return "index.xhtml?faces-redirect=true";
        } catch (IOException ex) {
            MessageUtil.putError("Error saving token", ex.getMessage());
        } catch (DaisyPointsEncryptionException ex) {
            MessageUtil.putError("Error generating sample data", ex.getMessage());
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
            token = null;
        } finally {
            if (backend.deleteInstallToken()) {
                token = null;
            } else {
                MessageUtil.putError("Unable to delete token file", "");
            }
        }
    }  
    
}
