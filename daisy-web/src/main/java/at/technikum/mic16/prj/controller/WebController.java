package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.service.WebshopService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author leandros
 */
@Named(value = "webController")
@SessionScoped
public class WebController implements Serializable {
    
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    @EJB
    WebshopService backend;
    
    private List<Category> categories = new ArrayList<>();
    private MenuModel menumodel;
    private Long selectedCategoryId;
    private Category selectedCategory;
    private List<Product> displayedProducts = new ArrayList<>();
    private String searchText = "";
    private Product selectedProduct;
    
    private boolean viewAbandoned = false;

    public WebController() {
    }

    public List<Category> getCategories() {
        return categories;
    }

    public MenuModel getMenumodel() {
        return menumodel;
    }

    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    /**
     * Sets the selectedCategoryId and updates selectedCategory by passed id
     * @param selectedCategoryId 
     * @return Where to navigate next
     */
    public String setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
        boolean modified = false;
        for (Category c : categories) {
            if (c.getId().compareTo(selectedCategoryId) == 0) {
                selectedCategory = c;
                modified = true;
            }
        }
        // Fetch products based on selected category
        if (modified) {
            displayedProducts = backend.getProductsByCategory(selectedCategory);
        }
        
        if (viewAbandoned) {
            viewAbandoned = false;
            return "index.xhtml?faces-redirect=true";
        }
        return null;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public List<Product> getDisplayedProducts() {
        return displayedProducts;
    }

    public void setDisplayedProducts(List<Product> displayedProducts) {
        this.displayedProducts = displayedProducts;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
    

    
    /**
     * Do some initialization stuff
     */
    @PostConstruct
    public void init() {
        categories = backend.getAllCategories();
        displayedProducts = backend.getAllProductsPaginated(DEFAULT_PAGE_SIZE, 0);
        constructCategoryMenu();        
    }
    
    /**
     * Construct the category menu
     * methodology relies on sub-categories being returned by database first
     */
    private void constructCategoryMenu() {
        menumodel = new DefaultMenuModel();        
        Map<String,DefaultSubMenu> createdSubMenus = new HashMap<>();
        
        for (Category c : categories) {
            if (c.isRoot()) {
                // Root - create submenu
                DefaultSubMenu sub = new DefaultSubMenu();
                sub.setLabel(c.getName());
                menumodel.addElement(sub);
                createdSubMenus.put(c.getName(), sub);
            } else if (! c.isLeaf()) {
                // Intermediate - create submenu and find parent to register as child category
                DefaultSubMenu parent = createdSubMenus.get(c.getParent().getName());
                DefaultSubMenu sub = new DefaultSubMenu();
                sub.setLabel(c.getName());
                parent.addElement(sub);
                createdSubMenus.put(c.getName(), sub);
            } else {
                // Leaf - find parent and register as child menu item
                DefaultSubMenu parent = createdSubMenus.get(c.getParent().getName());
                DefaultMenuItem item = new DefaultMenuItem();
                item.setValue(c.getName());
                // Invoke selectedCategoryId - this also updates displayedProducts
                item.setCommand("#{webController.setSelectedCategoryId(" + c.getId() + ")}");
                parent.addElement(item);
            }
        }

    }
    
    public String searchForProducts() {
        if (searchText == null || searchText.isEmpty() || searchText.replaceAll(" ", "").isEmpty()) {
            // TODO: handle search without criteria
            return null;
        }
        displayedProducts = backend.getProductsByNameOrDescription(searchText);
        if (viewAbandoned) {
            viewAbandoned = false;
            return "index.xhtml?faces-redirect=true";
        }
        return null;
    }
    
    public static String getRatingImageForProduct(Product product) {
        // get rating, descard floating part and return URL        
        return "images/daisy_" + Integer.toString((int) product.averageRating()) + ".png";
    }
    
    public String navigateToProductDetail() {
        viewAbandoned = true;
        return "product.xhtml";
    }
    
    public void createNewUser(String userId, String password, String firstName, String lastName) {
        backend.createNewUser(userId, password, firstName, lastName);
    }
    
}
