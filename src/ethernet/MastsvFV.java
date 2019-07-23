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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
 * @author Sudhakar Barde
 */

public class MastsvFV {

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
    String mynewcheck1system;
    java.util.Date date = new java.util.Date();

    public String filecheck() {
        MastsvLP.Loadproperties();
        filech = new File(MastsvLP.source);
        if (filech.exists()) {
            return "fileok";
        } else {
            System.out.println("NEWUSRSRCFILNTFOU");
            return "filemiss";
        }
    }

    public String startfilecreate() {
        MastsvLP.Loadproperties();
        startfile = new File("" + MastsvLP.dummypath + "aaiclient.vml");
        if (startfile.exists()) {
            return "filecreate";
        } else {
            try {
                startfile.createNewFile();

                return "filecreate";
            } catch (IOException ex) {
            	config.MastsvCExcep.handleException(ex, "Filvalid");
                Logger.getLogger(MastsvFV.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("NEWUSRAAICLTFAIL");
                return "filecretefail";
            }

        }

    }

    public String filevalidationfirststep() {

        try {
            String systemdatafetchstatus = getsystemdata();
            if (systemdatafetchstatus.equals("getsystemdatasuccess")) {
                String filestatus = filecheck();

                if (filestatus.equals("fileok")) {
                    String targetf = targetfilecheck();

                    if (targetf.equals("targetfound")) {
                        String checksystemstatus = checksystem();
                        if (checksystemstatus.equals("Checksystemgetsuccess")) {

                            MastsvVFC frd = new MastsvVFC();
                            String userst = MastsvA.newuserstatus;

                            String fraudstatusget = frd.pingvarutralicence(licencehddid, userst);
                            if (fraudstatusget.equals("validatesuccess")) {
                                if ((filedeckeycompare.equals(systemproductkeyencreptioncomp))) {

                                    if ((systemcurrentdatecomp.equals(filestartdatecomp)) || ((systemcurrentdatecomp.after(filestartdatecomp)) && (systemcurrentdatecomp.before(fileexpdatecomp))) || (systemcurrentdatecomp.equals(fileexpdatecomp))) {

                                        MastsvGUU updateuser = new MastsvGUU();
                                        String getresult = updateuser.pingvarutralicenceupdateuser(licencehddid, MastsvA.newuserstatus);
                                        if (getresult.equals("validatesuccess")) {
                                            if (systemcurrentdatecomp.equals(fileexpdatecomp)) {

                                                String databsestatus = createdatabase();
                                                if (databsestatus.equals("databasecreate")) {

                                                    JOptionPane.showMessageDialog(null, "Congratulations! Your license is activated for one day.", "MASTS License Alert!", JOptionPane.INFORMATION_MESSAGE);

                                                    String aaiclientstatus = startfilecreate();
                                                    if (aaiclientstatus.equals("filecreate")) {

                                                        if (MastsvA.newuserstatus.equals("UserReinstallLicenceHere")) {
                                                            return "filevalidationiscomplete";

                                                        } else {
                                                            String checkstatus = checkscreate();
                                                            if (checkstatus.equals("doneallcheckscreated") || checkstatus.equals("okalreadyexistnoneedtocreate")) {

                                                                return "filevalidationiscomplete";

                                                            } else {
                                                                System.out.println("NEWUSRRGCRTTFAIL");
                                                                return "filevalidationgetfail";
                                                            }
                                                        }
                                                    } else {
                                                        System.out.println("NEWUSRAAICLTFAIL");
                                                        return "filevalidationgetfail";
                                                    }

                                                }else  if(databsestatus.equals("failservervalidation")){
                                                	JOptionPane.showMessageDialog(null, "Sorry! Not allowed to reinstall Trial Edition.\nPlease contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                                    filech.deleteOnExit();
                                                    filech.delete();
                                                    System.out.println("VALUSRVAFAILRST");
                                                    
                                                    MastsvFD f=new MastsvFD();
                                                    f.deleteAllFiles();
                                                    return "filevalidationgetfail";
                                                } else {
                                                    JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                                    filech.deleteOnExit();
                                                    filech.delete();

                                                    System.out.println("NEWUSRDBCRTFAIL");
                                                    return "filevalidationgetfail";

                                                }

                                            } else {

                                                String databsesta = createdatabase();

                                                if (databsesta.equals("databasecreate")) {
                                                    JOptionPane.showMessageDialog(null, "Congratulations! Your license is activated successfully. ", "MASTS License Alert!", JOptionPane.INFORMATION_MESSAGE);

                                                    String aaiclientstatus = startfilecreate();
                                                    if (aaiclientstatus.equals("filecreate")) {
                                                        if (MastsvA.newuserstatus.equals("UserReinstallLicenceHere")) {

                                                            return "filevalidationiscomplete";

                                                        } else {
                                                            String checkstatus = checkscreate();
                                                            if (checkstatus.equals("doneallcheckscreated") || checkstatus.equals("okalreadyexistnoneedtocreate")) {

                                                                return "filevalidationiscomplete";

                                                            } else {

                                                                System.out.println("REINBTVRNTUP");
                                                                return "filevalidationgetfail";
                                                            }
                                                        }
                                                    } else {

                                                        return "filevalidationgetfail";
                                                    }
                                                }else if(databsesta.equals("failservervalidation")){
                                                	JOptionPane.showMessageDialog(null, "Sorry! Not allowed to reinstall Trial Edition.\nPlease contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                                    filech.deleteOnExit();
                                                    filech.delete();
                                                    System.out.println("VALUSRVAFAILRST");
                                                    return "filevalidationgetfail";
                                                } else {
                                                    JOptionPane.showMessageDialog(null, "Licence activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                                    filech.deleteOnExit();
                                                    filech.delete();
                                                    System.out.println("NEWUSRDBCRTFAIL");
                                                    return "filevalidationgetfail";

                                                }

                                            }

                                        } else {
                                            JOptionPane.showMessageDialog(null, "Sorry for the inconvenience! We are currently down for maintainance. Please try after some time.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                            filech.deleteOnExit();
                                            filech.delete();
                                            System.out.println("NEWUSRCHKVALFAIL");
                                            return "filevalidationgetfail";

                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                        filech.deleteOnExit();
                                        filech.delete();
                                        System.out.println("NEWUSRCHKVALFAIL");
                                        return "filevalidationgetfail";

                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                    filech.deleteOnExit();
                                    filech.delete();
                                    System.out.println("NEWUSRCHKVALFAIL");
                                    return "filevalidationgetfail";

                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                filech.deleteOnExit();
                                filech.delete();
                                System.out.println("USRTRYSPOFDTC");
                                return "filevalidationgetfail";

                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                            filech.deleteOnExit();
                            filech.delete();
                            System.out.println("NEWUSRFIL&STFAIL");
                            return "filevalidationgetfail";

                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                        filech.deleteOnExit();
                        filech.delete();

                        System.out.println("NEWUSRSRCFILNTFOU");
                        return "filevalidationgetfail";

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                    System.out.println("NEWUSRTRNTFD");
                    return "filevalidationfail";

                }

            } else {
                JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);
                System.out.println("NEWUSRSTFAIL");
                return "filevalidationfail";

            }
        } catch (Exception g) {
            JOptionPane.showMessageDialog(null, "License activation failed! Please upload valid license file.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

            filech.deleteOnExit();
            filech.delete();
            config.MastsvCExcep.handleException(g, "Filvalid");
            System.out.println("NEWUSRFILVALFAL");
            return "filevalidationgetfailinexception";
        }

    }

    public String croscheck1createornotdecision() {
        String licencename, activatedate, expdate;
        MastsvMFD wr = new MastsvMFD();

        wr.getValue();
        licencename = wr.Ethernetcall;
        activatedate = wr.tenetative;
        expdate = wr.reactive;

        if ((licencename.equals("NA") || activatedate.equals("NA") || expdate.equals("NA"))) {
            return "yesweneedtocreatecross1";
        } else {
            return "cross1alreadythere";
        }

    }

    public String secondcroscheckcretadecision() {
        String currentUsersHomeDir = System.getProperty("user.home");

        String target = currentUsersHomeDir + "\\" + ".m2\\repository\\Jaspersite";

        File f = new File(target);

        if (f.exists()) {

            return "alreadyitisthere";

        } else {
            return "secondcrosscheckhavetocreate";
        }

    }

    public String checkscreate() {

        String Crosonestatus = croscheck1createornotdecision();

        if (Crosonestatus.equals("yesweneedtocreatecross1")) {
            MastsvMFD fxset = new MastsvMFD();
            String status = fxset.setValue();
            if (status.equals("crossonesetok")) {

                String secondcheckstatus = secondcroscheckcretadecision();

                if (secondcheckstatus.equals("secondcrosscheckhavetocreate")) {

                    String currentUsersHomeDir = System.getProperty("user.home");

                    String target = currentUsersHomeDir + "\\" + ".m2";
                    File f = new File(target);
                    if(!f.exists()){
                    	f.mkdir();
                    	
                    }
                                        
                    File file = new File(target + "\\repository");

                    if(!file.exists()){
                    	file.mkdir();
                    }
                    
                    File filesecond = new File(target + "\\repository\\jaspersite");

                    if (filesecond.mkdir()) {

                        return "doneallcheckscreated";
                    } else {

                        return "failtocreatechecks";
                    }
                } else {

                    return "failtocreatechecks";
                }

            } else {

                return "failtocreatechecks";
            }

        } else {
            String secondcheckstatus = secondcroscheckcretadecision();

            if (secondcheckstatus.equals("alreadyitisthere")) {

                return "okalreadyexistnoneedtocreate";
            } else {

                return "failtocreatechecks";
            }

        }

    }

    public String targetfilecheck() {
        MastsvLP.Loadproperties();
        targetfile = new File(MastsvLP.target);
        if (targetfile.exists()) {
            return "targetfound";
        } else {
            System.out.println("NEWUSRTRNTFD");
            return "targetfail";
        }
    }

    public String checksystem() throws FileNotFoundException, IOException, SQLException {
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

            String getsystemdatastaus = getsystemdata();
            if (getsystemdatastaus.equals("getsystemdatasuccess")) {
                String filedecreptionstatus = filedatadecrpetion();
                if (filedecreptionstatus.equals("systemadatadecrptsuccess")) {

                    return "Checksystemgetsuccess";
                } else {
                    System.out.println("NEWUSRFILFAILDEC");
                    return "Checksystemfail";
                }
            } else {
                System.out.println("NEWUSRSYTFAIL");
                return "Checksystemfail";
            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Filvalid");
        	System.out.println("NEWUSRSYTFAIL");

            return "Checksystemfail";
        }

    }

    public String createdatabase() throws SQLException {
        MastsvLP.Loadproperties();

        try {

            String myzone = java.util.TimeZone.getDefault().getID();
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("hh").appendLiteral(":")
                    .appendPattern("mm").appendLiteral(":")
                    .appendPattern("ss").appendLiteral(" ")
                    .toFormatter();
            String mydate = formatter.withZone(ZoneId.of(myzone)).format(Instant.now());
            java.util.Date d = null;

            try {
                d = new SimpleDateFormat("KK:mm:ss").parse(mydate);
            } catch (ParseException e) {
            	config.MastsvCExcep.handleException(e, "Filvalid");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            String mydata = sdf.format(d);

            String[] time1 = mydata.split(":");
            int hour = Integer.parseInt(time1[0]);
            int minit = Integer.parseInt(time1[1]);

            con = config.MastsvConn.create(MastsvRDT.databasename.get(0));
            stmt = con.createStatement();
            int i = 0;
            i = stmt.executeUpdate("create table "+MastsvRDT.tablename_1.get(0)+"(ACCCH1 varchar(260),ACCCH2 varchar(100),ACCCH3 varchar(100),ACCCH4 varchar(60),ACCCH5 varchar(100),ACCCH6 varchar(100),ACCCH7 varchar(100),ACCCH8 varchar(60),ACCCH9 varchar(60),ACCCH10 varchar(100),ACCCH11 varchar(40))");

            if (i >= 0) {
                Date dd = new Date();
                SimpleDateFormat t = new SimpleDateFormat("yyyy/MM/dd");
                String d1 = t.format(dd);

                MastsvMB dateen = new MastsvMB();
                
                String curdatedenc;
                curdatedenc = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), d1);
                

                String encreptzone;
                encreptzone = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), myzone);
                
                String encrepthour, encreptminit;
                encrepthour = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), "" + hour);
              

                encreptminit = MastsvdAD.encrypt(dateen.getMkey1(), dateen.getMkey2(), "" + minit);
              
                String insersql = "insert into "+MastsvRDT.tablename_1.get(0)+"(ACCCH1,ACCCH2,ACCCH3,ACCCH4,ACCCH5,ACCCH6,ACCCH7,ACCCH8,ACCCH9,ACCCH10,ACCCH11) values(?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(insersql);
                ps.setString(1, key);
                ps.setString(2, actdate);
                ps.setString(3, expdate);
                ps.setString(4, curdatedenc);
                ps.setString(5, firstlicenceversion);
                ps.setString(6, clientname);
                ps.setString(7, licencehddid);
                ps.setString(8, "" + encrepthour);
                ps.setString(9, "" + encreptminit);
                ps.setString(10, encreptzone);
                ps.setString(11, "newuser");

                int y = 0;
                y = ps.executeUpdate();
                if (y > 0) {

                    mynewcheck1system = curdatedenc;

                    return "databasecreate";
                    
                   

                } else {

                    filech.deleteOnExit();
                    filech.delete();

                    System.out.println("NEWUSRDBINSFAIL");
                    return "databasecreatefail";

                }
            } else {
                filech.deleteOnExit();
                filech.delete();

                System.out.println("NEWUSRDBCRTFAIL");
                return "databasecreatefail";
            }

        } catch (Exception f) {

        	config.MastsvCExcep.handleException(f, "Filvalid");
            filech.deleteOnExit();
            filech.delete();

            MastsvA.jButton1.setEnabled(false);
            MastsvA.jButton2.setEnabled(true);
            MastsvA.jButton3.setEnabled(false);

            System.out.println("NEWUSRDBCRTFAIL");
            return "databasecreatefail";
        }

    }

    public String filedatadecrpetion() {
        try {
            MastsvMB filebean = new MastsvMB();
           

            filedeckeycompare = MastsvdAD .decrypt(filebean.getMkey1(), filebean.getMkey2(), key);

            String fileactdatedecr = MastsvdAD .decrypt(filebean.getMkey1(), filebean.getMkey2(), actdate);

            String fileexpirydecr = MastsvdAD .decrypt(filebean.getMkey1(), filebean.getMkey2(), expdate);

            MastsvCB dateenkey = new MastsvCB();
          

            String gotstartdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileactdatedecr);

            String gotexpdate = MastsvdAD.decrypt(dateenkey.getKey1(), dateenkey.getKey2(), fileexpirydecr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            filestartdatecomp = sdf.parse(gotstartdate);
            fileexpdatecomp = sdf.parse(gotexpdate);

            return "systemadatadecrptsuccess";

        } catch (Exception g) {

        	config.MastsvCExcep.handleException(g, "Filvalid");
            System.out.println("NEWUSRFILFAILDEC");
            return "systemadatadecrptfailtodecrept";
        }
    }

    public String getsystemdata() {

        try {

            serialnumberC = MastsvFV.getSerialNumber("C:\\");
            systemmac = MastsvFV.getMACAddress();
            systemnewid = MastsvFV.getnewidfunction();
            String motherboardserial = MastsvFV.getcpuid();
            
            systemmac = MastsvFV.getMACAddress();
            systemnewid = MastsvFV.getnewidfunction();
            MastsvCong u=new MastsvCong();
            String first=u.toChange(77)+u.toChange(65)+u.toChange(83)+u.toChange(84)+u.toChange(83)+u.toChange(84)+u.toChange(114)+u.toChange(101)+u.toChange(101)+u.toChange(68)+u.toChange(48)+u.toChange(77);
            String dd=systemmac+systemnewid+first;
            int machash = dd.hashCode();
            int motherid = motherboardserial.hashCode();
            int newid = systemnewid.hashCode();
            int hddhash = serialnumberC.hashCode();
            String sysmother = "" + motherid;
            
            String hddid = "" + hddhash + machash + newid + sysmother;

            MastsvCB firstenkey = new MastsvCB();
           
            String salt = "@v@r^t$@";
            String firststepencreptionofrhdd = MastsvdAD .encrypt(firstenkey.getKey1(), firstenkey.getKey2(), hddid);
            String firststepencreptionofrserial = MastsvdAD .encrypt(firstenkey.getKey1(), firstenkey.getKey2(), salt);

          
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

            return "getsystemdatasuccess";

        } catch (Exception r) {

        	config.MastsvCExcep.handleException(r, "Filvalid");
            System.out.println("NEWUSRSTDTFAIL");
            return "getsystemdatafail";
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
        	config.MastsvCExcep.handleException(e, "Filvalid");
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
        } catch (IOException e) {
        	config.MastsvCExcep.handleException(e, "Filvalid");

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
        	config.MastsvCExcep.handleException(e, "Filvalid");
            System.exit(1);
        }
        if (result.trim().length() < 1) {
            System.exit(1);
        }

        return result.trim();
    }

}
