/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leandros
 */
@Model
public class ProductDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager em;
    
    public Product findByID(Long id) {
        return em.find(Product.class, id);
    }
    
    public void persist(Product product) {
        em.persist(product);
    }

    public void merge(Product product) {
        em.merge(product);
    }

    public void delete(Product product) throws EntityNotFoundException {
        // attach and delete it...
        Recension attached = em.find(Recension.class, product.getId());
        if (attached != null) {
            em.remove(attached);
        } else {
            throw new EntityNotFoundException("Product not found with id: " + product.getId());
        }

    }
    
    
    
}
