/**
 * @Class Name : PortCheck.java
 */
package config;

import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jun 26, 2015 3:34:10 PM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvPC {

    public static boolean isPortInUse(int portNumber) {
        boolean result;
        try {
            new ServerSocket(portNumber).close();
            result = false;
        } catch (Exception e) {
            result = true;
            config.MastsvCExcep.handleException(e, "PortCheck");
        }
        return result;
    }

    public static boolean validatePort(String b) {
        String exp = "(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})";

        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(b);
        if (m.find()) {

            return true;
        } else {
            return false;
        }
    }
}
