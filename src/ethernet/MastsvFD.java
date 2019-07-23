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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import config.MastsvCong;
import config.MastsvRDT;

/**
 *
 * @author swapnil
 */
public class MastsvFD {

    Connection con;
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

    String lastaccesdate, lastaccesshour, lastaccessminit;
    Date lastaccessdate;
    File f;

    static LineNumberReader lineNumberReader = null;

    public void databsseconnection() throws SQLException {
        MastsvLP.Loadproperties();

        try {
            con = config.MastsvConn.create(MastsvRDT.databasename.get(0));
        } catch (Exception f) {

        	config.MastsvCExcep.handleException(f, "Filedelte");
            System.out.println("FDDBCNEXC");
            con.close();
            JOptionPane.showMessageDialog(null, "Another instance of MASTS is already running! Please close other instances of MASTS and try again.", "MASTS", JOptionPane.WARNING_MESSAGE);
            MastsvA.jButton2.setEnabled(true);
            MastsvA.jButton1.setEnabled(false);
            MastsvA.jButton3.setEnabled(false);
        }
    }

    public String gettingfiledata() throws FileNotFoundException, IOException {
        try {

            MastsvLP.Loadproperties();
           
            lineNumberReader = new LineNumberReader(new FileReader(MastsvLP.target));
            String line = null;

            Vector<String> data = new Vector<String>();
            while ((line = lineNumberReader.readLine()) != null) {

                data.add(line);

            }

            ArrayList<String> ar = new ArrayList<String>(data);
            Iterator<String> i = ar.iterator();
            f1 = (String) i.next();
            i.hasNext();
            fileekey = (String) i.next();
            i.hasNext();
            f2 = (String) i.next();
            i.hasNext();
            name = (String) i.next();
            i.hasNext();
            f3 = (String) i.next();
            i.hasNext();
            filestartdate = (String) i.next();
            i.hasNext();
            f4 = (String) i.next();
            i.hasNext();
            fileexpdate = (String) i.next();
            lineNumberReader.close();

            filedatadecrpetion();
            return "Filedeletefiledatafound";
        } catch (Exception dd) {
            System.out.println("FDFILEDTFOEXC");
            config.MastsvCExcep.handleException(dd, "Filedelte");
            return "Filedeletefiledatafail";
        }
    }

    public void filedatadecrpetion() {

        MastsvMB filebean = new MastsvMB();
        

        filedeckeycompare = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), fileekey);

        String fileactdatedecr = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), filestartdate);

        String fileexpirydecr = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), fileexpdate);

        MastsvCB dateenkey = new MastsvCB();
      
        String gotstartdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileactdatedecr);

        String gotexpdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileexpirydecr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            filestartdatecomp = sdf.parse(gotstartdate);
            fileexpdatecomp = sdf.parse(gotexpdate);
        } catch (Exception g) {
        	 config.MastsvCExcep.handleException(g, "Filedelte");
        	 System.out.println("FDFILEDTDECEXC");
        }
    }

    public String getdatabasedata() {
        try {

            String lastacces = null;
            databsseconnection();
            Statement stt = con.createStatement();
            ResultSet rs = stt.executeQuery("select * from "+MastsvRDT.tablename_1.get(0)+"");
            while (rs.next()) {
                databaseekey = rs.getString(1);
                databasestartdate = rs.getString(2);
                databaseexpdate = rs.getString(3);
                lastacces = rs.getString(4);

                lastaccesshour = rs.getString(8);
                lastaccessminit = rs.getString(9);
            }
            MastsvMB dateen = new MastsvMB();
         
            lastaccesdate = MastsvdAD.decrypt(dateen.getMkey1(), dateen.getMkey2(), lastacces);

            decrpetiondatabasedata();
            return "Filedeleteddabasedatafound";
        } catch (Exception e) {
        	 config.MastsvCExcep.handleException(e, "Filedelte");
            System.out.println("FDDAFOUNDEXC");
            JOptionPane.showMessageDialog(null, "Another instance of MASTS is already running! Please close other instances of MASTS and try again.", "MASTS", JOptionPane.ERROR_MESSAGE);
            MastsvA.jButton1.setEnabled(false);
            MastsvA.jButton3.setEnabled(false);
            MastsvA.jLabel15.setVisible(false);
            MastsvA.jButton2.setEnabled(true);
            return "Filedeleteddabasedatanotfound";

        }
    }

    public void decrpetiondatabasedata() {
        MastsvMB filebean = new MastsvMB();
        databasedeckeycompare = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), databaseekey);
        String databaseactdatedecr = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), databasestartdate);
        String databaseexpirydecr = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), databaseexpdate);

        MastsvCB dateenkey = new MastsvCB();
      
        String gotdatabasestartdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), databaseactdatedecr);

        String gotdatabaseexpdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), databaseexpirydecr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            databasestartdatecomp = sdf.parse(gotdatabasestartdate);
            databaseexpdatecomp = sdf.parse(gotdatabaseexpdate);
            lastaccessdate = sdf.parse(lastaccesdate);

        } catch (Exception g) {
        	 config.MastsvCExcep.handleException(g, "Filedelte");
            System.out.println("FDDATADECEXC");

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
        	 config.MastsvCExcep.handleException(e, "Filedelte");
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
        	 config.MastsvCExcep.handleException(e, "Filedelte");
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
        	 config.MastsvCExcep.handleException(e, "Filedelte");
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
        	config.MastsvCExcep.handleException(e, "Filedelte");
            System.exit(1);
        }
        if (result.trim().length() < 1) {
            System.exit(1);
        }

        return result.trim();
    }

    public String getsystemdata() {
        try {
        	MastsvCong u=new MastsvCong();
        	serialnumberC = MastsvFD.getSerialNumber("C:\\");

            int hddhash = serialnumberC.hashCode();

            systemmac = MastsvFD.getMACAddress();
           
            systemnewid = MastsvFD.getnewidfunction();
            String motherboardserial = MastsvFD.getcpuid();
            String first=u.toChange(77)+u.toChange(65)+u.toChange(83)+u.toChange(84)+u.toChange(83)+u.toChange(84)+u.toChange(114)+u.toChange(101)+u.toChange(101)+u.toChange(68)+u.toChange(48)+u.toChange(77);
            String dd=systemmac+systemnewid+first;
            int machash = dd.hashCode();
            int motherid = motherboardserial.hashCode();
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

            systemcurrentdatecomp = sdf.parse(d1);
            Calendar cal = Calendar.getInstance();
            cal.getTime();
            SimpleDateFormat sdfc = new SimpleDateFormat("HH:mm:ss");
            String curtime = sdfc.format(cal.getTime());

            systemcurenttimecomp = sdfc.parse(curtime);
            return "filedeletesystemdatafound";

        } catch (Exception fp) {

        	config.MastsvCExcep.handleException(fp, "Filedelte");
            System.out.println("FDSYTDTFOEXC");
            return "filedeletesystemdatafail";
        }

    }

    public void databaseupdate() {
        try {
            int i = 0;
            Date dd = new Date();
            SimpleDateFormat t = new SimpleDateFormat("yyyy/MM/dd");
            String date1 = t.format(dd);
            Date date = new Date();

            MastsvMB dateen = new MastsvMB();
            
            String curdatedencindeletefile;
            curdatedencindeletefile = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), date1);
           

            String strDateFormat = "HH:mm:ss a";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

            String mydata = sdf.format(date);

            StringTokenizer st = new StringTokenizer(mydata, " ");
            String actualtime = st.nextToken();
            st.hasMoreElements();
            

            String[] time1 = actualtime.split(":");
            int hour = Integer.parseInt(time1[0]);
            int minit = Integer.parseInt(time1[1]);

            String encrepthour, encreptminit;
            encrepthour = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), "" + hour);
          
            encreptminit = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), "" + minit);
           
            databsseconnection();
            String slq = "update "+MastsvRDT.tablename_1.get(0)+" set ACCCH4=?,ACCCH8=?,ACCCH9=? where  ACCCH1=? ";
            PreparedStatement ps = con.prepareStatement(slq);
            ps.setString(1, curdatedencindeletefile);

            ps.setString(2, "" + encrepthour);
            ps.setString(3, "" + encreptminit);

            ps.setString(4, databaseekey);
            i = ps.executeUpdate();
            if (i > 0) {

                MastsvA.jButton2.setEnabled(false);
                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                MastsvA.jButton1.setEnabled(true);
                MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
            } else {
                System.out.println("FDVALIDLICUPDATAFAILCOLUMN");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please try again. ", "MASTS", JOptionPane.WARNING_MESSAGE);
                MastsvA.jButton1.setEnabled(false);
            }
        } catch (Exception r) {
        	config.MastsvCExcep.handleException(r, "Filedelte");
            System.out.println("FDVALIDLICUPDATAFAIL");

        }
    }

    public String checklicence() {
        try {
            if ((filedeckeycompare.equals(systemproductkeyencreptioncomp)) && (databasedeckeycompare.equals(systemproductkeyencreptioncomp))) {
                if ((systemcurrentdatecomp.equals(filestartdatecomp) && systemcurrentdatecomp.equals(databasestartdatecomp)) || ((systemcurrentdatecomp.after(filestartdatecomp) && systemcurrentdatecomp.after(databasestartdatecomp)) && (systemcurrentdatecomp.before(fileexpdatecomp)) && systemcurrentdatecomp.before(databaseexpdatecomp)) || (systemcurrentdatecomp.equals(fileexpdatecomp) && systemcurrentdatecomp.equals(databaseexpdatecomp))) {

                    if (lastaccessdate.before(systemcurrentdatecomp) || lastaccessdate.equals(systemcurrentdatecomp)) {

                        if (systemcurrentdatecomp.equals(fileexpdatecomp) && systemcurrentdatecomp.equals(databaseexpdatecomp)) {

                            databaseupdate();
                            JOptionPane.showMessageDialog(null, "Dear Customer, your MASTS license will expire today!\nYou need to renew your MASTS license to continue using it.\nPlease contact MASTS support team for renewal options.", "MASTS License Alert!", JOptionPane.INFORMATION_MESSAGE);
                            return "filedeletelicencesuccess";

                        } else {
                            databaseupdate();

                            return "filedeletelicencesuccess";

                        }

                    } else {

                        System.out.println("FDCHECKLICFAIL");
                        return "filedeletelicencesuccessfail";

                    }
                } else {

                    System.out.println("FDCHECKLICFAIL");
                    return "filedeletelicencesuccessfail";
                }
            } else {

                System.out.println("FDCHECKLICFAILKEY");
                return "filedeletelicencesuccessfail";
            }

        } catch (Exception e) {

        	config.MastsvCExcep.handleException(e, "Filedelte");
            System.out.println("FDCHECKLICFAIL");
            return "filedeletelicencesuccessfail";
        }

    }
    
    public void deleteAllFiles(){
    	boolean gotSQLExc = false;
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
        	config.MastsvCExcep.handleException(se, "Filedelte");
            if (se.getSQLState().equals("XJ015")) {
                gotSQLExc = true;
            }
        }
        if (!gotSQLExc) {

        } else {
            System.out.println("MASTS Processing Started..");
        }
        
        
        MastsvLP.Loadproperties();
        String dd=MastsvLP.dummypath+MastsvRDT.databasename.get(0);
        deleteFolder(dd);
        dd=MastsvLP.dummypath+"Log";
        deleteFolder(dd);
        new File(MastsvLP.dummypath+"buildConf.properties");
        new File(MastsvLP.dummypath+"buildConf.properties");
        new File(MastsvLP.dummypath+"build-1Conf.properties");
        
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
