/**
 * @Class Name : FileEncryptDecrypt.java
 */
package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import secure.MastsvCE;
import secure.MastsvFE;
/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jul 1, 2015 3:49:56 PM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvFED {

    public static void checkFile(String ine, String enc, String dec) {
        try {

            MastsvFE fe = new MastsvFE();
            String fkey = fe.getKeygard1();
            FileInputStream fis = new FileInputStream(ine);
            FileOutputStream fos = new FileOutputStream(enc);
            try {
                MastsvCE.encrypt(fkey, fis, fos);
            } catch (Throwable e) {

                e.printStackTrace();
            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Fildecrypt");
        }

        try {
            MastsvCong c = new MastsvCong();
            String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                    + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);

            FileInputStream fis111 = new FileInputStream(enc);
            FileOutputStream fos111 = new FileOutputStream(dec);
            try {
                decrypt(key, fis111, fos111);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Properties props = new Properties();

            InputStream inputStream = new FileInputStream(new File(dec));

            if (inputStream != null) {
                props.load(inputStream);
            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Fildecrypt");
        }

    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;

        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();

    }
}
