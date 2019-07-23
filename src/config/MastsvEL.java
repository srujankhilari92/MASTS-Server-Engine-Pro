/**
 * @Class Name : EditLLog4J.java
 */
package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ethernet.MastsvLP;

/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jun 6, 2015 12:31:49 PM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvEL {

	public void Loadlog4Jroperties() {
		Properties prop = new Properties();
		MastsvLP l = new MastsvLP();
		String in = l.format();
		String file =in+"WebClient\\MASTS\\src\\main\\webapp\\log4j.properties";
		File propFile = new File(file);
		if (propFile.exists()) {
			try {
				InputStream is = new FileInputStream(propFile);

				prop.load(is);
				FileOutputStream out = new FileOutputStream(file);
				String source = prop.getProperty("log4j.appender.R.File");
				prop.setProperty("log4j.appender.R.File", file.substring(0, file.lastIndexOf("MASTS")) + "Logs\\info.log");
				source = prop.getProperty("log4j.appender.R.File");
				prop.store(out, null);
				out.close();
				is.close();

			} catch (Exception e) {
				config.MastsvCExcep.handleException(e, "E4Log");
			}
		}
	}

	public void loadJettyPort() {
		
		MastsvLP l = new MastsvLP();
		String in = l.format();
		String updatePlugin =in+"WebClient\\MASTS\\UpdatePlugins\\pom.xml";
		String destPath =in+"WebClient\\MASTS\\";
		File f = new File(updatePlugin);
		if (f.exists()) {
			try {
				MastsvMF.moveFile(updatePlugin, destPath);
				String file =in+"WebClient\\MASTS\\pom.xml";
				File propFile = new File(file);
				if (propFile.exists()) {
					try {

						MastsvMP.modify(file);
					} catch (Exception e) {
						config.MastsvCExcep.handleException(e, "E4Log");
					}
				}

				f.deleteOnExit();

			} catch (IOException e) {
				//TODO Auto-generated catch block
				config.MastsvCExcep.handleException(e, "E4Log");
			}

		} else {

			String file =in+"WebClient\\MASTS\\pom.xml";
			File propFile = new File(file);
			if (propFile.exists()) {
				try {

					MastsvMP.modify(file);
				} catch (Exception e) {
					config.MastsvCExcep.handleException(e, "E4Log");
				}
			}
		}

	}


	public void loadJettyFiles() {	

		System.out.println("In Load Jetty file..");
		//Unzip the files...
		MastsvUZ u=new MastsvUZ();
		u.unZipItUpdatedFiles();        

		//take back up of previous one....

		MastsvLP.Loadproperties();

		MastsvLP l = new MastsvLP();
		String in = l.format();
		File src =new File(in+"WebClient\\MASTS\\src");
		File dest =new File(in+"WebClient\\MASTSBackup\\src");

		if(src.exists()){

			try{
				if(!dest.exists()){

					new File(in+"WebClient\\MASTSBackup\\").mkdir();
					dest.mkdir();
					MastsvTB.copyFolder(src,dest);
					System.out.println("Copy backup 1 Load Jetty file..");
				}


			}catch(Exception e){
				config.MastsvCExcep.handleException(e, "E4Log");
				System.exit(0);
			}
		}

		src =new File(in+"WebClient\\MASTS\\target\\classes");
		dest =new File(in+"WebClient\\MASTSBackup\\classes");

		if(src.exists()){

			try{
				MastsvTB.copyFolder(src,dest);
				System.out.println("Copy backup 2 Load Jetty file..");
			}catch(IOException e){
				config.MastsvCExcep.handleException(e, "E4Log");
				System.exit(0);
			}
		}

		src =new File( in+"WebClient\\MASTS\\UpdatePlugins\\Plugin7\\src");
		dest =new File( in+"WebClient\\MASTS\\src");

		System.out.println("SRC path: "+in+"WebClient\\MASTS\\UpdatePlugins\\Plugin7\\src"+" for RES: "+new File(in+"WebClient\\MASTS\\UpdatePlugins\\Plugin7\\src").exists());
		if(src.exists()){

			try{
				MastsvTB.copyFolder(src,dest);
				System.out.println("Copy backup 3 Load Jetty file..");
			}catch(IOException e){
				config.MastsvCExcep.handleException(e, "E4Log");
				System.exit(0);
			}
		}
		
		
		src =new File( in+"WebClient\\MASTS\\UpdatePlugins\\Plugin7\\classes");
		dest =new File( in+"WebClient\\MASTS\\target\\classes");

		if(src.exists()){

			try{
				MastsvTB.copyFolder(src,dest);
				System.out.println("Copy backup 4 Load Jetty file..");
			}catch(IOException e){
				config.MastsvCExcep.handleException(e, "E4Log");
				System.exit(0);
			}
		}


	}

}
