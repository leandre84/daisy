/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author leandros
 */
@Entity
@Table(name = "placed_order")
public class PlacedOrder implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_fk")
    private User placedBy;
    
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;
    
    @Column(nullable = false)
    private float total;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderItem> orderItems;
    

    public Long getId() {
        return id;
    }
    
    public User getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(User placedBy) {
        this.placedBy = placedBy;
    }
    
    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    
    public void updateTotal() {
        float newTotal = 0;
        for (OrderItem item : orderItems) {
            newTotal += item.getCount() * item.getPrice();
        }
        total = newTotal;
    }

            
    
}
