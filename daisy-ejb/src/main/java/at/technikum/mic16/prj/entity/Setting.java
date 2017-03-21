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
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author leandros
 */
@Entity
@Table(name = "setting")
public class Setting implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "setting_key")
    private String settingKey;
    
    @Column(name = "setting_value")
    private String settingValue;
    
    public Setting(String settingKey, String settingValue) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.settingKey);
        hash = 59 * hash + Objects.hashCode(this.settingValue);
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
        final Setting other = (Setting) obj;
        if (!Objects.equals(this.settingKey, other.settingKey)) {
            return false;
        }
        if (!Objects.equals(this.settingValue, other.settingValue)) {
            return false;
        }
        return true;
    }
    
    


    
    
    

}
