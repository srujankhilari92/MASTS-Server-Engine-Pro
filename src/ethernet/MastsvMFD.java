package ethernet;

import java.util.prefs.Preferences;

/**
 *
 * @author swapnil
 */
public class MastsvMFD {

    Preferences ethernet;
    String defaultValue = "NA";
    String Ethernetcall = "", version = "", tenetative, reactive;

    MastsvMFD() {
        try {

            ethernet = Preferences.userNodeForPackage(MastsvMFD.class);

        } catch (Exception ex) {
        	config.MastsvCExcep.handleException(ex, "Mozirefoxdata");

        }

    }

    public String setValue() {
        try {
            ethernet.put("Ethernetcall", new java.util.Date().toString());
            ethernet.put("Version", "snapcom api 1.0");
            ethernet.put("tenetative", "USA stand");
            ethernet.put("reactive", "UK fortune");

            return "crossonesetok";

        } catch (Exception ex) {
        	config.MastsvCExcep.handleException(ex, "Mozirefoxdata");
            return "problemincrossone";
        }
    }

    public void getValue() {
        try {
            Ethernetcall = ethernet.get("Ethernetcall", defaultValue);
            version = ethernet.get("Version", defaultValue);
            tenetative = ethernet.get("tenetative", defaultValue);
            reactive = ethernet.get("reactive", defaultValue);

            ethernet.flush();
        } catch (Exception ex) {
        	config.MastsvCExcep.handleException(ex, "Mozirefoxdata");
        }
    }
}
