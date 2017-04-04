package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import at.technikum.mic16.prj.daisypoints.DaisyPointsEncryptionException;
import at.technikum.mic16.prj.service.InitBean;
import at.technikum.mic16.prj.service.WebshopService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

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
    private String errorMessage;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @PostConstruct
    public void init() {
        try {
            token = backend.retrieveInstallToken();
        } catch (FileNotFoundException e) {
            // Tokenfile not existing yet
        } catch (IOException e) {
            // error during read
            errorMessage = "Error reading token file :-(";
        }
    }
    
    public String commitToken() {
        if (token == null || token.isEmpty()) {
            errorMessage = "Please enter a token!";
            return null;
        }
        errorMessage = null;
        try {
            backend.persistInstallToken(token);
            initBean.insertVulnerabilityData(token);
            return "index.xhtml?faces-redirect=true";
        } catch (IOException ex) {
            errorMessage = "Error installing token";
        } catch (DaisyPointsEncryptionException ex) {
            deleteToken();
            errorMessage = "Error generating sample data";
            Logger.getLogger(TokenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    

    public void deleteToken() {
        initBean.deleteVulnerabilityData();
        if (backend.deleteInstallToken()) {
            errorMessage = null;
            token = null;
        } else {
            errorMessage = "Unable to delete token file";
        }
    }

}
