/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.UserRole;
import java.util.List;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leandros
 */
@Model
public class UserRoleDAO {
    
    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager entityManager;

    public UserRole findByID(String userId, String role) {
        Query q = entityManager.createQuery("select ur from UserRole ur where "
                + "ur.userId = :userId and ur.role = :role", UserRole.class);
        q.setParameter("userId", userId);
        q.setParameter("role", role);
        return (UserRole) q.getSingleResult();
    }
    
    public List<UserRole> findByUserID(String userId) {
        Query q = entityManager.createQuery("select ur from UserRole ur where "
                + "ur.userId = :userId", UserRole.class);
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    public void persist(UserRole userRole) {
        entityManager.persist(userRole);
    }

    public void delete(UserRole userRole) {
        entityManager.remove(userRole);
    }
    
}
