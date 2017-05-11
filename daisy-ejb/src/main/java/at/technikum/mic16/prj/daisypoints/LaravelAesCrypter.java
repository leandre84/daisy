package at.technikum.mic16.prj.daisypoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import org.apache.commons.codec.binary.Hex;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Handles encryption compatible with PHP/Laravel's routines for securely
 * generating encrypted messages to pass to daisy-points
 * @credit https://gist.github.com/KushtrimPacaj/43a383ab419fc222f80e
 * @author leandros
 */
public class LaravelAesCrypter {
    
    public static final String LARAVEL_CHARSET = "UTF-8";
    
    /**
     * Encrypts a string compatible with Laravel
     * @param keyValue key to be used for encryption
     * @param plaintext plaintext to encrypt
     * @return IV + ciphertext + MAC in Laravel's JSON format
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public static String encrypt(byte[] keyValue, String plaintext) throws
            UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        
        Key key = new SecretKeySpec(keyValue, "AES");
        /* no need to serialize in our Laravel version
        String serializedPlaintext = "s:" + plaintext.getBytes().length + ":\"" + plaintext + "\";";
        byte[] plaintextBytes = serializedPlaintext.getBytes("UTF-8");
        */
        byte[] plaintextBytes = plaintext.getBytes(LARAVEL_CHARSET);

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = c.getIV();
        byte[] encVal = c.doFinal(plaintextBytes);
        String encryptedData = Base64.encodeBase64String(encVal);

        SecretKeySpec macKey = new SecretKeySpec(keyValue, "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(macKey);
        hmacSha256.update(Base64.encodeBase64(iv));
        byte[] calcMac = hmacSha256.doFinal(encryptedData.getBytes(LARAVEL_CHARSET));
        String mac = new String(Hex.encodeHex(calcMac));

        LaravelAesModel aesData = new LaravelAesModel(
                Base64.encodeBase64String(iv),
                encryptedData,
                mac);
        
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        String aesDataJson = gson.toJson(aesData);

        return Base64.encodeBase64String(aesDataJson.getBytes(LARAVEL_CHARSET));
    }

    /*
    public static String decrypt(byte[] keyValue, String ivValue, String encryptedData, String macValue) throws Exception {
        Key key = new SecretKeySpec(keyValue, "AES");
        byte[] iv = Base64.decode(ivValue.getBytes("UTF-8"), Base64.DEFAULT);
        byte[] decodedValue = Base64.decode(encryptedData.getBytes("UTF-8"), Base64.DEFAULT);

        SecretKeySpec macKey = new SecretKeySpec(keyValue, "HmacSHA256");
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(macKey);
        hmacSha256.update(ivValue.getBytes("UTF-8"));
        byte[] calcMac = hmacSha256.doFinal(encryptedData.getBytes("UTF-8"));
        byte[] mac = Hex.decodeHex(macValue.toCharArray());
        if (!Arrays.equals(calcMac, mac))
            return "MAC mismatch";

        Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding"); // or PKCS5Padding
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decValue = c.doFinal(decodedValue);

        int firstQuoteIndex = 0;
        while (decValue[firstQuoteIndex] != (byte) '"') firstQuoteIndex++;
        return new String(Arrays.copyOfRange(decValue, firstQuoteIndex + 1, decValue.length - 2));
    }
*/

}