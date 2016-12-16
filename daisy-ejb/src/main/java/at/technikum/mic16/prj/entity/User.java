/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author leandros
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "password", nullable = false)
    private String password;
    
    public User() {
        
    }
    
    public User(String id, String passwordHash) {
        this.id = id;
        this.password = passwordHash;
    }
    
    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

}
