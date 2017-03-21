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
import at.technikum.mic16.prj.dao.SettingDAO;
import at.technikum.mic16.prj.dao.UserDAO;
import at.technikum.mic16.prj.dao.UserRoleDAO;
import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.Setting;
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
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
    
    public static final String SETTING_TOKEN_COLUMN = "daisy.token";
    
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
    @Inject
    private SettingDAO settingDAO;
    
    
    public List<Category> getAllCategories() {
       return categoryDAO.findAll();
    }
    
    public List<Product> getAllProductsPaginated(int pagesize, int index) {
        return productDAO.findAll(index*pagesize, pagesize);
    }
    
    public List<Product> getProductsByNameOrDescription(String queryString) {
        return productDAO.findByNameOrDescription(queryString, -1, -1);
    }
    
    public List<Product> getProductsByCategory(Category category) {
        return productDAO.findByCategory(category, -1, -1);
    }
    
    public void setToken(String token) {
        Setting newToken = new Setting(SETTING_TOKEN_COLUMN, token);
        settingDAO.persist(newToken);
    }
    
    public String getToken() {
        String token = "";
        try {
            token = settingDAO.findById(SETTING_TOKEN_COLUMN).getSettingValue();
        } catch (Exception e) {
            // implement me
        }
        return token;
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
        /*
        Recension recension;
        try {
            recension = recensionDAO.findByUserAndProduct(user, product);
        } catch (NoResultException e) {
            return null;
        }
        return recension;
        */
        return recensionDAO.findByUserAndProduct(user, product);
    }
    
}
