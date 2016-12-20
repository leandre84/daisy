/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Category;
import at.technikum.mic16.prj.entity.OrderItem;
import at.technikum.mic16.prj.entity.PlacedOrder;
import at.technikum.mic16.prj.entity.Product;
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
public class OrderItemDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager em;
    
    public OrderItem findByID(Long id) {
        return em.find(OrderItem.class, id);
    }

    public List<OrderItem> findByPlacedOrder(PlacedOrder order) {
        Query q = em.createQuery("SELECT o FROM OrderItem o WHERE PlacedOrder = :order", Category.class);
        q.setParameter("order", order);
        return q.getResultList();
    }
    
    public List<OrderItem> findByProduct(Product product) {
        Query q = em.createQuery("SELECT o FROM OrderItem o WHERE Product = :product", Category.class);
        q.setParameter("product", product);
        return q.getResultList();
    }
    
    public void persist(OrderItem orderItem) {
        em.persist(orderItem);
    }

    public void merge(OrderItem orderItem) {
        em.merge(orderItem);
    }

    public void delete(OrderItem orderItem) throws EntityNotFoundException {
        // attach and delete it...
        OrderItem attached = em.find(OrderItem.class, orderItem.getId());
        if (attached != null) {
            em.remove(attached);
        } else {
            throw new EntityNotFoundException("OrderItem not found with id: " + orderItem.getId());
        }

    }
    
    
    
}
