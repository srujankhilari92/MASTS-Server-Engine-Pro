package ethernet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import config.MastsvCong;
import config.MastsvRDT;

/**
 *
 * @author swapnil
 */
public class MastsvRFV {

    MastsvA act;
    String key, clientname, actdate, expdate;
    String second, fourth;
    static LineNumberReader lineNumberReader = null;
    Connection con;
    Statement stmt;

    String fileekey, filestartdate, fileexpdate;
    String filedeckeycompare;
    Date filestartdatecomp, fileexpdatecomp;
    String f1, f2, f3, f4, name;

    String databasedeckeycompare;
    String databaseekey, databasestartdate, databaseexpdate;
    Date databasestartdatecomp, databaseexpdatecomp;

    String serialnumberC, systemmac, systemnewid, motherboardserial;
    String systemproductkeyencreptioncomp;
    Date systemcurrentdatecomp, systemcurenttimecomp;

    String lastaccesdate;
    Date lastaccessdate;
    File f, filech, targetfile, startfile;
    public static String firstlicenceversion;
    public static String licencehddid;

    public String filecheck() {
        MastsvLP.Loadproperties();
        filech = new File(MastsvLP.renewpath);
        if (filech.exists()) {
            return "fileok";
        } else {
            return "filemiss";
        }
    }

    public String startfilecreate() {
        MastsvLP.Loadproperties();
        startfile = new File("" + MastsvLP.dummypath + "aaiclient.vml");
        if (startfile.exists()) {
            return "alreadyexist";
        } else {
            try {
                startfile.createNewFile();
                return "filecreate";
            } catch (IOException ex) {
            	config.MastsvCExcep.handleException(ex, "Renfilevalidation");
            	
                Logger.getLogger(MastsvRFV.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return "";
    }

    public String updatedatabaseforrenew() {
        try {
            con = config.MastsvConn.create(MastsvRDT.databasename.get(0));
            int y = 0;

            String updatedlicence = "update "+MastsvRDT.tablename_1.get(0)+" set ACCCH2=?,ACCCH3=? where ACCCH1=?";
            PreparedStatement ps = con.prepareStatement(updatedlicence);

            ps.setString(1, actdate);
            ps.setString(2, expdate);
            ps.setString(3, key);
            y = ps.executeUpdate();
            if (y > 0) {

                return "licencerenewdatabaseupdated";
            } else {
                System.out.print("LCRENUPDBFL");
                return "licencerenewdatabasefailtoupdate";

            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Renfilevalidation");
            System.out.print("LCRENDBEXC");
            return "licencerenewdatabasefailtoupdate";
        }
    }

    public String Licencefilerename() {
        try {
            String to = MastsvLP.source;

            File newfile = new File(to);
            newfile.delete();

            if (filech.renameTo(newfile)) {

                return "filerenameok";
            } else {

                System.out.print("LCRENRNMOP");
                return "filerenamefailtook";
            }
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Renfilevalidation");
            System.out.print("LCRENRNMOP");
            return "filerenamefailtook";
        }

    }

    public String secondLicencefilerename() {

        String to = MastsvLP.target;

        File newfile = new File(to);
        newfile.delete();
        if (targetfile.renameTo(newfile)) {

            return "filerenameok";
        } else {

            return "filerenamefailtook";
        }

    }

    public String filevalidationfirststep() {
        getsystemdata();
        try {
            String filestatus = filecheck();

            if (filestatus.equals("fileok")) {
                String targetf = targetfilecheck();

                if (targetf.equals("targetfound")) {
                    checksystem();

                    if ((filedeckeycompare.equals(systemproductkeyencreptioncomp))) {

                        if ((systemcurrentdatecomp.equals(filestartdatecomp)) || ((systemcurrentdatecomp.after(filestartdatecomp)) && (systemcurrentdatecomp.before(fileexpdatecomp))) || (systemcurrentdatecomp.equals(fileexpdatecomp))) {
                            if (systemcurrentdatecomp.equals(fileexpdatecomp)) {

                                JOptionPane.showMessageDialog(null, "Congratulations! Your license is activated for one day.", "MASTS License Alert!", JOptionPane.INFORMATION_MESSAGE);
                                MastsvA.jButton1.setEnabled(true);
                                MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                MastsvA.jButton2.setEnabled(false);
                                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                startfilecreate();
                                String databasestatus = updatedatabaseforrenew();
                                if (databasestatus.equals("licencerenewdatabaseupdated")) {

                                    String renamest = Licencefilerename();
                                    if (renamest.equals("filerenameok")) {
                                        String renamest1 = secondLicencefilerename();
                                        if (renamest1.equals("filerenameok")) {
                                            return "renewfilevalidatesuccess";
                                        } else {
                                            System.out.print("LCRENVALFL");
                                            filech.deleteOnExit();
                                            filech.delete();
                                            return "failtorenew";
                                        }
                                    } else {
                                        System.out.print("LCRENVALFL");
                                        filech.deleteOnExit();
                                        filech.delete();
                                        return "failtorenew";
                                    }
                                } else {
                                    System.out.print("LCRENVALFL");
                                    filech.deleteOnExit();
                                    filech.delete();
                                    return "failtorenew";
                                }

                            } else {

                                JOptionPane.showMessageDialog(null, "Congratulations! Your license is renewed successfully.", "MASTS License Alert!", JOptionPane.INFORMATION_MESSAGE);
                                MastsvA.jButton1.setEnabled(true);
                                MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                MastsvA.jButton2.setEnabled(false);
                                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                startfilecreate();
                                String databasestatus = updatedatabaseforrenew();
                                if (databasestatus.equals("licencerenewdatabaseupdated")) {
                                    String renamest = Licencefilerename();
                                    if (renamest.equals("filerenameok")) {
                                        String renamest1 = secondLicencefilerename();
                                        if (renamest1.equals("filerenameok")) {
                                            return "renewfilevalidatesuccess";
                                        } else {
                                            filech.deleteOnExit();
                                            filech.delete();
                                            return "failtorenew";
                                        }
                                    } else {
                                        filech.deleteOnExit();
                                        filech.delete();
                                        return "failtorenew";
                                    }
                                } else {
                                    filech.deleteOnExit();
                                    filech.delete();
                                    return "failtorenew";
                                }

                            }

                        } else {
                            System.out.print("LCRENVALFL");
                            JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);
                            MastsvA.jButton1.setEnabled(false);
                            MastsvA.jButton2.setEnabled(true);
                            filech.deleteOnExit();
                            filech.delete();
                            return "renewfilevalidatefaleforreason";

                        }
                    } else {
                        System.out.print("LCRENVALFL");
                        JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);
                        MastsvA.jButton1.setEnabled(false);
                        MastsvA.jButton2.setEnabled(true);
                        filech.deleteOnExit();
                        filech.delete();

                        return "renewfilevalidatefaleforreason";

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);
                    MastsvA.jButton1.setEnabled(false);
                    MastsvA.jButton2.setEnabled(false);
                    MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                    MastsvA.jButton3.setEnabled(true);
                    filech.deleteOnExit();
                    filech.delete();
                    return "renewfilevalidatefaleforreason";

                }
            } else {

                JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);
                MastsvA.jButton1.setEnabled(false);
                MastsvA.jButton2.setEnabled(false);
                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                MastsvA.jButton3.setEnabled(true);
                return "renewfilevalidatefaleforreason";
            }
        } catch (Exception g) {
            JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);
            MastsvA.jButton1.setEnabled(false);
            MastsvA.jButton2.setEnabled(true);
            MastsvA.jButton3.setEnabled(false);
            filech.deleteOnExit();
            filech.delete();
            config.MastsvCExcep.handleException(g, "Renfilevalidation");
            return "renewfilevalidatefaleforreason";
        }

    }

    public String targetfilecheck() {
        MastsvLP.Loadproperties();
        targetfile = new File(MastsvLP.renewdestpath);
        if (targetfile.exists()) {
            return "targetfound";
        } else {
            return "targetfail";
        }
    }

    public void checksystem() throws FileNotFoundException, IOException, SQLException {
        MastsvLP.Loadproperties();
        
        lineNumberReader = new LineNumberReader(new FileReader(MastsvLP.renewdestpath));
        String line = null;
        Vector<String> data = new Vector<String>();
        while ((line = lineNumberReader.readLine()) != null) {

            data.add(line);

        }

        ArrayList<String> ar = new ArrayList<String>(data);
        Iterator<String> i = ar.iterator();
        firstlicenceversion = (String) i.next();
        i.hasNext();
        key = (String) i.next();
        i.hasNext();
        second = (String) i.next();
        i.hasNext();
        clientname = (String) i.next();
        i.hasNext();
        licencehddid = (String) i.next();
        i.hasNext();
        actdate = (String) i.next();
        i.hasNext();
        fourth = (String) i.next();
        i.hasNext();
        expdate = (String) i.next();
        lineNumberReader.close();
        getsystemdata();
        filedatadecrpetion();

    }

    public void filedatadecrpetion() {

        MastsvMB filebean = new MastsvMB();
        

        filedeckeycompare = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), key);

        String fileactdatedecr = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), actdate);

        String fileexpirydecr = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), expdate);

        MastsvCB dateenkey = new MastsvCB();
        

        String gotstartdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileactdatedecr);

        String gotexpdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileexpirydecr);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            filestartdatecomp = sdf.parse(gotstartdate);
            fileexpdatecomp = sdf.parse(gotexpdate);

        } catch (Exception g) {
            System.out.println("LCRNFLDTDECFL");

            config.MastsvCExcep.handleException(g, "Renfilevalidation");
        }
    }

    public void getsystemdata() {

        serialnumberC = MastsvRFV.getSerialNumber("C:\\");

        int hddhash = serialnumberC.hashCode();

        systemmac = MastsvRFV.getMACAddress();
        systemnewid = MastsvRFV.getnewidfunction();
        String motherboardserial = MastsvRFV.getcpuid();

        int motherid = motherboardserial.hashCode();
        MastsvCong u=new MastsvCong();
        String first=u.toChange(77)+u.toChange(65)+u.toChange(83)+u.toChange(84)+u.toChange(83)+u.toChange(84)+u.toChange(114)+u.toChange(101)+u.toChange(101)+u.toChange(68)+u.toChange(48)+u.toChange(77);
        String dd=systemmac+systemnewid+first;
        int machash = dd.hashCode();
        int newid = systemnewid.hashCode();
        String sysmother = "" + motherid;
       
        String hddid = "" + hddhash + machash + newid + sysmother;

        MastsvCB firstenkey = new MastsvCB();
       
        String salt = "@v@r^t$@";
        String firststepencreptionofrhdd = MastsvdAD.encrypt(firstenkey.getKey1(), firstenkey.getKey2(), hddid);
        String firststepencreptionofrserial = MastsvdAD.encrypt(firstenkey.getKey1(), firstenkey.getKey2(), salt);
        String combine = firststepencreptionofrhdd + "$-%" + firststepencreptionofrserial;
        MastsvCB productkey = new MastsvCB();
      
        systemproductkeyencreptioncomp = MastsvdAD.encrypt(productkey.getKey1(), productkey.getKey2(), combine);
       
        Date d = new Date();
        SimpleDateFormat t = new SimpleDateFormat("yyyy/MM/dd");
        String d1 = t.format(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            systemcurrentdatecomp = sdf.parse(d1);
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdfc = new SimpleDateFormat("HH:mm:ss");
            String curtime = sdfc.format(cal.getTime());

            systemcurenttimecomp = sdfc.parse(curtime);

        } catch (Exception r) {
            System.out.println("LCRNSTDTDECFL");
            config.MastsvCExcep.handleException(r, "Renfilevalidation");
        }

    }

    public static String getcpuid() {

        String result = "";

       
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"wbem/wmic", "cpu", "get", "name"});
        } catch (IOException e) {
        	config.MastsvCExcep.handleException(e, "Renfilevalidation");
            throw new RuntimeException(e);
        }

       
        is = process.getInputStream();
        ArrayList<String> al = new ArrayList<String>();
        BufferedReader input = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = input.readLine()) != null) {

                al.add(line);
            }

            result = al.get(2).toString();
            is.close();
        } catch (IOException e) {
        	config.MastsvCExcep.handleException(e, "Renfilevalidation");
            e.printStackTrace();
        }

        return result;
    }

    public static String getnewidfunction() {
        String result = "";
        try {
            File file = File.createTempFile("GetMBSerial", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs
                    = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n"
                    + "   (\"Select * from Win32_ComputerSystemProduct\") \n"
                    + "For Each objItem in colItems \n"
                    + "    Wscript.Echo objItem.IdentifyingNumber \n"
                    + "Next \n";

            fw.write(vbs);
            fw.close();
            Process gWMI = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(gWMI.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;

            }
            input.close();
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Renfilevalidation");
            e.printStackTrace();
        }
        result = result.trim();

        return result;
    }

    public static String getMACAddress() {
		String strMAC = "None";
		MastsvCong u=new MastsvCong();
		try {
			boolean isdbit = false;
			if (System.getProperty("os.name").contains("Windows")) {
				isdbit = (System.getenv("ProgramFiles") != null);
			} else {
				isdbit = (System.getProperty("os.arch").indexOf("64") != -1);
			}
			
			
			System.out.println("Result: "+isdbit);
			if(isdbit){
				strMAC="^$-"+u.toChange(66)+u.toChange(73)+u.toChange(84);
			}else{
				strMAC="#@-"+u.toChange(66)+u.toChange(73)+u.toChange(84);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strMAC;
	}

    public static String getSerialNumber(String string) {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\"" + string + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Renfilevalidation");
            System.exit(1);
        }
        if (result.trim().length() < 1) {
            System.exit(1);
        }

        return result.trim();
    }

}
