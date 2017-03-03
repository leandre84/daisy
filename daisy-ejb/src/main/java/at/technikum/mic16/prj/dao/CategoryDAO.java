/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Category;
import java.util.List;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leandros
 */
@Model
public class CategoryDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager em;
    
    public Category findByID(Long id) {
        return em.find(Category.class, id);
    }

    public Category findByName(String name) {
        Query q = em.createQuery("SELECT c FROM Category c WHERE name = :name", Category.class);
        q.setParameter("name", name);
        return (Category) q.getSingleResult();
    }
    
    public List<Category> findAll() {
        Query q = em.createQuery("SELECT c FROM Category c", Category.class);
        return q.getResultList();
    }
    
    public void persist(Category category) {
        em.persist(category);
    }

    public void merge(Category category) {
        em.merge(category);
    }
    
    

    public void delete(Category category) throws EntityNotFoundException {
        // attach and delete it...
        Category attached = em.find(Category.class, category.getId());
        if (attached != null) {
            em.remove(attached);
        } else {
            throw new EntityNotFoundException("Category not found with id: " + category.getId());
        }

    }
    
    
    
}
