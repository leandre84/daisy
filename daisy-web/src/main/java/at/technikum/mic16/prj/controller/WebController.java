package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.service.WebshopService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author leandros
 */
@Named(value = "webController")
@SessionScoped
public class WebController implements Serializable {
    
    @Inject
    WebshopService backend;

    public WebController() {
    }
    
    public String backendTest() {
        return backend.getTest();
    }
    
    public String test() {
        return "yo!";
    }
    
    public void persistanceTest() {
        backend.persistanceTest();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "gemacht, siehe JBoss log",  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
}