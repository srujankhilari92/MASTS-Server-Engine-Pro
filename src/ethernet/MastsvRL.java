package ethernet;

import static ethernet.MastsvA.encryptOrDecrypt;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.crypto.Cipher;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import config.MastsvCong;

/**
 *
 * @author swapnil
 */
public class MastsvRL extends Frame implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6000897977039265664L;
	FileInputStream fis2, fis21;
    FileOutputStream fos2, fos21;
    String renewsourcefile, renewdestiny;

    public String uploadactionforrenew() throws Throwable {
        MastsvLP.Loadproperties();

        renewsourcefile = MastsvLP.renewpath;
        renewdestiny = MastsvLP.renewdestpath;
        JFileChooser fileopen = new JFileChooser();

        int ret = fileopen.showDialog(null, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            String filenamese = file.getName();

            if (filenamese.equals("MASTS_Renew_License.vml")) {
                if (file.length() < 2000) {
                    fis2 = new FileInputStream(file);
                    fos2 = new FileOutputStream(renewsourcefile);
                    doCopy(fis2, fos2);
                    fis2.close();
                    fos2.close();
                    String finalstatus = checkvalidationforrenew();
                    if (finalstatus.equals("licenceisokrenewcomplete")) {
                        return "okfileisrenew";
                    } else {
                        return "sorryprobleinrenewing";
                    }
                } else {
                    System.out.println("LCRNSZBG");
                    JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                    MastsvA.jButton1.setEnabled(true);
                    MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
                    MastsvA.jButton2.setEnabled(false);
                    MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                    this.dispose();
                    return "okfileisfailtorenew";
                }
            } else {
                System.out.println("LCRNINVFLUP");
                JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                MastsvA.jButton1.setEnabled(true);
                MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
                MastsvA.jButton2.setEnabled(false);
                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                this.dispose();
                return "okfileisfailtorenew";
            }
        } else {
            return "okfileisfailtorenew";
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

    public String checkvalidationforrenew() throws Throwable {

        /*  Decreption decs=new Decreption();
         decs.accefile();*/
        MastsvCong c = new MastsvCong();
        String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);
        try {
            fis21 = new FileInputStream(renewsourcefile);
            fos21 = new FileOutputStream(renewdestiny);
            decrypt(key, fis21, fos21);
            /* FileRevers rv=new FileRevers();
             rv.checksystem();*/
            fis21.close();
            fos21.close();
            MastsvRFV renecheck = new MastsvRFV();
            String licencevalidationstatus = renecheck.filevalidationfirststep();
            if (licencevalidationstatus.equals("renewfilevalidatesuccess")) {
                return "licenceisokrenewcomplete";
            } else {
                System.out.println("LCRNVLFL");
                return "licenceisnotokrenew";
            }

        } catch (Exception f) {
        	config.MastsvCExcep.handleException(f, "Renlicence");
            System.out.println("LCRNVLFL");

            return "licenceisnotokrenew";
        }

    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

}
