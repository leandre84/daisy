/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
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
public class ProductDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager em;
    
    public Product findByID(Long id) {
        return em.find(Product.class, id);
    }
    
    public List<Product> findAllPaginated(int offset, int count) {
        Query q = em.createQuery("FROM Product p", Product.class);
        q.setFirstResult(offset);
        q.setMaxResults(count);
        return q.getResultList();
    }
    
    public List<Product> findByNameOrDescription(String substring) {
        Query q = em.createQuery("FROM Product p WHERE name like :substring or description like :substring", Product.class);
        q.setParameter("substring", "%" + substring + "%");
        return q.getResultList();
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
