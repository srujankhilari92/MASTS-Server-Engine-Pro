package ethernet;

import java.io.IOException;

/**
 *
 * @author Sudhakar Barde
 */
public class MastsvEV {

    static String javahomepath;

    public String environment() {

        String javaHome = System.getProperty("java.home");

        if ((javaHome.contains("jre")) && (javaHome.contains("jdk"))) {

            int len = javaHome.length();

            String d = javaHome.substring(0, len - 4);

            javahomepath = d;
        } else if (javaHome.contains("jre")) {

            javahomepath = javaHome;

        } else {

            javahomepath = javaHome;

        }
        return javahomepath.toString();
    }

    public boolean setpat() throws IOException {
        String data = environment();

        if ((!data.equals("")) || (!data.equals(null))) {
            String systemCLASSPATH = System.getenv("CLASSPATH");
            String systemJAVA_HOME = System.getenv("JAVA_HOME");
            String systemPATH = System.getenv("PATH");

            if (systemCLASSPATH != null && systemCLASSPATH != "" && systemJAVA_HOME != null && systemPATH != null) {

                return true;
            } else {

                return false;
            }

        } else {

            return false;

        }

    }

}
