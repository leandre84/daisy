/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.technikum.mic16.prj.daisypoints;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Facilitates generation of encrypted messages for daisy-points
 * @author leandros
 */
public class DaisyPointsCrypter {
    
    private static final String SHARED_SECRET = "abc";
    
    public static String encryptMessage(String installationToken,
            String message) throws DaisyPointsEncryptionException {
        try {
            String preKey = installationToken.concat(SHARED_SECRET);
            byte[] key = DigestUtils.getMd5Digest().digest(preKey.getBytes(LaravelAesCrypter.LARAVEL_CHARSET));
            return LaravelAesCrypter.encrypt(key, message);
        } catch (UnsupportedEncodingException | InvalidKeyException |
                NoSuchAlgorithmException | BadPaddingException
                | IllegalBlockSizeException | NoSuchPaddingException ex) {
            throw new DaisyPointsEncryptionException(ex.getMessage());
        }
    }
    
}
