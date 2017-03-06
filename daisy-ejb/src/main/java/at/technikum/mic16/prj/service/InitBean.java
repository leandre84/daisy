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
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
import at.technikum.mic16.prj.util.JBossPasswordUtil;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        try {
            insertSampleData();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(InitBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void insertSampleData() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        UserRole user1Role = new UserRole("user1", UserRole.Role.CUSTOMER);
        UserRole user2Role = new UserRole("user2", UserRole.Role.CUSTOMER);
        userRoleDAO.persist(user1Role, user2Role);
        
        User user1 = new User("user1", JBossPasswordUtil.getPasswordHash("user1"));
        User user2 = new User("user2", JBossPasswordUtil.getPasswordHash("user2"));
        userDAO.persist(user1, user2);
        
        Category clothes = new Category("Clothes");
        categoryDAO.persist(clothes);
        
        Category men = new Category("Men");
        men.setParent(clothes);
        categoryDAO.persist(men);
        
        Category trousersMen = new Category("Trousers");
        trousersMen.setParent(men);
        categoryDAO.persist(trousersMen);
                
        
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
        
        
        Product lg1 = new Product("LG VT100X60", 1999.99f, "This new LG is superb...", "/images/products/vintage-tv.jpg" ,telly);
        Product lg2 = new Product("LG VT020F20", 1199.99f, "This new LG is not as good...", "",telly);
        Product hoover1 = new Product("AEG KL500F", 149.90f, "Brand new and strong...", "",hoover);
        
        productDAO.persist(lg1, lg2, hoover1);
        
        Recension recension1 = new Recension();
        recension1.setCreationDate(LocalDate.now().minusDays(14));
        recension1.setProduct(lg1);
        recension1.setRating(3);
        recension1.setUser(user1);
        recension1.setText("I like it");
        
        recensionDAO.persist(recension1);
        
        
    }

}
