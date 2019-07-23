package config;

import java.io.IOException;

import ethernet.MastsvLP;

public class MastsvFES {

    public String setenvir() throws IOException {
        try {
            MastsvLP.Loadproperties();

            String ma = System.getProperty("user.home")+"/.m2/" + "maven";
            ma = ma.replaceAll("//", "/");

            ma = ma.replaceAll("/", "\\\\");

            String PATH = ma + "\\bin";
            String MAVEN_HOME = ma;
            String javaHome = System.getProperty("java.home");
            String systemPATH = System.getenv("PATH");

            PATH = PATH + ";" + javaHome + "\\bin";

            String systemMAVEN_HOME = System.getenv("MAVEN_HOME");

            if (systemPATH == null) {
                String cmdPATH = "SETX PATH \"" + PATH + "\"";
                Runtime.getRuntime().exec(cmdPATH);

            } else {
                String cmdPATH = "SETX PATH \"" + PATH + "\"";
                Runtime.getRuntime().exec(cmdPATH);

            }

            if (systemMAVEN_HOME == null) {
                String cmdMAVEN_HOME = "SETX MAVEN_HOME \"" + MAVEN_HOME + "\"";
                Runtime.getRuntime().exec(cmdMAVEN_HOME);

            } else {
                String cmdMAVEN_HOME = "SETX MAVEN_HOME \"" + MAVEN_HOME + "\"";
                Runtime.getRuntime().exec(cmdMAVEN_HOME);
            }
            return "pathsuccess";

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "FinalEnv");
        }
        return "pathfail";
    }

}
