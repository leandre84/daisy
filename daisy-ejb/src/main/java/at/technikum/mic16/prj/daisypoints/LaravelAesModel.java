/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.daisypoints;

/**
 * Holds encrypted data pertinent to communication with points system
 * @author leandros
 */
public class LaravelAesModel {
    
    public String iv; // base64
    public String value; // base64
    public String mac; // hexadecimal

    public LaravelAesModel(String iv, String value, String mac) {
        this.iv = iv;
        this.value = value;
        this.mac = mac;
    }

}
