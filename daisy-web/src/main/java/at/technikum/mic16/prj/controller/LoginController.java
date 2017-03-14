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
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author leandros
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController implements Serializable {
    
    @EJB
    WebshopService backend;
    
    private User user;
    private List<UserRole> roles;
    
    private String inputUser;
    private String inputPassword;
    
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
    
    
    
    public String logIn() {
        String hashedPassword;
        try {
            hashedPassword = JBossPasswordUtil.getPasswordHash(inputPassword);
            user = backend.authenticateUser(inputUser, hashedPassword);
            roles = backend.getUserRoles(user);
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Login failed: " + e.getMessage()));
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
        user = null;
        roles = null;
    }
    
}
