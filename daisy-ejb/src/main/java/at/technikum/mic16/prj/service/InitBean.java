/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.service;

import at.technikum.mic16.prj.dao.CategoryDAO;
import at.technikum.mic16.prj.dao.OrderItemDAO;
import at.technikum.mic16.prj.dao.PlacedOrderDAO;
import at.technikum.mic16.prj.dao.ProductDAO;
import at.technikum.mic16.prj.dao.RecensionDAO;
import at.technikum.mic16.prj.dao.UserDAO;
import at.technikum.mic16.prj.dao.UserRoleDAO;
import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.Product;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author leandros
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

    @Inject
    private CategoryDAO categoryDAO;
    @Inject
    private ProductDAO productDAO;
    @Inject
    private OrderItemDAO orderItemDAO;
    @Inject
    private PlacedOrderDAO placedOrderDAO;
    @Inject
    private RecensionDAO recensionDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private UserRoleDAO userRoleDAO;

    @PostConstruct
    public void init() {
        insertSampleData();
    }
    
    private void insertSampleData() {
        
        Category clothes = new Category("Clothes");
        categoryDAO.persist(clothes);
        
        Category electro = new Category("Electro");
        categoryDAO.persist(electro);
        
        Category telly = new Category("Television");
        telly.setParent(electro);
        categoryDAO.persist(telly);
        
        Category hoover = new Category("Hoover");
        hoover.setParent(electro);
        categoryDAO.persist(hoover);
        
        Category smartphone = new Category("Smartphone");
        smartphone.setParent(electro);
        categoryDAO.persist(smartphone);
        
        
        Product lg1 = new Product("LG VT100X60", 1999.99f, "This new LG is superb...", telly);
        Product lg2 = new Product("LG VT020F20", 1199.99f, "This new LG is not as good...", telly);
        Product hoover1 = new Product("AEG KL500F", 149.90f, "Brand new and strong...", hoover);
        
        productDAO.persist(lg1);
        productDAO.persist(lg2);
        productDAO.persist(hoover1);
    }

}
