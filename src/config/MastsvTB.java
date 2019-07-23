package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class MastsvTB {

	public static void copyFolder(File src, File dest)
	    	throws IOException{
	    	
	    	if(src.isDirectory()){
	    		
	    		//if directory not exists, create it
	    		if(!dest.exists()){
	    		   dest.mkdir();
	    		 
	    		}
	    		
	    		//list all the directory contents
	    		String files[] = src.list();
	    		
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	    	   
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	    	                     
	    	        byte[] buffer = new byte[1024];
	    	    
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();
	    	     
	    	}
	    }
	
	public static void deleteFolder(String path)
	{
		
		File index=new File(path);
		if(index.exists()){
			deleteFol(index);
		}
		
	}
	
	public static void deleteFol(File index){
		if(index.exists()){

			String[]entries = index.list();
			for(String s: entries){
				File currentFile = new File(index.getPath(),s);
				//System.out.println(currentFile.getName());
				
					if(currentFile.isFile())
					{
						currentFile.deleteOnExit();
					}
					else
					{
						deleteFol(currentFile);
					}
				
				currentFile.delete();
			}
		
		}
	}

}
