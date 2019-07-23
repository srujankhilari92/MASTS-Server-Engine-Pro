package config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MastsvDTm {

	
	public static String getDateTime(){
		Date date;
		SimpleDateFormat dateFormat;

		String sDate;
		date = new Date();
		dateFormat = new SimpleDateFormat("E, MMMM dd, yyyy h:mm a");
		sDate = dateFormat.format(date);
		
		return sDate;
	}
	
}
