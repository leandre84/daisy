/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author leandros
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @ManyToOne(optional = true)
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Category> children = new ArrayList();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    /**
     * Sets parent also updating the parent's child
     * @param parent 
     */
    public void setParent(Category parent) {
        this.parent = parent;
        parent.getChildren().add(this);
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name=" + name + ", parent=" + (parent != null ? parent.getName() : "null") + ", children=" + children.toString() + '}';
    }
    
    /**
     * Check if category is leaf node
     * @return true if there are no child categories in this category, otherwise false
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    /**
     * Check if category is root node
     * @return  true if this category has no parent
     */
    public boolean isRoot() {
        return (parent == null);
    }
    
    /**
     * Gets the category's depth
     * @return the number of parent categories
     */
    public int depth() {
        int depth = 0;
        
        Category c = this;
        while ((c = c.getParent()) != null) {
            depth++;
        }
        
        return depth;    
    }
  
    
}
