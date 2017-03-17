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
        
        UserRole user1Role = new UserRole("user1@foo.at", UserRole.Role.CUSTOMER);
        UserRole user2Role = new UserRole("user2@foo.at", UserRole.Role.CUSTOMER);
        userRoleDAO.persist(user1Role, user2Role);
        
        User user1 = new User("user1@foo.at", JBossPasswordUtil.getPasswordHash("user1"), "User", "1");
        User user2 = new User("user2@foo.at", JBossPasswordUtil.getPasswordHash("user2"), "User", "2");
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
        
        
        Product phillips1 = new Product("Philips 55PUK4900", 679.99f, "This new Phillips is superb...", "/images/products/Phillips_55PUK4900.jpg" ,telly);
        Product phillips2 = new Product("Phillips 55PUS6031", 998.99f, "This new Phillips is not as good...", "/images/products/Phillips_55PUS6031.jpg",telly);
        Product samsung1 = new Product("Samsung UE55JU6470", 850.00f, "This new Samsung is superb...", "/images/products/Samsung_UE55JU6470.jpg" ,telly);
        Product samsung2 = new Product("Samsung UE55K5660", 1100.00f, "This new Samsung is not as good...", "/images/products/Samsung_UE55K5650.jpg",telly);
        Product samsung3 = new Product("Samsung UE65JU6070", 1200.99f, "This new Samsung is not as good...", "/images/products/Samsung_UE65JU6070.jpg",telly);
        Product hoover1 = new Product("AEG KL500F", 149.90f, "Brand new and strong...", "",hoover);
        
        productDAO.persist(phillips1, phillips2,samsung1,samsung2,samsung3, hoover1);
        
        Recension recension1 = new Recension();
        recension1.setCreationDate(LocalDate.now().minusDays(14));
        recension1.setProduct(phillips1);
        recension1.setRating(4);
        recension1.setUser(user1);
        recension1.setText("I like it");
        
        Recension recension2 = new Recension();
        recension2.setCreationDate(LocalDate.now().minusDays(3));
        recension2.setProduct(phillips1);
        recension2.setRating(3);
        recension2.setUser(user2);
        recension2.setText("It's ok, don't expect too much.");
        
        recensionDAO.persist(recension1, recension2);
        
        
    }

}
