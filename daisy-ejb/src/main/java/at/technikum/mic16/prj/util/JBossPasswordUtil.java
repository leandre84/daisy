/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


/**
 *
 * @author leandros
 */
public class JBossPasswordUtil {

    public static String getPasswordHash(String password) throws UnsupportedEncodingException {        
        MessageDigest sha256 = DigestUtils.getSha256Digest();
        return Base64.encodeBase64String(sha256.digest(password.getBytes()));
    }

}
