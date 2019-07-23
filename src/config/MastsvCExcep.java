package config;

public class MastsvCExcep {
	public static void handleException(Exception ex, String keyword){
		StackTraceElement[] elements = ex.getStackTrace();  
        for (int iterator=1; iterator<=elements.length; iterator++)  
               System.out.println("------Error Message: "+ex.getMessage()+"------ Check keyword: "+keyword+"----- Class Name: "+elements[iterator-1].getClassName()+"----- Method Name:"+elements[iterator-1].getMethodName()+"----- Line Number:"+elements[iterator-1].getLineNumber());
   
	} 
}
