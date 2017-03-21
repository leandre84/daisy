package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.service.WebshopService;
import java.io.Serializable;
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
    
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    @PostConstruct
    public void init() {
        token = backend.getToken();
    }
    
    public String commitToken() {
        backend.setToken(token);
        return "index.xhtml?faces-redirect=true";
    }
    
    

}
