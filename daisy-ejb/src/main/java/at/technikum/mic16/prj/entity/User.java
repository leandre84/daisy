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
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column
    private String description;
 
    
    public User() {
    }
    
    public User(String id, String passwordHash, String firstName, String lastName) {
        this.id = id;
        this.password = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String friendlyName() {
        return firstName + " " + lastName;
    }
    
    
    

}
