package com.payline.payment.oney.utils.chiffrement;

import com.payline.payment.oney.exception.DecryptException;
import com.payline.payment.oney.exception.InvalidDataException;
import com.payline.payment.oney.exception.PluginTechnicalException;
import com.payline.payment.oney.utils.OneyConstants;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class OneyCrypto {

    private static final Logger LOGGER = LogManager.getLogger(OneyCrypto.class);

    //Symetric key, used to encrypt & decrypt message
    private String key;


    public OneyCrypto(String key) {
        this.key = key;
    }


    /**
     * decrypt message with symetric key
     *
     * @param messageToEncrypt String, message to encrypt
     * @return String, the decrypted message
     */
    public String encrypt(String messageToEncrypt) throws DecryptException {
        try {
            // Initialise a cipher
            // Convert the key to SecretKeySpec
            byte[] decodedKey = DatatypeConverter.parseBase64Binary(this.key);
            SecretKeySpec oneySecret = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");


            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, oneySecret);
            // Make the encryption
            byte[] decryptedBytes = messageToEncrypt.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = cipher.doFinal(decryptedBytes);
            return DatatypeConverter.printBase64Binary(encryptedBytes);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new DecryptException(e, "OneyCrypto.encrypt." + e.getClass().getSimpleName());
        }
    }


    /**
     * decrypt message with symetric key
     *
     * @param messageEncrypted String, message to decrypt
     * @return String, the decrypted message
     */
    public String decrypt(String messageEncrypted) throws DecryptException {
        // Convert the key to SecretKeySpec
        byte[] decodedKey = DatatypeConverter.parseBase64Binary(this.key);
        SecretKeySpec oneySecret = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // Define variables
        byte[] encryptedMessage = DatatypeConverter.parseBase64Binary(messageEncrypted);

        try {
            // Initialise a cipher
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, oneySecret); // Make the decryption
            byte[] decryptedBytes = cipher.doFinal(encryptedMessage);
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new DecryptException(e, "OneyCrypto.decrypt." + e.getClass().getSimpleName());
        }
    }


    /**
     * Encrypt a request  message
     *
     * @param toEncrypt, the String to encrypt
     * @param key,       String,  the chiffrement key
     * @return ecrypted message
     */
    public static String encryptMessage(String toEncrypt, String key) throws PluginTechnicalException {
        if (key == null || key.isEmpty()) {
            throw new InvalidDataException("La clé de chiffrement ne peut pas être nulle", OneyConstants.PARTNER_CHIFFREMENT_KEY);
        }

        if (toEncrypt == null) {
            LOGGER.warn("Message to encrypt is empty");
            toEncrypt = "";
        }

        OneyCrypto crypto = new OneyCrypto(key);
        return crypto.encrypt(toEncrypt);
    }

}
