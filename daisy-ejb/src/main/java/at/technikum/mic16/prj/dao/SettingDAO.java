/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.dao;

import at.technikum.mic16.prj.entity.Setting;
import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leandros
 */
@Model
public class SettingDAO {

    @PersistenceContext(unitName = "daisy-persunit")
    private EntityManager entityManager;

    public Setting findById(String id) {
        return entityManager.find(Setting.class, id);
    }

    public void persist(Setting... settings) {
        for (Setting setting : settings) {
            entityManager.persist(setting);
        }
    }

    public void merge(Setting setting) {
        entityManager.merge(setting);
    }

    public void delete(Setting setting) {
        entityManager.remove(setting);
    }

}
