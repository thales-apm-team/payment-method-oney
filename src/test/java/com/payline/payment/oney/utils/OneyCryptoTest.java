package com.payline.payment.oney.utils;

import com.payline.payment.oney.utils.chiffrement.OneyCrypto;
import org.junit.Assert;
import org.junit.Rule;

import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OneyCryptoTest {


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private OneyCrypto crypto;


   @Test
    public void encryptOK(){
       crypto = new OneyCrypto("66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

       String message = "mon message a chiffrer";
       String messageEncrypted = crypto.encrypt(message);

       Assert.assertNotEquals(message, messageEncrypted);
       Assert.assertEquals("w7wNbi0SYzTRnHmVN3tUUiSveqAUZVyttsUtRbXw8Mk=", messageEncrypted);
   }



    @Test
    public void decryptOK(){
        crypto = new OneyCrypto("66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        String messageEncrypted = "SE7ZT07NBY+cOL7B3/jLRdfFBH96VX2mMyxioBE7F8o=";
        String message = crypto.decrypt(messageEncrypted);

        Assert.assertNotEquals(message, messageEncrypted);
        Assert.assertEquals("mon message a dechiffrer", message);
    }


    @Test
    public void testEncryptDecrypt(){
        crypto = new OneyCrypto("66s581CG5W+RLEqZHAGQx+vskjy660Kt8x8rhtRpXtY=");

        String message = "test encrypt/decrypt {somekey : some_value}";
        String messageEncrypted = crypto.encrypt(message);
        String messageDecrypted = crypto.decrypt(messageEncrypted);


        Assert.assertNotEquals(message, messageEncrypted);
        Assert.assertNotEquals(messageEncrypted, messageDecrypted);
        Assert.assertEquals(message, messageDecrypted);

    }

    @Test
    public void encryptWrongKey(){
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Unable to encrypt this message");
        crypto = new OneyCrypto("maCle");

        String message = "mon message a chiffrer";
        String messageEncrypted = crypto.encrypt(message);

    }




}
