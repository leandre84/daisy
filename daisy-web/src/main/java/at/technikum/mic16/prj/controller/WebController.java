package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.service.WebshopService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author leandros
 */
@ManagedBean(name = "webController")
@SessionScoped
public class WebController implements Serializable {
    
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    @EJB
    private WebshopService backend;
    
    @ManagedProperty(value = "#{navigationController}")
    private NavigationController navigationController;
    
    private List<Category> categories = new ArrayList<>();
    private MenuModel menumodel;
    private Long selectedCategoryId;
    private Category selectedCategory;
    private List<Product> displayedProducts = new ArrayList<>();
    private String searchText = "";
    private String searchMode = "fuzzy";
    private Product selectedProduct;
    private Map<Product,Integer> cart = new HashMap<>();

    public WebController() {
    }

    public WebshopService getBackend() {
        return backend;
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public void setNavigationController(NavigationController navigationController) {
        this.navigationController = navigationController;
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
     */
    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
        /* programatic update not working in this case
        searchText = "";
        RequestContext.getCurrentInstance().update("tv_search");
        */
                
        boolean modified = false;
        for (Category c : categories) {
            if (c.getId().compareTo(selectedCategoryId) == 0) {
                selectedCategory = c;
                modified = true;
            }
        }
        // Fetch products based on selected category
        if (modified) {
            selectedProduct = null;
            displayedProducts = backend.getProductsByCategory(selectedCategory);
            navigationController.setCurrentPage("products_overview.xhtml");
        }
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

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Map<Product, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<Product, Integer> cart) {
        this.cart = cart;
    }
    
    

    
    /**
     * Do some initialization stuff
     */
    @PostConstruct
    public void init() {
        categories = backend.getAllCategories();
        displayedProducts = backend.getAllProductsPaginated(-1, -1);
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
                sub.setExpanded(true);
                menumodel.addElement(sub);
                createdSubMenus.put(c.getName(), sub);
            } else if (! c.isLeaf()) {
                // Intermediate - create submenu and find parent to register as child category
                DefaultSubMenu parent = createdSubMenus.get(c.getParent().getName());
                DefaultSubMenu sub = new DefaultSubMenu();
                sub.setLabel(c.getName());
                sub.setExpanded(true);
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
    
    public void searchForProducts() {
        if (searchText == null || searchText.isEmpty() || searchText.replaceAll(" ", "").isEmpty()) {
            // TODO: handle search without criteria
            return;
        }
        selectedProduct = null;
        // fetch products using vulnerable routine in case of exact match
        if (searchMode.equals("exact")) {
            displayedProducts = backend.getProductsByExactName(searchText);
        } else {
            displayedProducts = backend.getProductsByNameOrDescription(searchText);
        }
        navigationController.setCurrentPage("products_overview.xhtml");
    }
    
    public static String getRatingImageForProduct(Product product) {
        // get rating, descard floating part and return URL        
        return "images/daisy_" + Integer.toString((int) product.averageRating()) + ".png";
    }
    
    public static String getRatingImageForRecension(Recension recension) {        
        return "images/daisy_" + Integer.toString(recension.getRating()) + ".png";
    }
    
    /**
     * Add product to shopping cart
     * @param product Product to be added
     */
    public void addToCart(Product product) {
        Integer quantity = cart.get(product);
        if (quantity == null) {
            cart.put(product, 1);
        } else {
            cart.put(product, ++quantity);
        }
    }
    
    /**
     * Adjust quantity of product in shopping cart
     * @param product Product's quantity to adjust
     * @param quantity New quantity. If value less than one, product is discarded from shopping cart
     */
    public void adjustQuantityOfProductInCart(Product product, int quantity) {
        if (quantity < 1) {
            cart.remove(product);
        } else {
            cart.put(product, quantity);
        }
    }
    
    /**
     * Get the number of individual items in shopping cart
     * @return Number of individual items in shopping cart
     */
    public int getCartItemCount() {
        int count = 0;
        for (Map.Entry<Product,Integer> entry : cart.entrySet()) {
            count += entry.getValue();
        }
        return count;
    }
    
    public List<Entry<Product, Integer>> getCartEntries() {
        return new ArrayList<>(cart.entrySet());
    }
    
    public float getCartTotal() {
        float total = 0;
        for (Map.Entry<Product,Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
    
    public void emptyCart() {
        cart = new HashMap<>();
    }
    
}
