/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Product;
import at.technikum.mic16.prj.entity.Recension;
import at.technikum.mic16.prj.entity.User;
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
public class RecensionDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager em;
    
    public Recension findByID(Long id) {
        return em.find(Recension.class, id);
    }

    public List<Recension> findByUser(User user) {
        Query q = em.createQuery("SELECT r FROM Recension r WHERE User = :user", Recension.class);
        q.setParameter("user", user);
        return q.getResultList();
    }
    
    public List<Recension> findByProduct(Product product) {
        Query q = em.createQuery("SELECT r FROM Recension r WHERE Product = :product", Recension.class);
        q.setParameter("product", product);
        return q.getResultList();
    }
    
    public Recension findByUserAndProduct(User user, Product product) {
        Query q = em.createQuery("SELECT r FROM Recension r WHERE User = :user AND Product = :product", Recension.class);
        q.setParameter("user", user);
        q.setParameter("product", product);
        return (Recension) q.getSingleResult();
    }
    
    public void persist(Recension...recensions) {
        for (Recension recension : recensions) {
            em.persist(recension);
        }
    }

    public void merge(Recension recension) {
        em.merge(recension);
    }

    public void delete(Recension recension) throws EntityNotFoundException {
        // attach and delete it...
        Recension attached = em.find(Recension.class, recension.getId());
        if (attached != null) {
            em.remove(attached);
        } else {
            throw new EntityNotFoundException("Recension not found with id: " + recension.getId());
        }

    }
    
    
    
}
