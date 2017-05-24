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
import at.technikum.mic16.prj.entity.OrderItem;
import at.technikum.mic16.prj.entity.PlacedOrder;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
import at.technikum.mic16.prj.exception.TokenValidationException;
import at.technikum.mic16.prj.util.FileUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

/**
 *
 * @author leandros
 */
@Named
@Stateless
@LocalBean
public class WebshopService {
    
    public static final String TOKEN_FILE = "daisy.token";
    public static final int TOKEN_LENGTH = 32;
    
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
        
    
    public List<Category> getAllCategories() {
       return categoryDAO.findAll();
    }
    
    public List<Product> getAllProductsPaginated(int pagesize, int index) {
        return productDAO.findAll(index*pagesize, pagesize);
    }
    
    public List<Product> getProductsByNameOrDescription(String queryString) {
        return productDAO.findByNameOrDescription(queryString, -1, -1);
    }
    
    public List<Product> getProductsByExactName(String queryString) {
        return productDAO.findByExactName(queryString);
    }
    
    public List<Product> getProductsByCategory(Category category) {
        return productDAO.findByCategory(category, -1, -1);
    }
    
    public Product getProductById(Long id) {
        return productDAO.findByID(id);
    }
    
    public void persistInstallToken(String token) throws IOException, TokenValidationException {
        if (token == null || token.length() != 32) {
            throw new TokenValidationException("Token must be " + TOKEN_LENGTH + " characters long");
        }
        
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(TOKEN_FILE)));
            bw.write(token);
        } finally {
            FileUtil.safeClose(bw);
        }          
    }
    
    public String retrieveInstallationToken() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(TOKEN_FILE)));
            return br.readLine();
        } finally {
            FileUtil.safeClose(br);
        } 
    }
    
    public boolean deleteInstallationToken() {
        File f = new File(TOKEN_FILE);
        return f.delete();
    }
    
    public User authenticateUser(String userId, String passwordHash) {
        User user;
        try {
            user = userDAO.findByIDAndPassword(userId, passwordHash);
        } catch (NoResultException e) {
            return null;
        }
        return user;
    }
    
    public User getUserByID(String userId) {
        return userDAO.findById(userId);
    }
    
    public List<UserRole> getUserRoles(User user) {
        return userRoleDAO.findByUserID(user.getId());
    }
    
    public void createNewUser(String userId, String passwordHash, String firstName, String lastName) {
        UserRole newUserRole = new UserRole(userId, UserRole.Role.CUSTOMER);
        userRoleDAO.persist(newUserRole);
        User newUser = new User(userId, passwordHash, firstName, lastName);
        userDAO.persist(newUser);
    }
    
    public Recension getRecensionForProductByUser(Product product, User user) {
        Recension recension;
        try {
            recension = recensionDAO.findByUserAndProduct(user, product);
        } catch (NoResultException e) {
            return null;
        }
        return recension;
    }
    
    public void addOrModifyRecension(Recension recension) {
        recensionDAO.merge(recension);
    }
    
    public void commitOrder(PlacedOrder order) {
        placedOrderDAO.persist(order);
        for (OrderItem item : order.getOrderItems()) {
            orderItemDAO.persist(item);
        }
    }
    
}
