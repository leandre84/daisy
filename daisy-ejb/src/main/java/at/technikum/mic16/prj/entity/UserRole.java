/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author leandros
 */
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {
    
    public enum Role {
        ADMIN,
        CUSTOMER
    }
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;
    
    public UserRole() {
        
    }
    
    public UserRole(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.userId);
        hash = 19 * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserRole other = (UserRole) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (this.role != other.role) {
            return false;
        }
        return true;
    }

    
    
    
    
}
