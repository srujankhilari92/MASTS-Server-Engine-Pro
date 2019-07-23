package config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import secure.MastsvCE;
import secure.MastsvFE;
import ethernet.MastsvdAD;
import ethernet.MastsvCB;
import ethernet.MastsvLP;

public class MastsvUZ {

	/**
	 * Unzip it
	 *
	 * @param zipFile input zip file
	 * @param output zip file output folder
	 */
	public void unZipIt() {

		MastsvLP.Loadproperties();
		File f = new File(MastsvLP.dummypath + "MASTS.zip");
		System.out.println("MASTS is uploading...");
		if (!new File(MastsvLP.dummypath + "MASTS").exists()) {

			try {
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("MASTS.zip");
				OutputStream outputStream;
				MastsvLP.Loadproperties();
				outputStream = new FileOutputStream(f);

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}

				outputStream.close();
				inputStream.close();

				create(f.getPath(), MastsvLP.dummypath);

				f.deleteOnExit();
				
				MastsvEL e = new MastsvEL();
				e.Loadlog4Jroperties();
				e.loadJettyPort();
				makeProp();
			} catch (Exception e) {
				e.printStackTrace();
				config.MastsvCExcep.handleException(e, "UnZzip");
			}
		} else {
			
			MastsvEL e = new MastsvEL();
			e.Loadlog4Jroperties();
			MastsvLP.Loadproperties();
			MastsvLP l = new MastsvLP();
	        String in = l.format();
	        String updatePlugin = in + "WebClient/MASTS/UpdatePlugins/Plugins/Plugin7.zip";
			File f1 = new File(updatePlugin);
			if(f1.exists()){
				
				e.loadJettyFiles();
				
			}			
			MastsvTB.deleteFolder(in + "WebClient/MASTS/UpdatePlugins");
			e.loadJettyPort();
			makeProp();
			f.deleteOnExit();

		}

	}

	public void unZipItUpdatedFiles() {

		MastsvLP.Loadproperties();
		MastsvLP l = new MastsvLP();
        String in = l.format();
        String updatePlugin = in + "WebClient/MASTS/UpdatePlugins/Plugins/Plugin7.zip";
		File f = new File(updatePlugin);
		System.out.println("MASTS is uploading...");
		if (!new File(MastsvLP.dummypath + "MASTS/UpdatePlugins/Plugin7").exists()) {

			try {
				createUpdateFiles(f.getPath(), MastsvLP.dummypath + "MASTS/UpdatePlugins/");

			} catch (Exception e) {
				e.printStackTrace();
				config.MastsvCExcep.handleException(e, "UnZzip");
			}
		} 
	}
	
	public void unZipItConfig() {
		
		MastsvLP.Loadproperties();
		File f = new File(MastsvLP.dummypath + "maven.zip");

		if (!new File(System.getProperty("user.home")+"/.m2/" + "maven").exists()) {


			try {
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("maven.zip");
				OutputStream outputStream;
				MastsvLP.Loadproperties();
				outputStream = new FileOutputStream(f);

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);

				}

				outputStream.close();
				inputStream.close();

				createConfig(f.getPath(), System.getProperty("user.home")+"/.m2/");
				f.deleteOnExit();
				

			} catch (Exception e) {
				config.MastsvCExcep.handleException(e, "UnZzip");
			}
		

		} else {
			/*makeProp();
			EditLLog4J e = new EditLLog4J();
			e.Loadlog4Jroperties();
			f.deleteOnExit();*/
			
		}
		
	}

	public void create(String input, String output) throws Exception {
		File srcFile = new File(input);

		File temp = new File(output);

		if (!new File(output + "MASTS").exists()) {

			temp.mkdir();

			ZipFile zipFile = null;
			try {

				zipFile = new ZipFile(srcFile);

				Enumeration<? extends ZipEntry> e = zipFile.entries();
				while (e.hasMoreElements()) {

					ZipEntry entry = e.nextElement();

					File destinationPath = new File(output, entry.getName());

					destinationPath.getParentFile().mkdirs();

					if (entry.isDirectory()) {
						continue;
					} else {

						BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
						int b;
						byte buffer[] = new byte[1024];

						FileOutputStream fos = new FileOutputStream(destinationPath);

						BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

						while ((b = bis.read(buffer, 0, 1024)) != -1) {
							bos.write(buffer, 0, b);
						}

						bos.close();
						bis.close();

					}

				}
				zipFile.close();
				srcFile.delete();
			} catch (ZipException e) {
				System.out.println("Exception In UNZIP create..." + e.getMessage());
				config.MastsvCExcep.handleException(e, "UnZzip");
			} catch (IOException e) {
				System.out.println("Exception In UNZIP create..." + e.getMessage());
				config.MastsvCExcep.handleException(e, "UnZzip");
			}

		}

	}

	public void createUpdateFiles(String input, String output) throws Exception {
	
		File srcFile = new File(input);

		File temp = new File(output);

		if (!new File(output + "Plugin7").exists()) {

			temp.mkdir();

			ZipFile zipFile = null;
			try {

				zipFile = new ZipFile(srcFile);

				Enumeration<? extends ZipEntry> e = zipFile.entries();
				while (e.hasMoreElements()) {

					ZipEntry entry = e.nextElement();

					File destinationPath = new File(output, entry.getName());

					destinationPath.getParentFile().mkdirs();

					if (entry.isDirectory()) {
						continue;
					} else {

						BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
						int b;
						byte buffer[] = new byte[1024];

						FileOutputStream fos = new FileOutputStream(destinationPath);

						BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

						while ((b = bis.read(buffer, 0, 1024)) != -1) {
							bos.write(buffer, 0, b);
						}

						bos.close();
						bis.close();

					}

				}
				zipFile.close();
				//srcFile.delete();
			} catch (ZipException e) {
				System.out.println("Exception In UNZIP create..." + e.getMessage());
				config.MastsvCExcep.handleException(e, "UnZzip");
			} catch (IOException e) {
				System.out.println("Exception In UNZIP create..." + e.getMessage());
				config.MastsvCExcep.handleException(e, "UnZzip");
			}

		}

	}

	
	
	public void createConfig(String input, String output) throws Exception {
		File srcFile = new File(input);
		File temp = new File(output);

		if (!new File(output + "maven").exists()) {

			temp.mkdir();

			ZipFile zipFile = null;
			try {

				zipFile = new ZipFile(srcFile);

				Enumeration<? extends ZipEntry> e = zipFile.entries();
				while (e.hasMoreElements()) {

					ZipEntry entry = e.nextElement();

					File destinationPath = new File(output, entry.getName());

					destinationPath.getParentFile().mkdirs();

					if (entry.isDirectory()) {
						continue;
					} else {
						BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
						int b;
						byte buffer[] = new byte[1024];

						FileOutputStream fos = new FileOutputStream(destinationPath);

						BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

						while ((b = bis.read(buffer, 0, 1024)) != -1) {
							bos.write(buffer, 0, b);
						}

						bos.close();
						bis.close();

					}

				}
				zipFile.close();
				srcFile.delete();
			} catch (ZipException e) {
				System.out.println("Exception In unzip config..." + e.getMessage());
				config.MastsvCExcep.handleException(e, "UnZzip");
			} catch (IOException e) {
				System.out.println("Exception In unzip config..." + e.getMessage());
				config.MastsvCExcep.handleException(e, "UnZzip");
			}

		}
	}

	public static void makeProp() {
		String dest = MastsvLP.dummypath;
		dest = dest.replaceAll("%5b", "[").replaceAll("%5B", "[").replaceAll("%5d", "]").replaceAll("%5D", "]").replaceAll("%20", " ");
		dest = dest.replaceAll("%7b", "{").replaceAll("%7B", "{").replaceAll("%7d", "}").replaceAll("%7D", "}").replaceAll("%23", "#");
		dest = dest.replaceAll("%25", "%").replaceAll("%5e", "^").replaceAll("%5E", "^").replaceAll("%3D", "=").replaceAll("%3d", "=").replaceAll("%60", "`");

		try {

			MastsvLP.Loadproperties();
			Properties props = new Properties();
			MastsvCB c = new MastsvCB();
			MastsvL res=new MastsvL();
			String t1=res.toChange(77)+res.toChange(97)+res.toChange(115)+res.toChange(116)
			+res.toChange(83)+res.toChange(50)+res.toChange(48)+res.toChange(49)
			+res.toChange(54)+res.toChange(86)+res.toChange(65)+res.toChange(103)
			+res.toChange(101)+res.toChange(110)+res.toChange(116);

			String t2=res.toChange(86)+res.toChange(77)+res.toChange(48)+res.toChange(98)
			+res.toChange(105)+res.toChange(76)+res.toChange(101)+res.toChange(50)
			+res.toChange(48)+res.toChange(49)+res.toChange(54)+res.toChange(77)
			+res.toChange(97)+res.toChange(114)+res.toChange(115);
			File f = new File(dest + "MASTS/src/main/webapp/build.properties");
			if (f.exists() && f.getPath().contains("MASTS/src/main/webapp")) {
				f.delete();
				props.setProperty("VAR1", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), "jdbc:derby:" + dest.replaceAll("\\\\", "/") + "MASTS/Catalog/"));
				props.setProperty("VAR2", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t1));
				props.setProperty("VAR3", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t2));
				props.setProperty("VAR4", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin1/"));
				props.setProperty("VAR5", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin2/"));
				props.setProperty("VAR6", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin1dir/"));
				props.setProperty("VAR7", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin2dir/"));
				props.setProperty("VAR8", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/"));
				f.createNewFile();

			} else {

				props.setProperty("VAR1", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), "jdbc:derby:" + dest.replaceAll("\\\\", "/") + "MASTS/Catalog/"));
				props.setProperty("VAR2", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t1));
				props.setProperty("VAR3", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t2));
				props.setProperty("VAR4", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin1/"));
				props.setProperty("VAR5", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin2/"));
				props.setProperty("VAR6", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin1dir/"));
				props.setProperty("VAR7", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/src/main/webapp/Plugin2dir/"));
				props.setProperty("VAR8", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "MASTS/"));

				f.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(f);
			props.store(out, "This is an optional header comment string");

			InputStream inputStream = new FileInputStream(f);
			if (inputStream != null) {
				props.load(inputStream);

			}
		} catch (Exception e) {
			System.out.println("Exception In unzip makeprop..." + e.getMessage());
			config.MastsvCExcep.handleException(e, "UnZzip");
		}

		try {

			MastsvFE fe = new MastsvFE();
			String fkey = fe.getKeygard1();
			FileInputStream fis = new FileInputStream(dest + "MASTS/src/main/webapp/build.properties");
			FileOutputStream fos = new FileOutputStream(dest + "MASTS/src/main/webapp/build-1.properties");
			try {
				MastsvCE.encrypt(fkey, fis, fos);
			} catch (Throwable e) {

				e.printStackTrace();
			}

			new File(dest + "MASTS/src/main/webapp/build.properties").delete();

		} catch (Exception e) {
			e.printStackTrace();
			config.MastsvCExcep.handleException(e, "UnZzip");
		}

	}

}
