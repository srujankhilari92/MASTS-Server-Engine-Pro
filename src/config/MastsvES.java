package config;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;

/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jul 3, 2015 3:50:56 PM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvES {

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

    public String setpat() throws IOException {
        String data = environment();

        if ((!data.equals("")) || (!data.equals(null))) {
            final String JAVA_HOME = data;
            final String ClASS_PATH = data + "\\bin;" + data + "\\db";
            

            final String PATH = data + "\\bin";

            String systemCLASSPATH = System.getenv("CLASSPATH");

            String systemJAVA_HOME = System.getenv("JAVA_HOME");

            int i = 0;

            if (systemCLASSPATH != null) {
                if (!systemJAVA_HOME.contains(JAVA_HOME)) {
                    i++;
                    System.out.println("Java is Updated.");
                }
            }

            String systemPATH = System.getenv("PATH");
            if (i > 0) {
                if (systemCLASSPATH == null) {

                    String cmdCLASS_PATH = "SETX CLASSPATH \"" + ClASS_PATH + "\"";
                    Runtime.getRuntime().exec(cmdCLASS_PATH);

                } else {
                    String cmdCLASS_PATH = "SETX CLASSPATH \"" + ClASS_PATH + "\"";
                    Runtime.getRuntime().exec(cmdCLASS_PATH);

                }

                if (systemJAVA_HOME == null) {
                    String cmdJAVA_HOME = "SETX JAVA_HOME \"" + JAVA_HOME + "\"";
                    Runtime.getRuntime().exec(cmdJAVA_HOME);

                } else {
                    if (systemJAVA_HOME.contains(JAVA_HOME)) {
                        String cmdJAVA_HOME = "SETX JAVA_HOME \"" + JAVA_HOME + "\"";
                        Runtime.getRuntime().exec(cmdJAVA_HOME);

                    } else {
                        String cmdJAVA_HOME = "SETX JAVA_HOME \"" + JAVA_HOME + "\"";

                        Runtime.getRuntime().exec(cmdJAVA_HOME);

                    }

                }

                if (systemPATH == null) {
                    String cmdPATH = "SETX PATH \"" + PATH + "\"";
                    Runtime.getRuntime().exec(cmdPATH);

                } else {
                    String cmdPATH = "SETX PATH \"" + PATH + "\"";
                    Runtime.getRuntime().exec(cmdPATH);

                }

            }

            return "pathset";

        } else {
            return "JDKFail";

        }

    }

    public String decision() {
        try {
            String setpat = setpat();
            if (setpat.equals("pathset")) {
                return "GETALL";
            } else {
                return "NOTGETALL";
            }
        } catch (IOException ex) {
        	config.MastsvCExcep.handleException(ex, "EnvSetting");
        	return "NOTGETALL";
        }
    }

}
