package ru.vsu.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Evgeniy Evzerov on 06.02.17.
 * VIstar
 */



public class Person implements Serializable {


    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private PrivateKey privateKey;
    private PublicKey  publicKey;
    private PublicKey  receivedPublicKey;
    private byte[]     secretKey;
    private String     secretMessage;

    private String     IV = "abcdef12345600dacdef94756eeabefa";

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();


    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void encryptAndSendMessage(final String message, final Person person) {

        try {
            // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            final byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            person.receiveAndDecryptMessage(encryptedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void generateCommonSecretKey() {

        try {
            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(receivedPublicKey, true);

            secretKey = shortenSecretKey(keyAgreement.generateSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void generateKeys() {

        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(1024);

            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey  = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public PublicKey getPublicKey() {

        return publicKey;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void receiveAndDecryptMessage(final byte[] message) {

        try {

            // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            secretMessage = new String(cipher.doFinal(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * In a real life example you must serialize the public key for transferring.
     *
     * @param  person
     */
    public void receivePublicKeyFrom(final Person person) {

        receivedPublicKey = person.getPublicKey();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void whisperTheSecretMessage() {

        System.out.println(secretMessage);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * 1024 bit symmetric key size is so big for DES so we must shorten the key size. You can get first 8 longKey of the
     * byte array or can use a key factory
     *
     * @param   longKey
     *
     * @return
     */
    private byte[] shortenSecretKey(final byte[] longKey) {

        try {

            // Use 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
            final byte[] shortenedKey = new byte[32];

            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);

            return shortenedKey;

            // Below lines can be more secure
            // final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
            //
            // return keyFactory.generateSecret(desSpec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void encryptAndDecryptMessage(String text, ByteArrayOutputStream baos, String type) {

        File tmp = new File("cipher_params.txt");
        boolean fileCreated = false;

        try {

            if (!tmp.exists()) {
                tmp.createNewFile();
            }

            PrintWriter out = new PrintWriter(tmp.getAbsoluteFile());

            StringBuilder sb = new StringBuilder();
            sb.append("KEY=");
            sb.append(bytesToHex(secretKey));
            sb.append("\n");
            sb.append("IV=");
            sb.append(IV);
            sb.append("\n");
            sb.append("INPUT=");
            if (type.equals("-e")) {
                sb.append(bytesToHex(text.getBytes()));
            } else if (type.equals("-d")) {
                sb.append(text);
            }


            out.write(sb.toString());

            out.flush();
            out.close();

            fileCreated = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (fileCreated) {
            ExecuteShellCommand com = new ExecuteShellCommand();
            try {
                com.executeCommands(new File("cipher_params.txt").getAbsolutePath(), type, baos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                new File("cipher_params.txt").delete();
            }
        }
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
