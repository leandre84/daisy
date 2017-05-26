package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.PlacedOrder;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.service.WebshopService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 *
 * @author leandros
 */
@ManagedBean(name = "ordersController")
@RequestScoped
public class OrdersController implements Serializable {
    
    @EJB
    private WebshopService backend;
    
    @ManagedProperty(value = "#{webController}")
    private WebController webController;
    
    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;
    
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    
    private List<PlacedOrder> orders;
    

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

    public List<PlacedOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<PlacedOrder> orders) {
        this.orders = orders;
    }

    
    @PostConstruct
    public void init() {
        orders = backend.getOrdersOfUser(loginController.getUser());
    }

    
}
