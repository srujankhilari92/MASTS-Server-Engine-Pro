package ethernet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

import config.MastsvCM;
import secure.MastsvR;

/**
 *
 * @author swapnil
 */
public class MastsvGUU {

    String line;

    public String pingvarutralicenceupdateuser(String prkey, String action) {
        try {
            String hdd = null;
            StringTokenizer st = new StringTokenizer(prkey, "%");
            while (st.hasMoreTokens()) {

                hdd = st.nextToken();

            }

            String userhddid = hdd.trim();

            secure.MastsvR res = new MastsvR();
            String key = res.toChange(77) + res.toChange(64) + res.toChange(114) + res.toChange(49) + res.toChange(50) + res.toChange(51)
                    + res.toChange(52) + res.toChange(53) + res.toChange(87) + res.toChange(64) + res.toChange(114) + res.toChange(49)
                    + res.toChange(83) + res.toChange(101) + res.toChange(52) + res.toChange(74);
            
            String dateencrept = MastsvSe.encrypt(userhddid, key);
            String actionencrept = MastsvSe.encrypt(action, key);

            //srujan
            /*URL url = new URL(
                    "http://www.varutra.com/masts/MASTSUpdatePlugin/googlerpovidedactupdatedservice.php?googleservice=" + dateencrept + "&prolog=" + actionencrept);
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();*/
            
            URL url = new URL(MastsvCM.googleProvidedService + dateencrept + "&prolog=" + actionencrept);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.connect();

            if (con.getResponseCode() == 200) {

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        url.openStream()));
                line = in.readLine();

                String spilt[] = line.split(":");
                for (int i = 0; i < spilt.length; i++) {

                }
                in.close();
                String result = line;
                String decrptserverresponse = MastsvSe.decrypt(result, key);

                if (decrptserverresponse.equals("True")) {
                    return "validatesuccess";
                } else {
                    return "validategetfail";
                }

            } else {
                JOptionPane.showMessageDialog(null, "Sorry for the inconvenience! We are currently down for maintainance. Please try after some time.", "MASTS", JOptionPane.WARNING_MESSAGE);

                System.out.println("VARCHECKRSNOTFOU");
                return "responsefail";
            }
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Gooupdateuser");
            JOptionPane.showMessageDialog(null, "Sorry for the inconvenience! We are currently down for maintainance. Please try after some time.", "MASTS", JOptionPane.WARNING_MESSAGE);
            System.out.println("VARCHECKRSNOTFOU");
            return "responsefail";

        }
    }
}
