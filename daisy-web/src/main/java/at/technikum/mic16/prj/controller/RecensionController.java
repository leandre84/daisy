package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.service.WebshopService;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 * Backing bean for adding or modifying a product's recension related to a user (recension.xhtml)
 * @author leandros
 */
@ManagedBean(name = "recensionController")
@RequestScoped
public class RecensionController implements Serializable {
    
    @EJB
    private WebshopService backend;
    
    @ManagedProperty(value = "#{webController}")
    private WebController webController;
    
    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;
    
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    
    /* this may be fetched from other beans but we have theme here as well for convenience */
    private User user;
    private Product product;
    private Recension recension;

    public WebController getWebController() {
        return webController;
    }

    public void setWebController(WebController webController) {
        this.webController = webController;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Recension getRecension() {
        return recension;
    }

    public void setRecension(Recension recension) {
        this.recension = recension;
    }

    @PostConstruct
    public void init() {
        user = loginController.getUser();
        product = webController.getSelectedProduct();       
        
        Recension current = backend.getRecensionForProductByUser(product, user);
        if (current != null) {
            recension = current;
        } else {
            recension = new Recension();
            recension.setCreationDate(LocalDateTime.now());
            recension.setProduct(product);
            recension.setUser(user);
        }
    }
    
    public void addOrModifyRecension() {
        backend.addOrModifyRecension(recension);
        /*
        we have updated a single recension but this will not be reflected in the webController's view.
        for sure refreshing stale entity instances could be done more elegantly than what we do next...
        */
        webController.setSelectedProduct(backend.getProductById(product.getId()));
        navigationController.setCurrentPage(navigationController.getPreviousPage());
    }
    
    
    
}
