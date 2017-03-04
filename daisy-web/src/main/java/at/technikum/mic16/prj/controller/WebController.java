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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

    public void setSelectedCategoryId(Long selectedCategoryId) {
        System.out.println("Setting selected category to: " + selectedCategoryId);
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


    public String backendTest() {
        return backend.getTest();
    }
    
    public String test() {
        return "yo!";
    }
    
    
    public void persistanceTest() {
        backend.persistanceTest();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "gemacht, siehe JBoss log",  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    @PostConstruct
    public void init() {
        categories = backend.getAllCategories();
        
        menumodel = new DefaultMenuModel();
        
        DefaultSubMenu submenu = new DefaultSubMenu();
        submenu.setLabel("Foo");
        
        DefaultMenuItem item1 = new DefaultMenuItem();
        item1.setValue(categories.get(0).getName());
        item1.setCommand("#{webController.setSelectedCategoryId(" + categories.get(0).getId() + ")}");
        submenu.addElement(item1);
        
        DefaultMenuItem item2 = new DefaultMenuItem();
        item2.setValue(categories.get(1).getName());
        item2.setCommand("#{webController.setSelectedCategoryId(" + categories.get(1).getId() + ")}");
        submenu.addElement(item2);
        
        menumodel.addElement(submenu);
    }
    
}
