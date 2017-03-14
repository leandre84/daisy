package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
import at.technikum.mic16.prj.service.WebshopService;
import at.technikum.mic16.prj.util.JBossPasswordUtil;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.NoResultException;


/**
 *
 * @author leandros
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {
    
    @EJB
    WebshopService backend;
    
    private User user;
    private List<UserRole> roles;
    
    private String inputUser;
    private String inputPassword;
    
    private String errorMessage;
    
    public LoginController() {
    }

    public User getUser() {
        return user;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    
    
    
    public String logIn() {
        String hashedPassword;
        try {
            errorMessage = "";
            hashedPassword = JBossPasswordUtil.getPasswordHash(inputPassword);
            user = backend.authenticateUser(inputUser, hashedPassword);
            roles = backend.getUserRoles(user);
            return "index.xhtml?faces-redirect=true";
        } catch (NoResultException e ) {
            errorMessage = "Login failed!";
        } catch (UnsupportedEncodingException e) {
            errorMessage = "Error while logging in: " + e.getMessage();
        } catch (Exception e) {
            errorMessage = "Exception occured: " + e.getMessage();
        }
        
        return null;
    }
    
    public String logOut() {
        reset();
        return "index.xhtml?faces-redirect=true";
    }
    
    public void reset() {
        inputUser = "";
        inputPassword = "";
        errorMessage = "";
        user = null;
        roles = null;
    }
    
}
