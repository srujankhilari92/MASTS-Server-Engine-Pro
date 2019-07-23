package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class MastsvMF implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5551018986012904890L;

    public static void moveFile(String srcPath, String destPath) throws IOException {

        InputStream inStream = null;
        OutputStream outStream = null;

        try {

            File afile = new File(srcPath);
            File bfile = new File(destPath.substring(0, destPath.lastIndexOf("/")) + "/" + srcPath.substring(srcPath.lastIndexOf("/") + 1, srcPath.length()));

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }

            inStream.close();
            outStream.close();

            afile.deleteOnExit();

        } catch (IOException e) {
        	config.MastsvCExcep.handleException(e, "Movfile");
        }

    }
}
