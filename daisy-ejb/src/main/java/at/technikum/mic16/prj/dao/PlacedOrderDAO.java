/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.PlacedOrder;
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
public class PlacedOrderDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager em;
    
    public PlacedOrder findByID(Long id) {
        return em.find(PlacedOrder.class, id);
    }

    public List<PlacedOrder> findByUser(User user) {
        if (user == null) {
            return null;
        }
        Query q = em.createQuery("SELECT p FROM PlacedOrder p WHERE user_fk = :user", PlacedOrder.class);
        q.setParameter("user", user.getId());
        return q.getResultList();
    }
    
    public void persist(PlacedOrder placedOrder) {
        em.persist(placedOrder);
    }

    public void merge(PlacedOrder placedOrder) {
        em.merge(placedOrder);
    }

    public void delete(PlacedOrder placedOrder) throws EntityNotFoundException {
        // attach and delete it...
        PlacedOrder attached = em.find(PlacedOrder.class, placedOrder.getId());
        if (attached != null) {
            em.remove(attached);
        } else {
            throw new EntityNotFoundException("PlacedOrder not found with id: " + placedOrder.getId());
        }

    }
    
    
    
}
