/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Sudhakar Barde
 */
public class MastsvDVL {

    static Connection con;
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
    File f;
    public static String userstatus = "activate";
    static LineNumberReader lineNumberReader = null;

    public static String licenceversion;
    public static String licenceholder;
    public static String licencehddid;
    public static String licenceexpirydate;

    public static String licencefinalversion;
    public static String licencefinalholdername;
    public static String licencefinalhdd;
    public static String licencefinalexpdate;

    public static String lastacchour, lastaccessminit, lastaccessdatenoenc;

    int systmecurenthour, systemcurrentminit;
    int databaselicencelastaccesshour, databaselastaccessmint;
    String mydatazone, finalzone;
    java.util.Date date = new java.util.Date();

    Date firstcheckdate;
    String curdatedencforcheckone;

    public static String lastacchourtemp, lastaccessminittemp;

    public String databsseconnection() throws SQLException {
        MastsvLP.Loadproperties();

        try {

            con = config.MastsvConn.create(MastsvRDT.databasename.get(0));
            return "dabaseconectsuccess";

        } catch (Exception f) {
        	config.MastsvCExcep.handleException(f, "Datlicence");
            return "dabaseconectfail";
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

            String filedecstatus = filedatadecrpetion();
            if (filedecstatus.equals("Filadatadecrpetok")) {

                return "Filedatafound";
            } else {

                return "Filedatafail";
            }
        } catch (Exception dd) {

        	config.MastsvCExcep.handleException(dd, "Datlicence");
            return "Filedatafail";

        }

    }

    public String filedatadecrpetion() {

        MastsvMB filebean = new MastsvMB();
     

        filedeckeycompare =  MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), fileekey);

        String fileactdatedecr =  MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), filestartdate);

        String fileexpirydecr =  MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), fileexpdate);

        MastsvCB dateenkey = new MastsvCB();
       
        String gotstartdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileactdatedecr);

        String gotexpdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileexpirydecr);

        licencefinalexpdate = gotexpdate;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            filestartdatecomp = sdf.parse(gotstartdate);
            fileexpdatecomp = sdf.parse(gotexpdate);

            return "Filadatadecrpetok";

        } catch (Exception g) {
        	config.MastsvCExcep.handleException(g, "Datlicence");
            return "Filadatadecrpetofailhere";
        }
    }

    public String getdatabasedata() throws SQLException {
        try {

            String currentzone = java.util.TimeZone.getDefault().getID();

            String databsestatus = databsseconnection();
            if (databsestatus.equals("dabaseconectsuccess")) {

                Statement stt = con.createStatement();
                ResultSet rs = stt.executeQuery("select * from "+MastsvRDT.tablename_1.get(0)+"");
                while (rs.next()) {
                    databaseekey = rs.getString(1);
                    databasestartdate = rs.getString(2);
                    databaseexpdate = rs.getString(3);
                    lastaccessdatenoenc = rs.getString(4);

                    licenceversion = rs.getString(5);
                    licenceholder = rs.getString(6);
                    licencehddid = rs.getString(7);

                    lastacchourtemp = rs.getString(8);

                    lastaccessminittemp = rs.getString(9);
                    mydatazone = rs.getString(10);
                    userstatus = rs.getString(11);
                }

                licencefinalversion = licenceversion;
                licencefinalhdd = licencehddid;
                licencefinalholdername = licenceholder;

                String decdatabasestutus = decrpetiondatabasedata();
                if (decdatabasestutus.equals("databasedecreptionok")) {

                    if (finalzone.equals(currentzone)) {

                        return "getdatabasedatafound";
                    } else {

                        System.out.println("DTVALZNCHFD");
                        return "getdatabasedataexception";
                    }
                } else {

                    System.out.println("DTVALDBDTNTDEC");
                    return "getdatabasedataexception";
                }
            } else {
                System.out.println("DTVALDBDNTCT");
                return "getdatabasedataexception";
            }
        } catch (Exception e) {

        	config.MastsvCExcep.handleException(e, "Datlicence");
            System.out.println("DTVALDBDTEXC");

            return "getdatabasedataexception";
        }
    }

    public String decrpetiondatabasedata() {

        MastsvMB filebean = new MastsvMB();
      
        databasedeckeycompare = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), databaseekey);

        lastaccesdate = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), lastaccessdatenoenc);
        lastacchour = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), lastacchourtemp);
        lastaccessminit = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), lastaccessminittemp);

        finalzone = MastsvdAD.decrypt(filebean.getMkey1(), filebean.getMkey2(), mydatazone);

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

            licenceexpirydate = "" + databaseexpdatecomp;

            licencefinalexpdate = licenceexpirydate;

            return "databasedecreptionok";
        } catch (Exception g) {

        	config.MastsvCExcep.handleException(g, "Datlicence");
            MastsvA.jButton1.setEnabled(false);
            MastsvA.jButton3.setEnabled(false);
            MastsvA.jLabel15.setVisible(false);
            MastsvA.jButton2.setEnabled(true);

            MastsvA.jLabel16.setVisible(false);
            MastsvA.jLabel17.setVisible(false);
            MastsvA.jLabel19.setVisible(false);

            System.out.println("XCPNDECDTDT");
            return "databasedecreptionfail";
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
        	config.MastsvCExcep.handleException(e, "Datlicence");
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

        	config.MastsvCExcep.handleException(e, "Datlicence");
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
        	config.MastsvCExcep.handleException(e, "Datlicence");
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

        	config.MastsvCExcep.handleException(e, "Datlicence");
            System.exit(1);
        }
        if (result.trim().length() < 1) {
            System.exit(1);
        }

        return result.trim();
    }

    public String getsystemdata() {
        try {
            serialnumberC = MastsvDVL.getSerialNumber("C:\\");
            int hddhash = serialnumberC.hashCode();

            systemmac = MastsvDVL.getMACAddress();
            systemnewid = MastsvDVL.getnewidfunction();
            MastsvCong u=new MastsvCong();
            String first=u.toChange(77)+u.toChange(65)+u.toChange(83)+u.toChange(84)+u.toChange(83)+u.toChange(84)+u.toChange(114)+u.toChange(101)+u.toChange(101)+u.toChange(68)+u.toChange(48)+u.toChange(77);
            String dd=systemmac+systemnewid+first;
            int machash = dd.hashCode();
            motherboardserial = MastsvDVL.getcpuid();
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

            return "systemdatafound";
        } catch (Exception ff) {

        	config.MastsvCExcep.handleException(ff, "Datlicence");
            return "systemdatafail";
        }

    }

    public String databaseupdate() {
        try {

            Date date = new Date();

            String strDateFormat = "HH:mm:ss a";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

            String mydata = sdf.format(date);

            StringTokenizer st = new StringTokenizer(mydata, " ");
            String actualtime = st.nextToken();
            st.hasMoreElements();
           
            String[] time1 = actualtime.split(":");
            int hour = Integer.parseInt(time1[0]);
            int minit = Integer.parseInt(time1[1]);
            int i = 0;
            Date dd = new Date();
            SimpleDateFormat t = new SimpleDateFormat("yyyy/MM/dd");
            String date1 = t.format(dd);

            MastsvMB dateen = new MastsvMB();
            
            String currentdaupdatedencrepted;
            currentdaupdatedencrepted = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), date1);
           
            String encrepthour, encreptminit;
            encrepthour = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), "" + hour);
           

            encreptminit = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), "" + minit);
           
            databsseconnection();
            String slq = "update "+MastsvRDT.tablename_1.get(0)+" set ACCCH4=?,ACCCH8=?,ACCCH9=? where  ACCCH1=? ";
            PreparedStatement ps = con.prepareStatement(slq);
            ps.setString(1, currentdaupdatedencrepted);
            ps.setString(2, "" + encrepthour);
            ps.setString(3, "" + encreptminit);
            ps.setString(4, databaseekey);
            i = ps.executeUpdate();
            if (i > 0) {

                return "databseupdatefoundok";
            } else {
                System.out.println("DBUPFALEXC");
                return "databseupdatefail";
            }
        } catch (Exception r) {
            System.out.println("DBUPFALEXC");

            config.MastsvCExcep.handleException(r, "Datlicence");
            return "databseupdatefail";
        }
    }

    public String checklicence() throws Throwable {
        try {

            Date date = new Date();

            String strDateFormat = "HH:mm:ss a";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

            String mydata = sdf.format(date);

            StringTokenizer st = new StringTokenizer(mydata, " ");
            String actualtime = st.nextToken();
            st.hasMoreElements();
          
            String[] time1 = actualtime.split(":");
            int hour = Integer.parseInt(time1[0]);
            int minit = Integer.parseInt(time1[1]);
            systmecurenthour = hour;
            systemcurrentminit = minit;
            databaselicencelastaccesshour = Integer.parseInt(lastacchour);
            databaselastaccessmint = Integer.parseInt(lastaccessminit);

            if ((filedeckeycompare.equals(systemproductkeyencreptioncomp)) && (databasedeckeycompare.equals(systemproductkeyencreptioncomp))) {

                if ((systemcurrentdatecomp.equals(filestartdatecomp) && systemcurrentdatecomp.equals(databasestartdatecomp)) || ((systemcurrentdatecomp.after(filestartdatecomp) && systemcurrentdatecomp.after(databasestartdatecomp)) && (systemcurrentdatecomp.before(fileexpdatecomp)) && systemcurrentdatecomp.before(databaseexpdatecomp)) || (systemcurrentdatecomp.equals(fileexpdatecomp) && systemcurrentdatecomp.equals(databaseexpdatecomp))) {

                    if (lastaccessdate.before(systemcurrentdatecomp) || lastaccessdate.equals(systemcurrentdatecomp)) {

                        if (systemcurrentdatecomp.equals(fileexpdatecomp) && systemcurrentdatecomp.equals(databaseexpdatecomp)) {

                            if (lastaccessdate.equals(systemcurrentdatecomp)) {

                                if (systmecurenthour >= databaselicencelastaccesshour) {

                                    if (systmecurenthour == databaselicencelastaccesshour) {
                                        if (systemcurrentminit >= databaselastaccessmint) {
                                            String dataupdatestatus = databaseupdate();

                                            if (dataupdatestatus.equals("databseupdatefoundok")) {

                                                return "Correctlicence";
                                            } else {

                                                System.out.println("LCVRFAL");
                                                System.out.println("LCVRFALDBUCHF1");
                                                return "wrong";
                                            }

                                        } else {

                                            System.out.println("LCVRFAL");
                                            System.out.println("LCVRFALSYTMLTNTMCH1");
                                            return "wrong";
                                        }

                                    } else {
                                        String dataupdatestatus = databaseupdate();
                                        if (dataupdatestatus.equals("databseupdatefoundok")) {

                                            return "Correctlicence";
                                        } else {
                                            System.out.println("LCVRFALSYHTLHTNTMCH1");
                                            System.out.println("LCVRFAL");
                                            return "wrong";
                                        }
                                    }
                                } else {
                                    System.out.println("LCVRFALSYHTISMLLHT1");
                                    System.out.println("LCVRFAL");
                                    return "wrong";
                                }
                            } else {

                                String dataupdatestatus = databaseupdate();
                                if (dataupdatestatus.equals("databseupdatefoundok")) {

                                    return "Correctlicence";
                                } else {
                                    System.out.println("LCVRFALSYDTNTMCHLSDT1");
                                    System.out.println("LCVRFAL");
                                    return "wrong";
                                }

                            }

                        } else {

                            if (lastaccessdate.equals(systemcurrentdatecomp)) {

                                if (systmecurenthour >= databaselicencelastaccesshour) {

                                    if (systmecurenthour == databaselicencelastaccesshour) {
                                        if (systemcurrentminit >= databaselastaccessmint) {
                                            String dataupdatestatus = databaseupdate();
                                            if (dataupdatestatus.equals("databseupdatefoundok")) {

                                                return "Correctlicence";
                                            } else {
                                                System.out.println("LCVRFAL");
                                                System.out.println("LCVRFALDBUCHF2");
                                                return "wrong";
                                            }
                                        } else {
                                            System.out.println("LCVRFAL");
                                            System.out.println("LCVRFALSYTMLTNTMCH2");
                                            return "wrong";
                                        }

                                    } else {
                                        String dataupdatestatus = databaseupdate();
                                        if (dataupdatestatus.equals("databseupdatefoundok")) {

                                            return "Correctlicence";
                                        } else {
                                            System.out.println("LCVRFALSYHTLHTNTMCH2");
                                            System.out.println("LCVRFAL");

                                            return "wrong";
                                        }
                                    }
                                } else {
                                    System.out.println("LCVRFAL");
                                    System.out.println("LCVRFALSYHTISMLLHT2");
                                    return "wrong";
                                }
                            } else {
                                String dataupdatestatus = databaseupdate();
                                if (dataupdatestatus.equals("databseupdatefoundok")) {

                                    return "Correctlicence";
                                } else {
                                    System.out.println("LCVRFALSYDTNTMCHLSDT2");
                                    System.out.println("LCVRFAL");
                                    return "wrong";
                                }
                            }

                        }

                    } else {
                        System.out.println("LCVRFAL");
                        System.out.println("LCVRFALSYTMLTNTMCH12");
                        return "Wrong";
                    }
                } else {

                    if (systemcurrentdatecomp.after(fileexpdatecomp) && systemcurrentdatecomp.after(databaseexpdatecomp)) {

                        int response = JOptionPane.showConfirmDialog(null, "Dear Customer, your MASTS license has expired!\nYou will need to purchase a license to continue using MASTS.\nDo you want to renew your license?", "MASTS License Alert!",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (response == JOptionPane.NO_OPTION) {

                            return "usernotinterestedtorenew";
                        } else if (response == JOptionPane.YES_OPTION) {
                            MastsvRL r = new MastsvRL();

                            String myrenewstatus = r.uploadactionforrenew();
                            if (myrenewstatus.equals("okfileisrenew")) {

                                return "licencerenewsuccessfullyrestart";
                            } else {
                                System.out.println("LCVRFALNTSTRT");
                                return "failtorenew";
                            }
                        } else if (response == JOptionPane.CLOSED_OPTION) {

                            return "usernotinterestedtorenew";
                        }
                        return "";
                    } else {
                        System.out.println("LCVRFAL");
                        System.out.println("LCVRFALSTDTFLDTADDBEXPDT");
                        return "Wrong";
                    }
                }

            } else {
                System.out.println("LCVRFAL");
                System.out.println("LCVRFALLCNKYADSYTKYNTMCH");
                return "Wrong";
            }

        } catch (Exception df) {
        	config.MastsvCExcep.handleException(df, "Datlicence");
            MastsvA.jLabel15.setVisible(true);
            MastsvA.jLabel16.setVisible(false);
            MastsvA.jLabel17.setVisible(false);
            MastsvA.jLabel19.setVisible(false);

            System.out.println("LCVRFAL");
            System.out.println("LCVRFALEXPTNLST");
            return "licencefail";

        }

    }

}
