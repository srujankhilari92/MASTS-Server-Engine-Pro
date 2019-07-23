package ethernet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import config.MastsvL;
import config.MastsvRDT;

public class MastsvD {

    Statement st, stt;
    ResultSet rs, rss;
    Connection con;
    File filecheck;

    public void accefile() throws Throwable {
        try {

            MastsvD d = new MastsvD();
            String status = d.databasecheck();

           
            if (status.equals("notfind")) {

            	MastsvL m=new MastsvL();
                String key = m.toChange(115)+m.toChange(48)+m.toChange(113)+m.toChange(114)+m.toChange(98)+m.toChange(49)+m.toChange(116)+m.toChange(119)+m.toChange(97)+m.toChange(52);

                MastsvLP.Loadproperties();
                FileInputStream fis2 = new FileInputStream(MastsvLP.source);
                FileOutputStream fos2 = new FileOutputStream(MastsvLP.target);
                try {
                    decrypt(key, fis2, fos2);
                } catch (Throwable e) {
                    System.out.println("Exception Deccc Line No.: " + e.getStackTrace()[2].getLineNumber());
                }
                MastsvFV dq = new MastsvFV();
                dq.filevalidationfirststep();

            } else {

                MastsvDVL check = new MastsvDVL();
                check.getdatabasedata();
                check.gettingfiledata();
                check.getsystemdata();
                check.checklicence();

            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Decrion");
        }
    }

    public String filecheckatstart() {
        MastsvLP.Loadproperties();
        filecheck = new File(MastsvLP.source);
        if (filecheck.exists()) {
            return "fileokyes";
        } else {
            return "filemissnofound";
        }
    }

    public String databasecheck() {

        String dbName = MastsvRDT.databasename.get(0);

        String bd = MastsvLP.dummypath + dbName;

        File dbpath = new File(bd);
        try {

            if (dbpath.exists()) {

                return "findmedatabase";
            } else {

                return "notfind";
            }
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Decrion");
        }
        return "";
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
