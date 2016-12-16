/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.service;

import at.technikum.mic16.prj.dao.CategoryDAO;
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
    private CategoryDAO categoryDao;
    
    private String test = "test";

    public String getTest() {
        return test;
    }
    
    public void persistanceTest() {
        Category elektronik = new Category("Elektronik");
        categoryDao.persist(elektronik);
        Category fernseher = new Category("Fernseher");
        fernseher.setParent(elektronik);
        categoryDao.persist(fernseher);
        
        Category find = categoryDao.findByName("Fernseher");
        if (find != null) {
            System.out.println("Found Fernseher: " + find.toString());
        }
        
    }
    
    
   

}
