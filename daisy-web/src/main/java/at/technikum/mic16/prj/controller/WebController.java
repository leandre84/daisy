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
import java.util.List;
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
     * TODO: implement hierarchical model
     */
    private void constructCategoryMenu() {
        menumodel = new DefaultMenuModel();

        DefaultSubMenu submenu = new DefaultSubMenu();
        submenu.setLabel("Categories");

        for (Category c : categories) {
            if (!c.getChildren().isEmpty()) {
                // skip parent categories for now, see TODO
                continue;
            }
            DefaultMenuItem item = new DefaultMenuItem();
            item.setValue(c.getName());
            item.setCommand("#{webController.setSelectedCategoryId(" + c.getId() + ")}");
            submenu.addElement(item);
        }
        
        menumodel.addElement(submenu);

    }
    
}
