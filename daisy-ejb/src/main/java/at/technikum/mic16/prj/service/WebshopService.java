/*
 * To change this template, choose Tools | Templates
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
import java.util.List;
import javax.annotation.PostConstruct;
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
    
    private String test = "test";

    public String getTest() {
        return test;
    }
    
    public void persistanceTest() { 
    }
    
    public List<Category> getAllCategories() {
       return categoryDAO.findAll();
    }
   

}
