package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.OrderItem;
import at.technikum.mic16.prj.entity.PlacedOrder;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.service.WebshopService;
import at.technikum.mic16.prj.util.MessageUtil;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 * Backing bean for performing a shopping cart check out (checkout.xhtml)
 * @author leandros
 */
@ManagedBean(name = "checkoutController")
@RequestScoped
public class CheckoutController implements Serializable {
    
    @EJB
    private WebshopService backend;
    
    @ManagedProperty(value = "#{webController}")
    private WebController webController;
    
    @ManagedProperty(value = "#{loginController}")
    private LoginController loginController;
    
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    
    private Map<Product,Integer> cart;
    
    private String address;
    private String zip;
    private String town;
    private String country;
    

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

    @PostConstruct
    public void init() {
        cart = webController.getCart();
    }
    
    public List<Map.Entry<Product, Integer>> getCartEntries() {
        return webController.getCartEntries();
    }
    
    public float getCartTotal() {
        return webController.getCartTotal();
    }
    
    /**
     * Convert shopping cart to PlacedOrder instance and commit to to system
     */
    public void commitOrder() {
        PlacedOrder order = new PlacedOrder();
        Set<OrderItem> items = new HashSet<>();
        
        for (Map.Entry<Product,Integer> entry : cart.entrySet()) {
            OrderItem item = new OrderItem();
            Product product = entry.getKey();
            item.setProduct(product);
            item.setCount(entry.getValue());
            item.setPrice(product.getPrice());
            item.setOrder(order);
            items.add(item);
        }
        
        order.setOrderItems(items);
        order.updateTotal();
        order.setOrderDate(LocalDateTime.now());
        order.setPlacedBy(loginController.getUser());
        
        backend.commitOrder(order);
        
        webController.emptyCart();
        cart = webController.getCart();
        
        MessageUtil.putInfo("Thanks for your order!", "We appreciate it, really.");
        
        navigationController.setCurrentPage("products_overview.xhtml");
    }
    
    
    
}
