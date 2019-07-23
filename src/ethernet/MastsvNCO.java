package ethernet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author swapnil
 */
public class MastsvNCO {

    public static final String TIME_SERVER = "time-a.nist.gov";

    public String reinstalationnetcheck() {

        try {
            URL url = new URL("http://www.google.com");

            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();

            con.connect();
            if (con.getResponseCode() == 200) {
                return "ConnectionfoundReinstall";
            } else {
                return "ConnectionNotfoundForReinstall";
            }

        } catch (Exception r) {
        	config.MastsvCExcep.handleException(r, "Netoperation");
            
            return "ConnectionNotfoundForReinstall";
        }
    }

    public String getnetconnectionstatus() {
        try {

            URL url = new URL("http://www.google.com");

            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();

            con.connect();

            if (con.getResponseCode() == 200) {

                String getdatestatus = getcomparestatus();
                if (getdatestatus.equals("successfullymatch")) {

                    System.out.println("NEWUSRNETFOOKEVR");
                    return "Connectionfoundeverythingok";
                } else {

                    System.out.println("NEWUSRNETFOUBTUSRCHANG");
                    return "Connectionfoundbutprobleinchanges";
                }

            } else {
                System.out.println("NEWUSRNETNTFOU");
                return "Connectionnotfound";
            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Netoperation");
            System.out.println("NEWUSRNETNTFOU");
            return "Connectionnotfound";
        }

    }

    public String getcomparestatus() throws ParseException, MalformedURLException, IOException {

        URL handle2 = new URL("http://www.google.com");
        URLConnection con2 = handle2.openConnection();
        long dateinfo2;

        dateinfo2 = con2.getDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String myServer2data = sdf.format(new Date(dateinfo2));

        SimpleDateFormat datepars = new SimpleDateFormat("yyyy/MM/dd");

        Date dd = new Date();
        SimpleDateFormat t = new SimpleDateFormat("yyyy/MM/dd");
        String d1 = t.format(dd);

        Date serverSecondcurrentdate = datepars.parse(myServer2data);
        Date systemcurrentdate = datepars.parse(d1);

        if (systemcurrentdate.before(serverSecondcurrentdate)) {

            return "wrongmatch";
        } else if (systemcurrentdate.after(serverSecondcurrentdate)) {

            return "wrongmatch";
        } else if (systemcurrentdate.equals(serverSecondcurrentdate)) {

            return "successfullymatch";

        } else {
            return "wrongmatch";
        }

    }
}
