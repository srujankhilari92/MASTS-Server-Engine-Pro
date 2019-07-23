package ethernet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author swapnil
 */
public class MastsvVBC {

    public String ISVMdetect() {
        String result = "NOT VM";

       
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"SYSTEMINFO"});
        } catch (IOException e) {
        	config.MastsvCExcep.handleException(e, "Virdetect");
            throw new RuntimeException(e);
        }

     
        is = process.getInputStream();
        ArrayList<String> al = new ArrayList<String>();
        BufferedReader input = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = input.readLine()) != null) {

                if (line.contains("BIOS Version:") || line.contains("System Manufacturer:") || line.contains("System Model:")) {
                    al.add(line);
                }
            }
        } catch (IOException e) {
        	config.MastsvCExcep.handleException(e, "Virdetect");
            e.printStackTrace();
        }

        for (int i = 0; i < al.size(); i++) {
            String cmpstring = al.get(i).toString().toLowerCase();
            if (cmpstring.contains("virtualbox") || cmpstring.contains("vmware") || cmpstring.contains("parallels") || cmpstring.contains("hyper-v")) {
                result = "vmfoundhere";
                break;
            }
        }

        return result;
    }
}
