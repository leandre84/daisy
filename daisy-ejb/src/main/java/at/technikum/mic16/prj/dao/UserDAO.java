/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.User;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leandros
 */
@Model
public class UserDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager entityManager;
    
    public User findById(String id) {
        return entityManager.find(User.class, id);
    }

    public void persist(User user) {
        entityManager.persist(user);
    }

    public void delete(User user) {
        entityManager.remove(user);
    }
    
}
