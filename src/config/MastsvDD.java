/**
 * @Class Name : DeleteDirectoryExample.java
 */
package config;

/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jan 27, 2015 4:27:40 PM
 * @License : Copyright © 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
import java.io.File;
import java.io.IOException;

public class MastsvDD {

    public static void deleteFile(File directory) {
        if (!directory.exists()) {

        } else {

            try {

                delete(directory);

            } catch (IOException e) {
            	config.MastsvCExcep.handleException(e, "Deldir");

            }
        }

    }

    public static void delete(File file)
            throws IOException {

        if (file.isDirectory()) {
            if (file.list().length == 0) {

                file.delete();

            } else {

                String files[] = file.list();

                for (String temp : files) {

                    File fileDelete = new File(file, temp);

                    delete(fileDelete);
                }

                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            file.delete();
        }
    }
}
