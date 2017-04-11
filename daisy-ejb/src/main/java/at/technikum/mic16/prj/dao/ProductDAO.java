/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Category;
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
    
    /**
     * Find all products, optionally paginated
     * @param offset Offset to result set
     * @param count Number of rows to return, a value <= 0 will result in pagination being disabled
     * @return 
     */
    public List<Product> findAll(int offset, int count) {
        Query q = em.createQuery("FROM Product p WHERE active is true", Product.class);
        if (count > 0) {
            q.setFirstResult(offset);
            q.setMaxResults(count);
        }
        return q.getResultList();
    }
    
     /**
     * Find all products matching substring in name or description
     * @param substring Substring to match
     * @param offset Offset to result set
     * @param count Number of rows to return, a value <= 0 will result in pagination being disabled
     * @return 
     */
    public List<Product> findByNameOrDescription(String substring, int offset, int count) {
        Query q = em.createQuery("FROM Product p WHERE (name like :substring or description like :substring) AND active is true", Product.class);
        q.setParameter("substring", "%" + substring + "%");
        if (count > 0) {
            q.setFirstResult(offset);
            q.setMaxResults(count);
        }
        
        return q.getResultList();
    }
    
    /**
     * Find all products - this is vulnerable to SQL injection
     * @param queryString
     * @return 
     */
    public List<Product> findByExactName(String queryString) {
        Query q = em.createQuery("FROM Product p WHERE active is true AND name = '" + queryString + "'", Product.class);
        return q.getResultList();
    }
    
     /**
     * Find all products matching specific category
     * @param category Category to match
     * @param offset Offset to result set
     * @param count Number of rows to return, a value <= 0 will result in pagination being disabled
     * @return 
     */
    public List<Product> findByCategory(Category category, int offset, int count) {
        Query q = em.createQuery("FROM Product p WHERE category_fk = :category AND active is true", Product.class);
        q.setParameter("category", category);
        if (count > 0) {
            q.setFirstResult(offset);
            q.setMaxResults(count);
        }
        return q.getResultList();
    }
    
    /**
     * Find inactive products
     * @return List of inactive products
     */
    public List<Product> findInactive() {
        Query q = em.createQuery("FROM Product p WHERE active is false", Product.class);
        return q.getResultList();
    }
    
    public void persist(Product...products) {
        for (Product product : products) {
            em.persist(product);
        }
    }

    public void merge(Product product) {
        em.merge(product);
    }

    public void delete(Product product) throws EntityNotFoundException {
        // attach and delete it...
        Product attached = em.find(Product.class, product.getId());
        if (attached != null) {
            em.remove(attached);
        } else {
            throw new EntityNotFoundException("Product not found with id: " + product.getId());
        }
    }
    
    
    
}
