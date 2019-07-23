package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import secure.MastsvCE;
import secure.MastsvFE;
import ethernet.MastsvdAD;
import ethernet.MastsvCB;

public class MastsvCong {
	
	public static void main(String[] args) {
		
		String dest = new File("afile").getAbsolutePath()/*.replaceAll("afile", "")*/;
		System.out.println("dest: "+dest);
	}

	public String config() {
		String res="100";
		try {

			MastsvUZ u = new MastsvUZ();
			MastsvFES f = new MastsvFES();
			String result = f.setenvir();
			if (result.equals("pathsuccess")) {
				
					u.unZipIt();
				

			} else {
				System.out.println("Configuration Variables not set");
				JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.WARNING_MESSAGE);
			}
			res="done";
		
		
		} catch (Exception e) {

			System.out.println("Exception in Config");
			config.MastsvCExcep.handleException(e, "Config");
			
			res="EXC";
		}
		return res;
	}

	public void makeConfigProp() {
		try {
			Properties props = new Properties();
			String dest = new File("afile").getAbsolutePath().replaceAll("afile", "");

			try {

				if (!dest.contains(";")) {

					dest = dest.replaceAll("%5b", "[").replaceAll("%5B", "[").replaceAll("%5d", "]").replaceAll("%5D", "]").replaceAll("%20", " ");
					dest = dest.replaceAll("%7b", "{").replaceAll("%7B", "{").replaceAll("%7d", "}").replaceAll("%7D", "}").replaceAll("%23", "#");
					dest = dest.replaceAll("%25", "%").replaceAll("%5e", "^").replaceAll("%5E", "^").replaceAll("%3D", "=").replaceAll("%3d", "=").replaceAll("%60", "`");

					new File(dest + "/WebClient/").mkdir();
					File f = new File(dest + "/WebClient/buildConf.properties");

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
					if (f.exists()) {
						f.delete();
						props.setProperty("p", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), "jdbc:derby:" + dest.replaceAll("\\\\", "/") + "WebClient/"));
						props.setProperty("s", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/MASTS_License.vml"));
						props.setProperty("t", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/dynanimator.vml"));
						props.setProperty("d", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/"));
						props.setProperty("r", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/MASTS_Renew_License.vml"));
						props.setProperty("rd", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/eqvivalencedest.vml"));
						props.setProperty("t1", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t1));
						props.setProperty("t2", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t2));
						
						f.createNewFile();
					} else {

						props.setProperty("p", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), "jdbc:derby:" + dest.replaceAll("\\\\", "/") + "WebClient/"));
						props.setProperty("s", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/MASTS_License.vml"));
						props.setProperty("t", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/dynanimator.vml"));
						props.setProperty("d", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/"));
						props.setProperty("r", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/MASTS_Renew_License.vml"));
						props.setProperty("rd", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), dest.replaceAll("\\\\", "/") + "/WebClient/eqvivalencedest.vml"));
						props.setProperty("t1", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t1));
						props.setProperty("t2", MastsvdAD.encrypt(c.getKey1(), c.getKey2(), t2));
						
						f.createNewFile();
					}
					FileOutputStream out = new FileOutputStream(f);
					props.store(out, "This is an optional header comment string");

					InputStream inputStream = new FileInputStream(f);
					if (inputStream != null) {
						props.load(inputStream);

					}

					inputStream.close();
					out.close();

				}

			} catch (Exception e) {

				
				config.MastsvCExcep.handleException(e, "Config");
			}

			try {

				MastsvFE fe = new MastsvFE();
				String fkey = fe.getKeygard1();
				FileInputStream fis = new FileInputStream(dest + "/WebClient/buildConf.properties");
				FileOutputStream fos = new FileOutputStream(dest + "/WebClient/build-1Conf.properties");
				try {
					MastsvCE.encrypt(fkey, fis, fos);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				config.MastsvCExcep.handleException(e, "Config");

			}

		} catch (Exception ex) {
			config.MastsvCExcep.handleException(ex, "Config");
		}
	}

	public List<String> encrypt(String key1, String key2) {

		List<String> list = new ArrayList<String>();
		String res1 = "", res2 = "";
		int l = key2.length() - 1;
		int l1 = key1.length() - 1;
		for (int i = 0; i < key1.length() / 2; i++) {

			res1 = res1 + key1.charAt(i) + key2.charAt(l);
			l = l - 1;

		}
		for (int i = 0; i < key2.length() / 2; i++) {
			res2 = res2 + key2.charAt(i) + key1.charAt(l1);
			l1 = l1 - 1;
		}
		list.add(res1);
		list.add(res2);
		return list;
	}

	public String toChange(int i) {

		return Character.toString((char) i);
	}
}
