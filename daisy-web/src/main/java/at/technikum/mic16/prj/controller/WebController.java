package at.technikum.mic16.prj.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.service.WebshopService;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author leandros
 */
@Named(value = "webController")
@SessionScoped
public class WebController implements Serializable {
    
    @EJB
    WebshopService backend;
    
    private List<Category> categories;
    private MenuModel menumodel;
    
    private Long selectedCategoryId;
    private Category selectedCategory;

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
     */
    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
        for (Category c : categories) {
            if (c.getId().compareTo(selectedCategoryId) == 0) {
                selectedCategory = c;
            }
        }
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    
    /**
     * Do some initialization stuff
     */
    @PostConstruct
    public void init() {
        categories = backend.getAllCategories();
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
                item.setCommand("#{webController.setSelectedCategoryId(" + c.getId() + ")}");
                parent.addElement(item);
            }
        }

    }
    
}
