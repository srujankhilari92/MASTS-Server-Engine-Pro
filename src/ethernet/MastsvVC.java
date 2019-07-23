package ethernet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

import config.MastsvCM;
import secure.MastsvR;

/**
 *
 * @author swapnil
 */
public class MastsvVC {
	
	//srujan
	public static void main(String[] args) throws Exception {
		
		//System.setProperty("jsse.enableSNIExtension", "false");
		URL url = new URL("https://varutra.com/masts/Update-2016/information.php?thankyou=");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		con.connect();
		//srujan
		System.out.println(" in pingvarutra con.getResponseCode(): "+con.getResponseCode());
	}

	String line;
	String serverresponse, serverdate;
	String decrptserverresponse;
	String decrptserverdate;

	public String pingvarutra() {
		try {

			secure.MastsvR res = new MastsvR();
			String key = res.toChange(77) + res.toChange(64) + res.toChange(114) + res.toChange(49) + res.toChange(50) + res.toChange(51)
					+ res.toChange(52) + res.toChange(53) + res.toChange(87) + res.toChange(64) + res.toChange(114) + res.toChange(49)
					+ res.toChange(83) + res.toChange(101) + res.toChange(52) + res.toChange(74);
			

			String myzone = java.util.TimeZone.getDefault().getID();

			Date dd = new Date();
			SimpleDateFormat t = new SimpleDateFormat("yyyy/MM/dd");
			String currentdate = t.format(dd);

			String dateencrept = MastsvSe.encrypt(currentdate, key);
			String zoneencrept = MastsvSe.encrypt(myzone, key);

			//srujan
			/*URL url = new URL(
					"http://www.varutra.com/masts/Update-2016/information.php?thankyou=" + zoneencrept + "&clientname=" + dateencrept);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();*/
			
			
			URL url = new URL(MastsvCM.information + zoneencrept + "&clientname=" + dateencrept);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.connect();
			//srujan
			System.out.println(" in pingvarutra con.getResponseCode(): "+con.getResponseCode());
			
			if (con.getResponseCode() == 200) {

				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream()));
				line = in.readLine();

				String spilt[] = line.split(":");
				for (int i = 0; i < spilt.length; i++) {

				}
				serverresponse = spilt[1].trim();
				serverdate = spilt[3];

				in.close();
				try {
					decrptserverresponse = MastsvSe.decrypt(serverresponse, key);
					decrptserverdate = MastsvSe.decrypt(serverdate, key);
				} catch (Exception g) {
					config.MastsvCExcep.handleException(g, "Varcheck");
					JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
					System.out.println("VARRSDECEXC");
					return "mismatchfoundsomechanges";
				}

				if (decrptserverresponse.equals("true")) {
					Date cd = new Date();
					SimpleDateFormat ct = new SimpleDateFormat("yyyy/MM/dd");
					String d1 = ct.format(cd);
					SimpleDateFormat sdff = new SimpleDateFormat("yyyy/MM/dd");
					Date firstcheckdate = sdff.parse(decrptserverdate);
					Date currentdateforcmp = sdff.parse(d1);

					if (firstcheckdate.equals(currentdateforcmp)) {

						
							return "responsesuccess";
											
					} else {
						JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
						System.out.println("VARRSCRRBTUSRCHG");
						return "mismatchfoundsomechanges";
					}

				} else {
					JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

					System.out.println("VARRSFAIFRVALD");
					return "failforservercheck";
				}

			} else {
				JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

				System.out.println("VARCHECKRSNOTFOU");
				return "responsefail";
			}
		} catch (Exception e) {
			config.MastsvCExcep.handleException(e, "Varcheck");
			JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Check your internet connection settings and try again.", "MASTS", JOptionPane.WARNING_MESSAGE);
			System.out.println("VARCHECKRSNOTFOU");
			return "responsefail";

		}

	}
}
