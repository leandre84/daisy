/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author leandros
 */
@Entity
@Table(name = "order_item")
public class OrderItem {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private float price;
    
    @Column(nullable = false)
    private int count;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_fk")
    private Product product;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_fk")
    private PlacedOrder order;

    public Long getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PlacedOrder getOrder() {
        return order;
    }

    public void setOrder(PlacedOrder order) {
        this.order = order;
    }
    
    
    
}
