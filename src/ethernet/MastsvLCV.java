package ethernet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

import config.MastsvCM;

/**
 *
 * @author swapnil
 */
public class MastsvLCV {

    String line;

    public String getvaurtaid(String lic) throws IOException {
        String vl = "M@r12345W@r1Se4J";
        
        String ssl = MastsvSe.encrypt(lic, vl);
        //srujan
       /* URL url = new URL(
                "http://www.varutra.com/masts/MASTSUpdatePlugin/Testcasesintegration.php?mypackdata=" + ssl);
        HttpURLConnection con = (HttpURLConnection) url
                .openConnection();*/
        
        URL url = new URL(MastsvCM.testCasesIntegration + ssl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        
        if (con.getResponseCode() == 200) {

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            line = in.readLine();

        } else {
            JOptionPane.showMessageDialog(null, "Sorry for the inconvenience! We are currently down for maintainance. Please try after some time.", "MASTS", JOptionPane.WARNING_MESSAGE);

            System.out.println("VARCHECKRSNOTFOU");
            return "responsefail";
        }
        return null;
    }

}
