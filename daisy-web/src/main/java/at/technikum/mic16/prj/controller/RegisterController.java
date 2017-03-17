package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.service.WebshopService;
import at.technikum.mic16.prj.util.JBossPasswordUtil;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author leandros
 */
@ManagedBean(name = "registerController")
@ViewScoped
public class RegisterController implements Serializable {
    
    @EJB
    WebshopService backend;
    
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    
    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;
    
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String eMail) {
        this.email = eMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String doRegister() {
        try {
            backend.createNewUser(email, JBossPasswordUtil.getPasswordHash(password), firstName, lastName);
            loginController.setInputUser(email);
            loginController.setInputPassword(password);
            loginController.logIn();
            navigationController.setCurrentPage("products_overview.xhtml");
            return "index.xhtml?faces-redirect=true";
        } catch (UnsupportedEncodingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "This should never happen ;-) : " + ex.getMessage()));
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EJBTransactionRolledbackException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User already existing: " + ex.getMessage()));
        }
        return null;
    }
    
}
