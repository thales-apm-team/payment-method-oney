package com.payline.payment.oney.service.impl.request;

import com.payline.payment.oney.utils.chiffrement.OneyCrypto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;

public abstract class OneyRequest {
    //Construit une requête Oney

    protected static final Logger logger = LogManager.getLogger(OneyRequest.class);

    protected String  merchantGuid;
    protected String  pspGuid;
    protected String  encryptedMessage;
    protected String  decryptedMessage;


    public String getMerchantGuid() {
        return merchantGuid;
    }

    public String getPspGuid() {
        return pspGuid;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    /**
     * Encrypt a request  message
     * @param toEncrypt, the String to encrypt
     * @param key, String,  the chiffrement key
     * @return
     */
    public static String encryptMessage(String toEncrypt, String key) {
        if (toEncrypt == null ){
            logger.info("Message to encrypt is empty");
            toEncrypt = "";
        }

        OneyCrypto crypto = new OneyCrypto(key);
        return  crypto.encrypt(toEncrypt);
    }

    /**
     * Decrypt a request  message
     * @param toDecrypt, the String to decrypt
     * @param key, String,  the chiffrement key
     * @return
     */
    public static String decryptMessage(String toDecrypt, String key) {
        if (toDecrypt == null || toDecrypt.equals("")){
            logger.error("Message to decrypt is empty");
            return "";
        }

        OneyCrypto crypto = new OneyCrypto(key);
        return  crypto.decrypt(toDecrypt);

    }

}
