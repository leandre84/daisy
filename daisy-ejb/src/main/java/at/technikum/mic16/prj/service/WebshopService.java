/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.service;

import at.technikum.mic16.prj.dao.CategoryDAO;
import at.technikum.mic16.prj.dao.OrderItemDAO;
import at.technikum.mic16.prj.dao.PlacedOrderDAO;
import at.technikum.mic16.prj.dao.RecensionDAO;
import at.technikum.mic16.prj.dao.UserDAO;
import at.technikum.mic16.prj.dao.UserRoleDAO;
import at.technikum.mic16.prj.entity.Category;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author leandros
 */
@Named
@Stateless
@LocalBean
public class WebshopService {
    
    @Inject
    private CategoryDAO categoryDAO;
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
    
    private String test = "test";

    public String getTest() {
        return test;
    }
    
    public void persistanceTest() {
        Category elektronik = new Category("Elektronik");
        categoryDAO.persist(elektronik);
        Category fernseher = new Category("Fernseher");
        fernseher.setParent(elektronik);
        categoryDAO.persist(fernseher);
        
        Category find = categoryDAO.findByName("Fernseher");
        if (find != null) {
            System.out.println("Found Fernseher: " + find.toString());
        }
        
    }
    
    
   

}
