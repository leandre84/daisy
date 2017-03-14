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
import at.technikum.mic16.prj.entity.User;
import at.technikum.mic16.prj.entity.UserRole;
import java.util.List;
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
    
    public User authenticateUser(String userId, String passwordHash) {
        return userDAO.findByIDAndPassword(userId, passwordHash);
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
    
}
