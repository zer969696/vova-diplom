package ru.vsu.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Created by Evgeniy Evzerov on 07.02.17.
 * VIstar
 */
public class AESHelper {

    private static final String ALGORITHM = "AES";
    private static byte[] keyValue;

    public AESHelper(byte[] keyValue) {

        AESHelper.keyValue = keyValue;
    }

    public String encrypt(String valueToEnc) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());

        return new BASE64Encoder().encode(encValue);
    }

    public String decrypt(String encryptedValue) throws Exception {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);

        return new String(decValue);
    }

    private static Key generateKey() throws Exception {

        Key key = new SecretKeySpec(keyValue, ALGORITHM);

        return key;
    }
}
