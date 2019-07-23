package config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ethernet.MastsvLP;

public class MastsvL {

    static PrintStream log;
    static PrintWriter out;

    public static void createLoggs() {

        try {

            MastsvLP.Loadproperties();
            new File(MastsvLP.dummypath + "/Logs").mkdir();
            SimpleDateFormat sdf;
			String currentDateandTime;
	
			sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
			currentDateandTime = sdf.format(new Date());
            File ki = new File(MastsvLP.dummypath + "/Logs/"+currentDateandTime+"_Logs.txt");
            if (ki.exists()) {
                log = new PrintStream(MastsvLP.dummypath + "/Logs/"+currentDateandTime+"_Logs.txt");
                System.setOut(log);
                System.setErr(log);
                out = new PrintWriter(new BufferedWriter(new FileWriter(MastsvLP.dummypath + "/Logs/"+currentDateandTime+"_Logs.txt", true)));

            } else {
                log = new PrintStream(MastsvLP.dummypath + "/Logs/log.txt");
                System.setOut(log);
                System.setErr(log);

            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Logs");
        }

    }
    
    public String toChange(int i){

		return Character.toString ((char) i);

	}

}
