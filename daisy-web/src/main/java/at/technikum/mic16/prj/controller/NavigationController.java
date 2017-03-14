package at.technikum.mic16.prj.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




/**
 *
 * @author leandros
 */
@ManagedBean(name = "navigationController")
@SessionScoped
public class NavigationController implements Serializable {
    
    // we start with the overview page
    private String currentPage = "products_overview.xhtml";
    private String previousPage;
    
    public NavigationController() {
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        previousPage = this.currentPage;
        this.currentPage = currentPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }


 
}
