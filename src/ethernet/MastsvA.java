/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ethernet;

import static ethernet.MastsvDVL.con;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import config.MastsvCong;
import config.MastsvDTm;
import config.MastsvES;

import config.MastsvPC;
import config.MastsvRDT;
import config.MastsvRS;
import config.MastsvUZ;

/**
 *
 * @author Sudhakar Barde
 */
public class MastsvA extends javax.swing.JFrame {

    private static final long serialVersionUID = 6859494092284427074L;
    FileInputStream fis2, fis22, fis21;
    FileOutputStream fos2, fos22, fos21;
    String sourcefile, destfile, dbpath, dummypath;
    File startfile;
    public static String port;
    static Connection connect;
    String checklicencevalid;
    java.util.Date date = new java.util.Date();
    public static String userstatustracker;
    String ALPHANUMERI_PATTERN
            = "^[0-9]{1,5}$";
    Pattern pattern;
    Matcher matcher;
    static String getresult = "Start";
    public static String newuserstatus;

    public MastsvA() throws IOException {

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ethernet/images/NEW-MASTS_LOGO.png")));
        this.setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 3 - this.getSize().width / 3, dim.height / 6 - this.getSize().height / 6);

        initComponents();
        setTitle("MASTS");

        jLabel17.setVisible(false);
        jLabel18.setVisible(false);

        jLabel12.setVisible(false);

        jLabel16.setVisible(false);
        jLabel17.setVisible(false);
        jLabel19.setVisible(false);
        getpath();

        String startdecision = decision();

        System.out.println("User Status: " + startdecision);

        if (startdecision.equals("newstart")) {

            String crosscheckstatus = croscheck();

            if (crosscheckstatus.equals("yeswegot")) {

                newuserstatus = "UserReinstallLicenceHere";
                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);

            } else {
                String secondcrosscheckstatus = secondcroscheck1();

                if (secondcrosscheckstatus.equals("yesagainwegot")) {

                    newuserstatus = "UserReinstallLicenceHere";
                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);

                } else {

                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);
                    newuserstatus = "UserIsNew";

                }
            }
        }

    }

    public String croscheck() {
        String licencename, activatedate, expdate;
        MastsvMFD wr = new MastsvMFD();

        wr.getValue();
        licencename = wr.Ethernetcall;
        activatedate = wr.tenetative;
        expdate = wr.reactive;

        if (!(licencename.equals("NA") && activatedate.equals("NA") && expdate.equals("NA"))) {
            return "yeswegot";
        } else {
            return "yesallowhim";
        }

    }

    public String secondcroscheck1() {
        String currentUsersHomeDir = System.getProperty("user.home");

        String target = currentUsersHomeDir + "\\" + ".m2";
        File file = new File(target + "\\repository\\Jaspersite");
        if (file.exists()) {

            return "yesagainwegot";

        } else {
            return "yesagainallowhim";
        }

    }

    public String secondcroscheckcretadecision() {
        String currentUsersHomeDir = System.getProperty("user.home");

        String target = currentUsersHomeDir + "\\" + ".m2\\\\repository\\Jaspersite";
        File f = new File(target);

        if (f.exists()) {

            return "alreadyitisthere";

        } else {
            return "secondcrosscheckhavetocreate";
        }

    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {

            int dialogButton = JOptionPane.YES_NO_OPTION;

            int dialogResult = JOptionPane.showConfirmDialog(this, " Are you sure you want to exit MASTS?", "MASTS", dialogButton);
            if (dialogResult == 0) {

                String getstutus = jLabel12.getText().toString();

                if (getstutus.equals("Server Started Successfully!")) {
                    MastsvST st = new MastsvST();
                    try {

                        con.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                        config.MastsvCExcep.handleException(ex, "Actor");

                    }
                    String serverstatus = st.stop();

                    if (serverstatus.equals("successstop")) {

                        try {
                            System.out.println("RGTCLSRSTSUCC");
                            stopserveraction();
                            this.dispose();
                            Runtime.getRuntime().exec("taskkill /F /IM java.exe");

                        } catch (IOException ex) {
                            Logger.getLogger(MastsvA.class
                                    .getName()).log(Level.SEVERE, null, ex);
                            config.MastsvCExcep.handleException(ex, "Actor");
                        }
                    } else {
                        System.out.println("RGTCLSRSTFLTST");
                        this.dispose();
                        try {
                            Runtime.getRuntime().exec("taskkill /F /IM java.exe");

                        } catch (IOException ex) {
                            Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                            config.MastsvCExcep.handleException(ex, "Actor");
                        }
                    }
                } else {
                    System.out.println("RGTCLSRST");
                    this.dispose();
                    try {
                        Runtime.getRuntime().exec("taskkill /F /IM java.exe");

                    } catch (IOException ex) {
                        Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                        config.MastsvCExcep.handleException(ex, "Actor");
                    }
                }
            } else {

            }

        }
    }

    public void getpath() {
        MastsvLP.Loadproperties();

        sourcefile = MastsvLP.source;

        dbpath = MastsvLP.dbpath.replaceAll("derby:/", "derby:");
        destfile = MastsvLP.target;
        dummypath = MastsvLP.dummypath;
    }

    public String checkfile() {
        String licfile = sourcefile;

        File licpath = new File(licfile);
        if (licpath.exists()) {

            return "fileyes";
        } else {

            return "fileno";
        }
    }

    public String databasecheckstartup() {

        String dbName = MastsvRDT.databasename.get(0);

        String bd = dummypath + "" + dbName;
        File dbpath = new File(bd);
        try {

            if (dbpath.exists()) {

                return "findmedatabase";
            } else {

                return "notfind";
            }

        } catch (Exception f) {
          
            config.MastsvCExcep.handleException(f, "Actor");
        }
        return "";
    }

    public String decision() {
        String fileex = checkfile();
        String datbaseex = databasecheckstartup();

        if (fileex.equals("fileyes")) {

            if (datbaseex.equals("findmedatabase")) {

                jButton1.setEnabled(true);
                jButton1.setBackground(new java.awt.Color(16, 37, 63));
                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                jButton3.setEnabled(false);
                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jLabel18.setVisible(false);
                jLabel16.setVisible(false);
                jLabel19.setVisible(false);
                jLabel17.setVisible(false);
                jLabel15.setText("Please start MASTS Server Engine!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                return "everythingok";
            } else {

                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                jButton3.setEnabled(false);
                jButton1.setEnabled(false);
                jLabel18.setVisible(false);

                jLabel16.setVisible(false);
                jLabel19.setVisible(false);
                jLabel17.setVisible(false);
                return "databasedeleted";

            }
        } else {
            if (datbaseex.equals("findmedatabase")) {

                jButton3.setEnabled(true);
                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jButton1.setEnabled(false);
                jLabel18.setVisible(false);

                jLabel16.setVisible(false);
                jLabel17.setVisible(false);
                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                jLabel16.setVisible(false);
                jLabel17.setVisible(false);
                jLabel19.setVisible(false);
                return "filedeleted";
            } else {

                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jButton1.setEnabled(false);
                jLabel18.setVisible(false);

                jLabel16.setVisible(false);
                jLabel19.setVisible(false);
                jLabel17.setVisible(false);

                jLabel16.setVisible(false);
                jLabel17.setVisible(false);
                jLabel19.setVisible(false);
                return "newstart";
            }
        }
    }

    public void fileanddatabsefound() throws Throwable {
        try {

            MastsvDVL check = new MastsvDVL();

            String databasedat = check.getdatabasedata();

            if (databasedat.equals("getdatabasedatafound")) {

                String filedata = check.gettingfiledata();

                if (filedata.equals("Filedatafound")) {

                    String systemdata = check.getsystemdata();

                    if (systemdata.equals("systemdatafound")) {
                        MastsvNCO nt = new MastsvNCO();
                        String networkaccessdatestatus = nt.getnetconnectionstatus();

                        if (networkaccessdatestatus.equals("Connectionfoundeverythingok") || networkaccessdatestatus.equals("Connectionnotfound")) {

                            checklicencevalid = check.checklicence();

                            if (checklicencevalid.equals("Correctlicence")) {

                                userstatustracker = MastsvDVL.userstatus;

                                jButton1.setEnabled(false);
                                jButton3.setEnabled(false);
                                jLabel15.setVisible(false);
                                warningimg.setVisible(false);
                                jButton2.setEnabled(false);
                                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                jButton2.setForeground(new java.awt.Color(255, 255, 255));

                                try {

                                    MastsvRS s = new MastsvRS();
                                    if (s.checkF()) {
                                    	
                                    	//New maven.zip extract
                                    	
                                    	//System.out.println("before unzip maven");
                                    	MastsvUZ u = new MastsvUZ();
                            			u.unZipItConfig();
                            			
                                        Boolean restartaction = s.restart();

                                        if (restartaction == false) {
                                            this.dispose();
                                        }

                                    } else if(!new File(System.getProperty("user.home")+"/.m2/" + "maven").exists()){

                                    	MastsvUZ u = new MastsvUZ();
                            			u.unZipItConfig();
                                        Boolean restartaction = s.restart();

                                        if (restartaction == false) {
                                            this.dispose();
                                        }
                                    }else {

                                        port = jTextField1.getText().trim().toString();

                                        if (MastsvA.port != null && MastsvA.port != "" && MastsvA.port.length() > 0) {
                                        	 List<String> ignore = new ArrayList<String>(Arrays.asList("1","5","7","18","20","21","22","23","25","29","37","42","43","49","53","69","70","79","80","103","108","109","110","115","118","119","137","139","143","150","156","161","179","190","194","197","389","396","443","444","445","458","546","547","563","569","1080","1433","1434","1521","3006","3389","6002","6004","10000","500","88","514","513","123","9","11","8080","8081","162","1337"));
                                            if (MastsvPC.validatePort(port)) {
                                                if (Integer.parseInt(port) < 65536 && Integer.parseInt(port) != 1) {

                                                    if (MastsvPC.isPortInUse(Integer.parseInt(port)) || ignore.contains(port)) {
                                                        JOptionPane.showMessageDialog(null, "Sorry! Specified port number is already in use by another application. \nPlease try different port number.", "MASTS", JOptionPane.WARNING_MESSAGE);
                                                        jTextField1.setEditable(true);
                                                        jButton1.setEnabled(true);
                                                        jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                                        jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                                        jLabel16.setVisible(false);
                                                        jLabel17.setVisible(false);
                                                        jLabel19.setVisible(false);
                                                    } else {

                                                        if (userstatustracker.equals("newuser")) {

                                                            MastsvNCO ntcheck = new MastsvNCO();
                                                            String internetvaialbilitycheck = ntcheck.getnetconnectionstatus();

                                                            if (internetvaialbilitycheck.equals("Connectionfoundeverythingok")) {

                                                                MastsvVC check1 = new MastsvVC();
                                                                String lastpingvarutrastatus = check1.pingvarutra();
                                                                if (lastpingvarutrastatus.equals("responsesuccess")) {
                                                                    jLabel16.setText("Please wait!");
                                                                    jLabel16.setVisible(true);
                                                                    jLabel17.setVisible(true);
                                                                    jLabel19.setText("Downloading required files and plugins.");
                                                                    jLabel19.setVisible(true);

                                                                    serverstart();
                                                                } else {

                                                                    JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                                                    this.dispose();
                                                                }
                                                            } else if (internetvaialbilitycheck.equals("Connectionfoundbutprobleinchanges")) {

                                                                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                                                this.dispose();
                                                            } else if (internetvaialbilitycheck.equals("Connectionnotfound")) {

                                                                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                                                this.dispose();
                                                            }

                                                        } else {
                                                            jLabel16.setText("Please wait!");
                                                            jLabel16.setVisible(true);
                                                            jLabel17.setVisible(true);
                                                            jLabel19.setText("Initializing MASTS Server...");
                                                            jLabel19.setVisible(true);

                                                            serverstart();
                                                        }

                                                    }
                                                } else {
                                                    JOptionPane.showMessageDialog(null, "Specified port number is out of range! Please specify valid port number.", "MASTS", JOptionPane.WARNING_MESSAGE);
                                                    jTextField1.setEditable(true);
                                                    jButton1.setEnabled(true);
                                                    jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                                    jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                                    jLabel16.setVisible(false);
                                                    jLabel17.setVisible(false);
                                                    jLabel19.setVisible(false);
                                                }
                                            } else {
                                                JOptionPane.showMessageDialog(null, "Invalid port number! Please specify valid port number.", "MASTS", JOptionPane.WARNING_MESSAGE);
                                                jTextField1.setEditable(true);
                                                jButton1.setEnabled(true);
                                                jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                                jLabel16.setVisible(false);
                                                jLabel17.setVisible(false);
                                                jLabel19.setVisible(false);
                                            }

                                        } else {
                                            if (MastsvPC.isPortInUse(9091)) {
                                                JOptionPane.showMessageDialog(null, "Sorry! Specified port number is already in use by another application.\n"
                                                        + "Please try different port number.", "MASTS", JOptionPane.WARNING_MESSAGE);
                                                jTextField1.setEditable(true);
                                                jButton1.setEnabled(true);
                                                jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                                jLabel16.setVisible(false);
                                                jLabel17.setVisible(false);
                                                jLabel19.setVisible(false);
                                            } else {

                                                if (userstatustracker.equals("newuser")) {

                                                    MastsvNCO ntcheck = new MastsvNCO();
                                                    String internetvaialbilitycheck = ntcheck.getnetconnectionstatus();

                                                    if (internetvaialbilitycheck.equals("Connectionfoundeverythingok")) {

                                                        MastsvVC check1 = new MastsvVC();
                                                        String lastpingvarutrastatus = check1.pingvarutra();
                                                        if (lastpingvarutrastatus.equals("responsesuccess")) {
                                                            jLabel16.setText("Please wait!");
                                                            jLabel16.setVisible(true);
                                                            jLabel17.setVisible(true);
                                                            jLabel19.setText("Downloading required files and plugins.");
                                                            jLabel19.setVisible(true);

                                                            serverstart();
                                                        } else {

                                                            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                                            this.dispose();
                                                        }
                                                    } else if (internetvaialbilitycheck.equals("Connectionfoundbutprobleinchanges")) {

                                                        JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                                        this.dispose();
                                                    } else if (internetvaialbilitycheck.equals("Connectionnotfound")) {

                                                        JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                                        this.dispose();
                                                    }
                                                } else {
                                                    jLabel16.setText("Please wait!");
                                                    jLabel16.setVisible(true);
                                                    jLabel17.setVisible(true);
                                                    jLabel19.setText("Initializing MASTS Server...");
                                                    jLabel19.setVisible(true);

                                                    serverstart();
                                                }

                                            }

                                        }

                                    }

                                } catch (Exception e) {
                                	config.MastsvCExcep.handleException(e, "Actor");
                                    if (e.getMessage().contains("java.lang.NumberFormatException:")) {
                                        JOptionPane.showMessageDialog(null, "Invalid port number!\nPlease specify valid port number.", "MASTS", JOptionPane.WARNING_MESSAGE);
                                        jTextField1.setEditable(true);
                                        jButton1.setEnabled(true);
                                        jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                        jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                        jLabel16.setVisible(false);
                                        jLabel17.setVisible(false);

                                        jLabel19.setVisible(false);

                                    } else if (e.getMessage().contains("For input string:")) {
                                        JOptionPane.showMessageDialog(null, "Invalid port number!\nPlease specify valid port number.", "MASTS", JOptionPane.WARNING_MESSAGE);
                                        jTextField1.setEditable(true);
                                        jLabel16.setVisible(false);
                                        jLabel17.setVisible(false);
                                        jButton1.setEnabled(true);
                                        jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                        jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                        jLabel19.setVisible(false);

                                    }

                                }

                            } else if (checklicencevalid.equals("licencerenewsuccessfullyrestart")) {

                                int answer = JOptionPane.showConfirmDialog(null, "Your license is renewed successfully.\nSystem restart required to apply the license.\nPlease save your work and close all applications.", "MASTS", JOptionPane.YES_NO_OPTION);
                                if (answer == JOptionPane.YES_OPTION) {
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton3.setEnabled(false);
                                    jButton1.setEnabled(false);
                                    jLabel18.setVisible(true);
                                    jLabel18.setForeground(new java.awt.Color(0, 102, 0));
                                    jLabel18.setText("Congratulations! Your MASTS license is renewed successfully.");
                                    jLabel15.setVisible(false);
                                    warningimg.setVisible(false);
                                    jLabel16.setVisible(false);
                                    jLabel17.setVisible(false);
                                    jLabel19.setVisible(false);
                                    MastsvRS s = new MastsvRS();
                                    s.restartRenew();
                                } else if (answer == JOptionPane.NO_OPTION) {
                                    new File(MastsvLP.dummypath + "/2sjd45s56255f455f.vml").deleteOnExit();
                                    JOptionPane.showMessageDialog(null, "MASTS setup has detected that there are pending changes that require a system restart.\nPlease restart the system to complete the installation.", "MASTS", JOptionPane.INFORMATION_MESSAGE);
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton3.setEnabled(false);
                                    jButton1.setEnabled(false);
                                    jLabel18.setVisible(true);
                                    jLabel18.setForeground(new java.awt.Color(0, 102, 0));
                                    jLabel18.setText("Congratulations! Your MASTS license is renewed successfully.");
                                    jLabel15.setVisible(false);
                                    warningimg.setVisible(false);
                                    jLabel16.setVisible(false);
                                    jLabel17.setVisible(false);
                                    jLabel19.setVisible(false);
                                    this.dispose();
                                } else {
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton3.setEnabled(false);
                                    jButton1.setEnabled(false);
                                    jLabel18.setVisible(true);
                                    jLabel18.setForeground(new java.awt.Color(0, 102, 0));
                                    jLabel18.setText("Congratulations! Your MASTS license is renewed successfully.");
                                    jLabel15.setVisible(false);
                                    warningimg.setVisible(false);
                                    jLabel16.setVisible(false);
                                    jLabel17.setVisible(false);
                                    jLabel19.setVisible(false);
                                    this.dispose();
                                }

                            } else if (checklicencevalid.equals("usernotinterestedtorenew")) {
                                System.out.println("DTLCRNNTINT");
                                JOptionPane.showMessageDialog(null, "Your license has expired!\nYou can no longer use this product.\nPlease contact MASTS support team for renewal options.", "MASTS License Alert!", JOptionPane.WARNING_MESSAGE);

                                this.dispose();
                                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                            } else if (checklicencevalid.equals("failtorenew")) {
                                System.out.println("DTLCRNOPFLUN");
                                JOptionPane.showMessageDialog(null, "Sorry! unable to renew your MASTS license.\nPlease contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);

                                this.dispose();
                                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                            } else {

                                //System.out.println("checklicencevalid: "+checklicencevalid);
                                System.out.println("DTLCVRFAL");
                                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease restart MASTS Server and try again.\nIf problem persists, contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                                jButton1.setEnabled(false);
                                jButton3.setEnabled(false);
                                jLabel15.setText("License renewal failed!");
                                jLabel15.setVisible(true);
                                warningimg.setVisible(true);

                                jLabel16.setVisible(false);
                                jLabel17.setVisible(false);

                                jLabel19.setVisible(false);
                                this.dispose();
                                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                            }

                        } else {

                            System.out.println("DTNTFDBTCHFD");
                            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Check your internet connection settings and try again.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                            jTextField1.setEditable(true);
                            jButton1.setEnabled(true);
                            jButton1.setBackground(new java.awt.Color(16, 37, 63));
                            jButton1.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel16.setVisible(false);
                            jLabel17.setVisible(false);
                            jLabel19.setVisible(false);
                        }
                    } else {

                        System.out.println("DTSYDTGTFL");
                        JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please restart MASTS Server Manager and try again.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                        jButton1.setEnabled(false);
                        jButton3.setEnabled(false);
                        jLabel15.setText("License renewal failed!");

                        jLabel16.setVisible(false);
                        jLabel17.setVisible(false);

                        jLabel19.setVisible(false);
                        this.dispose();
                        Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                    }

                } else {

                    System.out.println("DTFLDTGTFL");
                    JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease restart MASTS Server and try again.\nIf problem persists, contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                    jButton1.setEnabled(false);
                    jButton3.setEnabled(false);
                    jLabel15.setText("License renewal failed!");
                    jButton2.setEnabled(true);
                    jLabel16.setVisible(false);
                    jLabel17.setVisible(false);

                    jLabel19.setVisible(false);
                    this.dispose();
                    Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                }

            } else {

                System.out.println("DTDBFAL");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease restart MASTS Server and try again.\nIf problem persists, contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                jButton1.setEnabled(false);
                jButton3.setEnabled(false);
                jLabel15.setText("License renewal failed!");
                jButton2.setEnabled(true);
                jLabel16.setVisible(false);
                jLabel17.setVisible(false);

                jLabel19.setVisible(false);
                this.dispose();
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");

            }

        } catch (Exception g) {
          
            config.MastsvCExcep.handleException(g, "Actor");
        }

    }

    public void serverstart() {
        MastsvMS d = new MastsvMS();

        try {

            d.start();
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Actor");
        }
    }

    public void filenotfounddatabasepresent() throws Throwable {

        JFileChooser fileChooser = new JFileChooser();
        JTextArea textarea = new JTextArea();

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {

                textarea.read(new FileReader(file.getAbsolutePath()), null);

                String filena = file.getName().toString();

                if (filena.equals("MASTS_License.vml")) {
                    if (file.length() < 2000) {
                        jLabel15.setVisible(false);
                        warningimg.setVisible(false);

                        fis2 = new FileInputStream(file);
                        fos2 = new FileOutputStream(sourcefile);

                        jLabel18.setVisible(false);
                        jButton1.setEnabled(true);
                        jButton1.setBackground(new java.awt.Color(16, 37, 63));
                        jButton1.setForeground(new java.awt.Color(255, 255, 255));
                        jButton3.setEnabled(false);
                        doCopy(fis2, fos2);
                        fis2.close();
                        fos2.close();

                        try {

                            MastsvCong c = new MastsvCong();
                            String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                                    + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);

                            fis22 = new FileInputStream(sourcefile);
                            fos22 = new FileOutputStream(destfile);
                            decrypt(key, fis22, fos22);
                            fis22.close();
                            fos22.close();
                        } catch (Exception f) {
                        	config.MastsvCExcep.handleException(f, "Actor");
                            System.out.println("ExceptionFDDElete");

                            this.dispose();
                            Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                        }
                        MastsvFD Filedelete1 = new MastsvFD();

                        String filedeletedatabsedata = Filedelete1.getdatabasedata();

                        if (filedeletedatabsedata.equals("Filedeleteddabasedatafound")) {

                            String filedeletefiledata = Filedelete1.gettingfiledata();

                            if (filedeletefiledata.equals("Filedeletefiledatafound")) {

                                String filedeletesystemdata = Filedelete1.getsystemdata();

                                if (filedeletesystemdata.equals("filedeletesystemdatafound")) {

                                    String filedeletelicencestatust = Filedelete1.checklicence();

                                    if (filedeletelicencestatust.equals("filedeletelicencesuccess")) {

                                        System.out.print("file deleted recovery completed");

                                        jButton1.setEnabled(true);
                                        jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                        jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                        jButton3.setEnabled(false);
                                        jLabel15.setText("Please start MASTS Server Engine!");

                                        jLabel15.setVisible(true);
                                        warningimg.setVisible(true);
                                        jButton2.setEnabled(false);
                                        jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                        jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                        jLabel16.setVisible(false);
                                        jLabel17.setVisible(false);
                                        jLabel19.setVisible(false);
                                    } else {
                                        System.out.print("file deleted recovery got fail");
                                        JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                                        jButton1.setEnabled(false);
                                        jButton3.setEnabled(true);
                                        jLabel15.setText("License verification failed!");
                                        jLabel15.setVisible(true);
                                        warningimg.setVisible(true);
                                        jButton2.setEnabled(false);
                                        jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                        jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                        jLabel16.setVisible(false);
                                        jLabel17.setVisible(false);
                                        jLabel19.setVisible(false);
                                        deletefile();

                                    }
                                } else {
                                    System.out.print("FDSYTDTFOEXC");
                                    JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                                    jButton1.setEnabled(false);
                                    jButton3.setEnabled(true);
                                    jLabel15.setText("License verification failed!");
                                    jLabel15.setVisible(true);
                                    warningimg.setVisible(true);
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jLabel16.setVisible(false);
                                    jLabel17.setVisible(false);
                                    jLabel19.setVisible(false);
                                    deletefile();

                                }

                            } else {
                                System.out.print("FDFILEDTFOEXC");
                                JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                                jButton1.setEnabled(false);
                                jButton3.setEnabled(true);
                                jLabel15.setText("License verification failed!");
                                jLabel15.setVisible(true);
                                warningimg.setVisible(true);
                                jButton2.setEnabled(false);
                                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                jLabel16.setVisible(false);
                                jLabel17.setVisible(false);

                                jLabel19.setVisible(false);
                                deletefile();

                            }

                        } else {
                            System.out.print("FDDAFOUNDEXC");
                            JOptionPane.showMessageDialog(null, "License verification failed!\n" + "Please upload valid license file or contact MASTS support team for any technical assistance.", "MASTS License Alert!", JOptionPane.ERROR_MESSAGE);
                            jButton1.setEnabled(false);
                            jButton3.setEnabled(true);
                            jButton2.setEnabled(false);
                            jButton2.setBackground(new java.awt.Color(16, 37, 63));
                            jButton2.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel15.setText("License verification failed!");
                            jLabel15.setVisible(true);
                            warningimg.setVisible(true);

                            jLabel16.setVisible(false);
                            jLabel17.setVisible(false);

                            jLabel19.setVisible(false);
                            deletefile();
                        }
                    } else {

                        System.out.println("FWRONG");
                        jButton1.setEnabled(false);
                        jButton3.setEnabled(true);
                        jLabel12.setText("");
                        jLabel12.setVisible(false);
                        jLabel15.setText("Please upload valid License file!");
                        jLabel15.setVisible(true);
                        warningimg.setVisible(true);
                        jButton2.setEnabled(false);
                        jButton2.setBackground(new java.awt.Color(16, 37, 63));
                        jButton2.setForeground(new java.awt.Color(255, 255, 255));
                        jLabel16.setVisible(false);
                        jLabel17.setVisible(false);

                        jLabel19.setVisible(false);
                        deletefile();
                    }

                } else {

                    System.out.println("FWRONG");
                    jButton1.setEnabled(false);
                    jButton3.setEnabled(true);
                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);
                    jButton2.setEnabled(false);
                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                    jLabel16.setVisible(false);
                    jLabel17.setVisible(false);

                    jLabel19.setVisible(false);
                    deletefile();
                }
            } catch (IOException ex) {
                System.out.println("Exception In Act filenot found db present..." + ex.getMessage());
                config.MastsvCExcep.handleException(ex, "Actor");
                JOptionPane.showMessageDialog(null, " Sorry, something went wrong! Please restart MASTS Server Manager.", "MASTS", JOptionPane.WARNING_MESSAGE);

                this.dispose();
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            }
        } else {

        }
    }

    public void deletefile() {
        File xx = new File(sourcefile);
        if (xx.exists()) {
            xx.deleteOnExit();
            xx.delete();
        } else {

        }

        File xxww = new File(destfile);
        if (xxww.exists()) {
            xxww.deleteOnExit();
            xxww.delete();
        } else {

        }
    }

    public void targetupload() throws Throwable {
        try {

            MastsvCong c = new MastsvCong();
            String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                    + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);

            FileInputStream fis111 = new FileInputStream(sourcefile);
            FileOutputStream fos111 = new FileOutputStream(destfile);
            decrypt(key, fis111, fos111);
            fis111.close();
            fos111.close();
        } catch (Exception pk) {
        	config.MastsvCExcep.handleException(pk, "Actor");
            System.out.println("Exception--Act: while uploading the target..");
        }
    }

    public void uploadaction() throws Throwable {

        String filena = "start";
        JFileChooser fileChooser = new JFileChooser();
        JTextArea textarea = new JTextArea();

        int returnVal = fileChooser.showOpenDialog(this);

        /*	if(returnVal==JFileChooser.CANCEL_OPTION){

         jLabel12.setText("");
         jLabel12.setVisible(false);
         jLabel15.setText("Please upload valid License file!");
         jLabel15.setVisible(true);
         warningimg.setVisible(true);
		
         }*/
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {

                textarea.read(new FileReader(file.getAbsolutePath()), null);

                filena = file.getName().toString();

                if (filena.equals("MASTS_License.vml")) {

                    if (file.length() < 2000) {

                        jButton3.setEnabled(false);

                        fis2 = new FileInputStream(file);
                        fos2 = new FileOutputStream(sourcefile);

                        jLabel18.setVisible(false);

                        doCopy(fis2, fos2);
                        fis2.close();
                        fos2.close();

                        MastsvNCO seconreinstallcheck = new MastsvNCO();
                        String beforereinstallfilevalidation = seconreinstallcheck.getnetconnectionstatus();

                        if (beforereinstallfilevalidation.equals("Connectionfoundeverythingok")) {

                            MastsvVC check = new MastsvVC();
                            String lastpingvarutrastatus = check.pingvarutra();
                            if (lastpingvarutrastatus.equals("responsesuccess")) {
                                String checkvalidationstatus = checkvalidation();
                                if (checkvalidationstatus.equals("checkvalidationsuccess")) {

                                    jButton1.setEnabled(true);
                                    jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton3.setEnabled(false);
                                    startfilecreate();
                                    jLabel15.setText("Please start MASTS Server Engine!");
                                    jLabel15.setVisible(true);
                                    warningimg.setVisible(true);
                                } else {
                                    System.out.println("NEWUSRCHKLICFAIL");
                                    jButton1.setEnabled(false);
                                    jButton3.setEnabled(true);
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jLabel12.setText("");
                                    jLabel12.setVisible(false);
                                    jLabel15.setText("Please upload valid License file!");
                                    jLabel15.setVisible(true);
                                    warningimg.setVisible(true);

                                    jLabel16.setVisible(false);
                                    jLabel17.setVisible(false);

                                    jLabel19.setVisible(false);

                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

                                jLabel12.setText("");
                                jLabel12.setVisible(false);
                                jLabel15.setText("Please upload valid License file!");
                                jLabel15.setVisible(true);
                                warningimg.setVisible(true);

                                jButton3.setEnabled(true);
                                jButton1.setEnabled(false);
                                jButton2.setEnabled(false);
                                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                deletefile();

                            }
                        } else if (beforereinstallfilevalidation.equals("Connectionfoundbutprobleinchanges")) {

                            System.out.println("Internet connection is available! Please check your system, if any changes was made.");
                            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

                            jLabel12.setText("");
                            jLabel12.setVisible(false);
                            jLabel15.setText("Please upload valid License file!");
                            jLabel15.setVisible(true);
                            warningimg.setVisible(true);

                            jButton3.setEnabled(true);
                            jButton1.setEnabled(false);
                            jButton2.setEnabled(false);
                            jButton2.setBackground(new java.awt.Color(16, 37, 63));
                            jButton2.setForeground(new java.awt.Color(255, 255, 255));
                            deletefile();

                        } else if (beforereinstallfilevalidation.equals("Connectionnotfound")) {

                            System.out.println("Internet connection is not available for reinstallation! Please check your system connection.");

                            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

                            jLabel12.setText("");
                            jLabel12.setVisible(false);
                            jLabel15.setText("Please upload valid License file!");
                            jLabel15.setVisible(true);
                            warningimg.setVisible(true);

                            jButton3.setEnabled(true);
                            jButton1.setEnabled(false);
                            jButton2.setEnabled(false);
                            jButton2.setBackground(new java.awt.Color(16, 37, 63));
                            jButton2.setForeground(new java.awt.Color(255, 255, 255));
                            deletefile();
                        }

                    } else {

                        System.out.println("NEWUSRFILUSIZ");
                        System.out.println("File is not valid! Please upload correct file.");
                        jLabel12.setText("");
                        jLabel12.setVisible(false);
                        jLabel15.setText("Please upload valid License file!");
                        jLabel15.setVisible(true);
                        warningimg.setVisible(true);

                        jButton3.setEnabled(true);
                        jButton1.setEnabled(false);
                        jButton2.setEnabled(false);
                        jButton2.setBackground(new java.awt.Color(16, 37, 63));
                        jButton2.setForeground(new java.awt.Color(255, 255, 255));

                    }

                } else {

                    System.out.println("NEWUSRFILUPWR");

                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);

                }
            } catch (IOException ex) {
                System.out.println("Exception In Act upload action..." + ex.getMessage());
                config.MastsvCExcep.handleException(ex, "Actor");
                ex.printStackTrace();

                this.dispose();
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            }
        } else {

        }

    }

    public void reinstalluploadaction() throws Throwable {

        JFileChooser fileChooser = new JFileChooser();
        JTextArea textarea = new JTextArea();

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {

                textarea.read(new FileReader(file.getAbsolutePath()), null);

                String filena = file.getName().toString();

                if (filena.equals("MASTS_License.vml")) {
                    if (file.length() < 2000) {

                        jButton3.setEnabled(false);

                        fis2 = new FileInputStream(file);
                        fos2 = new FileOutputStream(sourcefile);

                        jLabel18.setVisible(false);

                        doCopy(fis2, fos2);
                        fis2.close();
                        fos2.close();

                        MastsvNCO seconreinstallcheck = new MastsvNCO();
                        String beforereinstallfilevalidation = seconreinstallcheck.getnetconnectionstatus();

                        if (beforereinstallfilevalidation.equals("Connectionfoundeverythingok")) {

                            MastsvVC check = new MastsvVC();
                            String lastpingvarutrastatus = check.pingvarutra();
                            if (lastpingvarutrastatus.equals("responsesuccess")) {
                                String checkvalidationstatus = checkvalidation();
                                if (checkvalidationstatus.equals("checkvalidationsuccess")) {

                                    jButton1.setEnabled(true);
                                    jButton1.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton1.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jButton3.setEnabled(false);
                                    startfilecreate();
                                    jLabel15.setText("Please start MASTS Server Engine!");
                                    jLabel15.setVisible(true);
                                    warningimg.setVisible(true);
                                } else {
                                    System.out.println("RENUSRVALFAL");
                                    jButton1.setEnabled(false);
                                    jButton3.setEnabled(true);
                                    jButton2.setEnabled(false);
                                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                    jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                    jLabel12.setText("");
                                    jLabel12.setVisible(false);
                                    jLabel15.setText("Please upload valid License file!");
                                    jLabel15.setVisible(true);
                                    warningimg.setVisible(true);
                                    jLabel16.setVisible(false);
                                    jLabel17.setVisible(false);

                                    jLabel19.setVisible(false);

                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                                System.out.println("RENUSRVAFAILRS");
                                jLabel12.setText("");
                                jLabel12.setVisible(false);
                                jLabel15.setText("Please upload valid License file!");
                                jLabel15.setVisible(true);
                                warningimg.setVisible(true);

                                jButton3.setEnabled(true);
                                jButton1.setEnabled(false);
                                jButton2.setEnabled(false);
                                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                                deletefile();

                            }
                        } else if (beforereinstallfilevalidation.equals("Connectionfoundbutprobleinchanges")) {

                            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
                            System.out.println("RENUSRNTBTCHG");
                            jLabel12.setText("");
                            jLabel12.setVisible(false);
                            jLabel15.setText("Please upload valid License file!");
                            jLabel15.setVisible(true);
                            warningimg.setVisible(true);

                            jButton3.setEnabled(true);
                            jButton1.setEnabled(false);
                            jButton2.setEnabled(false);
                            jButton2.setBackground(new java.awt.Color(16, 37, 63));
                            jButton2.setForeground(new java.awt.Color(255, 255, 255));
                            deletefile();

                        } else if (beforereinstallfilevalidation.equals("Connectionnotfound")) {

                            System.out.println("RENUSRNONT");
                            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

                            jLabel12.setText("");
                            jLabel12.setVisible(false);
                            jLabel15.setText("Please upload valid License file!");
                            jLabel15.setVisible(true);
                            warningimg.setVisible(true);

                            jButton3.setEnabled(true);
                            jButton1.setEnabled(false);
                            jButton2.setEnabled(false);
                            jButton2.setBackground(new java.awt.Color(16, 37, 63));
                            jButton2.setForeground(new java.awt.Color(255, 255, 255));
                            deletefile();
                        }

                    } else {

                        System.out.println("RENUSRFILSZBG");
                        jLabel12.setText("");
                        jLabel12.setVisible(false);
                        jLabel15.setText("Please upload valid License file!");
                        jLabel15.setVisible(true);
                        warningimg.setVisible(true);

                        jButton3.setEnabled(true);
                        jButton1.setEnabled(false);
                        jButton2.setEnabled(false);
                        jButton2.setBackground(new java.awt.Color(16, 37, 63));
                        jButton2.setForeground(new java.awt.Color(255, 255, 255));

                    }

                } else {

                    System.out.println("RENUSRFILWR");
                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);

                    jButton3.setEnabled(true);
                    jButton1.setEnabled(false);
                    jButton2.setEnabled(false);
                    jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    jButton2.setForeground(new java.awt.Color(255, 255, 255));

                }
            } catch (IOException ex) {
                System.out.println("Exception In Act uploadaction..." + ex.getMessage());
                config.MastsvCExcep.handleException(ex, "Actor");
                ex.printStackTrace();

                this.dispose();
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            }
        } else {

        }
    }

    public void startserver() throws Throwable {

        jButton1.setEnabled(false);
        jLabel16.setVisible(false);
        jLabel19.setVisible(false);
        jLabel17.setVisible(false);
        jLabel15.setVisible(true);
        warningimg.setVisible(true);

        checkvalidation();

    }

    public String checkvalidation() throws Throwable {

        MastsvCong c = new MastsvCong();
        String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);
        try {
            jButton3.setEnabled(false);
            fis21 = new FileInputStream(sourcefile);
            fos21 = new FileOutputStream(destfile);
            decrypt(key, fis21, fos21);

            fis21.close();
            fos21.close();
            MastsvFV dq = new MastsvFV();
            String filevalidationstatus = dq.filevalidationfirststep();

            if (filevalidationstatus.equals("filevalidationiscomplete")) {
                jButton1.setEnabled(true);
                jButton1.setBackground(new java.awt.Color(16, 37, 63));
                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jButton3.setEnabled(false);
                startfilecreate();
                jLabel15.setText("Please start MASTS Server Engine!");
                jLabel15.setVisible(true);
                jLabel12.setVisible(false);
                warningimg.setVisible(true);

                return "checkvalidationsuccess";
            } else if (filevalidationstatus.equals("filevalidationgetfailinexception")) {
                System.out.println("NEWUSRFILVALFAL");
                JOptionPane.showMessageDialog(null, " Sorry, something went wrong! Please restart MASTS Server Manager.", "MASTS", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                return "checkvalidationfail";
            } else {
                jButton1.setEnabled(false);
                jButton3.setEnabled(true);
                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                jLabel16.setVisible(false);
                jLabel17.setVisible(false);

                jLabel19.setVisible(false);
                deletefile();

                System.out.println("NEWUSRFILVALFAL");
                return "checkvalidationfail";
            }
        } catch (Exception f) {
        	config.MastsvCExcep.handleException(f, "Actor");
            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
            System.out.println("NEWUSRCHKLICFAIL");
            this.dispose();
            Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            return "checkvalidationfail";
        }

    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
   
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        warningimg = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        masts_copyright = new javax.swing.JLabel();
        mastsurl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(185, 205, 229));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel3.setBackground(new java.awt.Color(185, 205, 229));

        jLabel15.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 51, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setText("Server is not running");

        jLabel16.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Please wait!");

        jLabel18.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 51, 51));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Sorry! Please select valid license file");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel3.setText("Select License File:  ");

        jButton3.setBackground(new java.awt.Color(16, 37, 63));
        jButton3.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Upload License File");
        jButton3.setToolTipText("Select File here");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setSelected(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jTextField1.setText("9091");
        jTextField1.setToolTipText("");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel7.setText("Specify Port Number:");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel6.setText("MASTS Server Engine runs on default port TCP-9091 (Recommended)  ");

        jLabel19.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Initializing MASTS Server startup");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel8.setText("You can specify custom port number, if required. ");

        warningimg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/warning12.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("jLabel12");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/LoadingProgressBar.gif"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel8))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel6)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(warningimg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(171, 171, 171))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(106, 106, 106))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jLabel18)
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(warningimg)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(185, 205, 229));

        jButton1.setBackground(new java.awt.Color(16, 37, 63));
        jButton1.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("START  Server");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        jButton1.setMaximumSize(new java.awt.Dimension(167, 17));
        jButton1.setMinimumSize(new java.awt.Dimension(167, 17));
        jButton1.setSelected(true);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(16, 37, 63));
        jButton2.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("STOP Server");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        jButton2.setSelected(true);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        jPanel5.setBackground(new java.awt.Color(254, 255, 255));

        jLabel1.setFont(new java.awt.Font("Cambria Math", 1, 32)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/NEW-MASTS_LOGO-2.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 30)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(23, 55, 94));
        jLabel2.setText("MASTS Server Manager");

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(23, 55, 94));
        jLabel4.setText("Pro Edition");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/minimize-5.png"))); // NOI18N
        jLabel10.setToolTipText("Minimize");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/close-7.png"))); // NOI18N
        jLabel44.setText("jLabel4");
        jLabel44.setToolTipText("Close");
        jLabel44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel44MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel44))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        masts_copyright.setForeground(new java.awt.Color(255, 255, 255));
        masts_copyright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/copy-rnew.png"))); // NOI18N

        mastsurl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/site - Copy.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(masts_copyright)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mastsurl, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mastsurl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(masts_copyright, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:

        jLabel15.setText("");

        warningimg.setVisible(false);
        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14));
        jLabel12.setForeground(new java.awt.Color(0, 102, 0));
        jLabel12.setText("License verification in process...");
        System.out.println("-------Upload License---------"+MastsvDTm.getDateTime()+"----------");
        jLabel12.setVisible(true);

        String fraud = startfilecreate();

        String decision = decision();
        String useruploadstaus = newuserstatus;
        
        //srujan
        System.out.println("fraud: "+fraud );
        System.out.println("decision: "+decision);
        System.out.println("useruploadstaus: "+useruploadstaus);

        if (decision.equals("filedeleted")) {
            try {

                filenotfounddatabasepresent();
                
            } catch (Throwable ex) {
                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                System.out.println("FDELETE FAIL");
                Logger.getLogger(MastsvA.class
                        .getName()).log(Level.SEVERE, null, ex);
              
                System.out.println("Exception Act Line No.: " + ex.getStackTrace()[2].getLineNumber());
                this.dispose();
                try {
                    Runtime.getRuntime().exec("taskkill /IM java.exe");
                } catch (IOException ex1) {
                    Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex1);
                    config.MastsvCExcep.handleException(ex1, "Actor");
                }
            }
        } else if ((decision.equals("newstart")) && (fraud.equals("filecreate")) && (useruploadstaus.equals("UserIsNew"))) {

            System.out.print("Welcome to MASTS! New Installation Successful.");
            MastsvNCO nt = new MastsvNCO();
            String internetvaialbilitycheck = nt.getnetconnectionstatus();

            //srujan
            System.out.println("internetvaialbilitycheck: "+internetvaialbilitycheck);
            
            if (internetvaialbilitycheck.equals("Connectionfoundeverythingok")) {

                try {
                    MastsvVC check = new MastsvVC();
                    String lastpingvarutrastatus = check.pingvarutra();
                    
                    //srujan
                    System.out.println("lastpingvarutrastatus: "+lastpingvarutrastatus);
                    
                    if (lastpingvarutrastatus.equals("responsesuccess")) {

                        uploadaction();
                    } else {

                        jLabel12.setText("");
                        jLabel12.setVisible(false);
                        jLabel15.setText("Please upload valid License file!");
                        jLabel15.setVisible(true);
                        warningimg.setVisible(true);
                        System.out.println("VARRSCRRBTUSRCHG");
                        JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

                    }

                } catch (Throwable ex) {
                    Logger.getLogger(MastsvA.class
                            .getName()).log(Level.SEVERE, null, ex);
                    System.out.println("VARRSCRRBTUSRCHG");
                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);
                    System.out.println("Exception Act Line No.: " + ex.getStackTrace()[2].getLineNumber());
                    this.dispose();
                    try {
                        Runtime.getRuntime().exec("taskkill /F /IM java.exe");
                    } catch (IOException ex1) {
                        Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex1);
                        
                        config.MastsvCExcep.handleException(ex1, "Actor");
                    }
                }
            } else if (internetvaialbilitycheck.equals("Connectionfoundbutprobleinchanges")) {
                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);

                System.out.println("NEWUSRNETFOUBTUSRCHANG");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

            } else if (internetvaialbilitycheck.equals("Connectionnotfound")) {

                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                System.out.println("Connection not found");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nCheck your internet connection settings and try again.", "MASTS", JOptionPane.ERROR_MESSAGE);

            }

        } else if ((decision.equals("newstart")) && (fraud.equals("filecreate")) && (useruploadstaus.equals("UserReinstallLicenceHere"))) {

            System.out.print("Welcome to MASTS! License Renewal Successful.");

            MastsvNCO nt = new MastsvNCO();
            String internetvaialbilitycheck = nt.getnetconnectionstatus();

            if (internetvaialbilitycheck.equals("Connectionfoundeverythingok")) {

                MastsvVC check = new MastsvVC();
                String lastpingvarutrastatus = check.pingvarutra();
                if (lastpingvarutrastatus.equals("responsesuccess")) {
                    try {
                        reinstalluploadaction();
                    } catch (Throwable ex) {

                        Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {

                    jLabel12.setText("");
                    jLabel12.setVisible(false);
                    jLabel15.setText("Please upload valid License file!");
                    jLabel15.setVisible(true);
                    warningimg.setVisible(true);
                    System.out.println("response wrong");
                    JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

                }
            } else if (internetvaialbilitycheck.equals("Connectionfoundbutprobleinchanges")) {

                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                System.out.println("Connection changes problem occured");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);

            } else if (internetvaialbilitycheck.equals("Connectionnotfound")) {
                jLabel12.setText("");
                jLabel12.setVisible(false);
                jLabel15.setText("Please upload valid License file!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                System.out.println("Connection not found");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nCheck your internet connection settings and try again.", "MASTS", JOptionPane.ERROR_MESSAGE);

            }

        } else if ((decision.equals("newstart")) && fraud.equals("alreadyexist")) {

            jLabel12.setText("");
            jLabel12.setVisible(false);
            jLabel15.setText("Please upload valid License file!");
            jLabel15.setVisible(true);
            warningimg.setVisible(true);
            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
            System.out.println("FRAUFOUND");
            this.dispose();
            try {
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            } catch (IOException ex) {
                Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                config.MastsvCExcep.handleException(ex, "Actor");
            }
        }

        //System.out.println("in last if "+getresult);
        if (getresult.equals("Start")) {
            System.out.println("in last if start ");
            jLabel12.setText("");
            jLabel12.setVisible(false);
            jLabel15.setText("Please upload valid License file!");
            jLabel15.setVisible(true);
            warningimg.setVisible(true);
        }
        System.out.println("-------Upload License---------"+MastsvDTm.getDateTime()+"----------");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        try {
        	System.out.println("--------Start Server---------"+MastsvDTm.getDateTime()+"----------");

            MastsvES env = new MastsvES();
            String res = env.decision();
            if (res.equals("GETALL")) {
                String ss = jTextField1.getText().trim().toString();
                int completestringlen = ss.length();

                pattern = Pattern.compile(ALPHANUMERI_PATTERN);
                matcher = pattern.matcher(ss);

                if (matcher.matches()) {
                    int strvalu = Integer.parseInt(ss);

                    if (((completestringlen <= 5) || (completestringlen >= 4)) && ((strvalu > 1) && (strvalu <= 65550)) && (!ss.equals(""))) {

                        jTextField1.setEditable(false);
                        jLabel17.setVisible(true);
                        jLabel17.setEnabled(true);
                        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ethernet/images/LoadingProgressBar.gif")));
                        jLabel12.setVisible(false);
                        getstatusatstartbutton();
                    } else {
                        JOptionPane.showMessageDialog(null, "Specified port number is out of range! Please specify valid port number.", "MASTS", JOptionPane.ERROR_MESSAGE);
                        jTextField1.requestFocus();

                    }
                } else {
                    jTextField1.setText("");
                    JOptionPane.showMessageDialog(null, "Please specify valid port number.", "MASTS", JOptionPane.ERROR_MESSAGE);
                    jTextField1.requestFocus();

                }

            } else {
                System.out.println("Act Env Variables setting!");
                JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Throwable ex) {
            Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }

        System.out.println("--------Start Server---------"+MastsvDTm.getDateTime()+"----------");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        try {
        	 System.out.println("--------Stop Server---------"+MastsvDTm.getDateTime()+"----------");
            con.close();
            MastsvST st = new MastsvST();

            String serverstatus = st.stop();
            if (serverstatus.equals("successstop")) {

                stopserveraction();
                jTextField1.setEditable(true);
                jButton1.setEnabled(true);
                jButton1.setBackground(new java.awt.Color(16, 37, 63));
                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jButton3.setEnabled(false);
                jLabel15.setText("Please start MASTS Server Engine!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                jLabel12.setText("Server Stopped!");
                jLabel12.setVisible(false);

                jLabel18.setVisible(false);

                jLabel16.setVisible(false);
                jLabel17.setVisible(false);
                jLabel19.setVisible(false);
                JOptionPane.showMessageDialog(null, "Server Stopped!", "MASTS", JOptionPane.INFORMATION_MESSAGE);

            } else {

                stopserveraction();
                jButton1.setEnabled(true);
                jButton1.setBackground(new java.awt.Color(16, 37, 63));
                jButton1.setForeground(new java.awt.Color(255, 255, 255));
                jTextField1.setEditable(true);
                jButton2.setEnabled(false);
                jButton2.setBackground(new java.awt.Color(16, 37, 63));
                jButton2.setForeground(new java.awt.Color(255, 255, 255));
                jButton3.setEnabled(false);
                jLabel15.setText("Please start MASTS Server Engine!");
                jLabel15.setVisible(true);
                warningimg.setVisible(true);
                jLabel12.setText("Server Stopped!");
                jLabel12.setVisible(false);
                jLabel18.setVisible(false);

                jLabel16.setVisible(false);
                jLabel17.setVisible(false);
                jLabel19.setVisible(false);

            }

        } catch (SQLException ex) {
            Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
            config.MastsvCExcep.handleException(ex, "Actor");
        } catch (IOException ex) {
            Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
            config.MastsvCExcep.handleException(ex, "Actor");
        
        }
        System.out.println("--------Stop Server---------"+MastsvDTm.getDateTime()+"----------");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:

        this.setState(this.ICONIFIED);
    }//GEN-LAST:event_jLabel10MouseClicked

    private void jLabel44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel44MouseClicked
        // TODO add your handling code here:
    	 System.out.println("--------Closed MASTS---------"+MastsvDTm.getDateTime()+"----------");
         int dialogButton1 = JOptionPane.YES_NO_OPTION;

        int dialogResult1 = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit MASTS?", "MASTS", dialogButton1);

        if (dialogResult1 == 0) {
            String getstutus = jLabel12.getText().toString();

            if (getstutus.equals("Server Started Successfully!")) {

                jLabel18.setVisible(false);
                jLabel15.setVisible(false);
                jLabel16.setVisible(false);
                jLabel17.setVisible(false);
                jLabel19.setVisible(false);
                jLabel15.setVisible(false);
                warningimg.setVisible(false);
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                    config.MastsvCExcep.handleException(ex, "Actor");
                }
                MastsvST st = new MastsvST();

                String serverstatus = st.stop();

                if (serverstatus.equals("successstop")) {

                    try {

                        stopserveraction();
                        this.dispose();
                        Runtime.getRuntime().exec("taskkill /F /IM java.exe");

                    } catch (IOException ex) {
                        Logger.getLogger(MastsvA.class
                                .getName()).log(Level.SEVERE, null, ex);
                        config.MastsvCExcep.handleException(ex, "Actor");
                    }
                } else {
                    System.out.println("SRSTRBTFLTST");
                    this.dispose();
                    try {
                        Runtime.getRuntime().exec("taskkill /F /IM java.exe");

                    } catch (IOException ex) {
                        Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                        config.MastsvCExcep.handleException(ex, "Actor");
                    }
                }
            } else {
                System.out.println("SRSTNORNFOU");
                this.dispose();
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM java.exe");

                } catch (IOException ex) {
                    Logger.getLogger(MastsvA.class.getName()).log(Level.SEVERE, null, ex);
                    config.MastsvCExcep.handleException(ex, "Actor");
                }
            }
        } else {

        }
        System.out.println("--------Closed MASTS---------"+MastsvDTm.getDateTime()+"----------");
        
    }//GEN-LAST:event_jLabel44MouseClicked

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        // TODO add your handling code here:
        jButton1.setBackground(new java.awt.Color(199, 219, 242));
        jButton1.setForeground(new java.awt.Color(6, 38, 80));

    }//GEN-LAST:event_jButton1MousePressed

    private void jButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton1MouseReleased

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        // TODO add your handling code here:
        jButton2.setBackground(new java.awt.Color(199, 219, 242));
        jButton2.setForeground(new java.awt.Color(6, 38, 80));


    }//GEN-LAST:event_jButton2MousePressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        String ss = jTextField1.getText().trim().toString();
        int l = ss.length();
        char c = evt.getKeyChar();
        List<String> ignore = new ArrayList<String>(Arrays.asList("1","5","7","18","20","21","22","23","25","29","37","42","43","49","53","69","70","79","80","103","108","109","110","115","118","119","137","139","143","150","156","161","179","190","194","197","389","396","443","444","445","458","546","547","563","569","1080","1433","1434","1521","3006","3389","6002","6004","10000","500","88","514","513","123","9","11","8080","8081","162","1337"));
        if (Character.valueOf(c) >= 0 && Character.valueOf(c) <= 9) {

            if (l > 0) {
                if (l <= 5) {
                	
                	if(!ignore.contains(String.valueOf(l))){
                		
                	}else{
                		 JOptionPane.showMessageDialog(null, "Specified port number is out of range! Please specify valid port number.", "MASTS", JOptionPane.ERROR_MESSAGE);
                         jTextField1.setText("9091");
                         jTextField1.requestFocus();
                	}

                } else {

                    JOptionPane.showMessageDialog(null, "Specified port number is out of range! Please specify valid port number.", "MASTS", JOptionPane.ERROR_MESSAGE);
                    jTextField1.setText("9091");
                    jTextField1.requestFocus();
                }
            }

        } else {
            pattern = Pattern.compile(ALPHANUMERI_PATTERN);
            matcher = pattern.matcher(ss);

            if (!matcher.matches()) {
                jTextField1.setText("");
                jTextField1.requestFocus();

            } else {

            }

        }
    }//GEN-LAST:event_jTextField1KeyReleased
    public void forsefullyclose() throws IOException {
        //JOptionPane.showMessageDialog(null,"Sorry! Something went wrong. Please restart MASTS Server Manager", "MASTS",JOptionPane.ERROR_MESSAGE);

        this.dispose();
        Runtime.getRuntime().exec("taskkill /F /IM java.exe");
    }

    public String startfilecreate() {
        File startfile = new File("" + dummypath + "aaiclient.vml");
        if (startfile.exists()) {
            return "alreadyexist";
        } else {

            return "filecreate";

        }

    }

    public String getstatusatstartbutton() throws Throwable {

        String Secondtimestaus = decision();

        if (Secondtimestaus.equals("everythingok")) {

            jButton3.setEnabled(false);
            jButton2.setEnabled(false);
            jButton2.setBackground(new java.awt.Color(16, 37, 63));
            jButton2.setForeground(new java.awt.Color(255, 255, 255));
            jLabel18.setVisible(false);

            try {
                targetupload();

            } catch (Throwable ex) {
                Logger.getLogger(MastsvA.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            fileanddatabsefound();

            return "firsteverythingok";
        } else if (Secondtimestaus.equals("databasedeleted")) {
            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.WARNING_MESSAGE);
            jButton3.setEnabled(false);
            jButton1.setEnabled(false);
            jLabel18.setVisible(false);

            jLabel16.setVisible(false);
            jLabel19.setVisible(false);
            jLabel17.setVisible(false);
            jButton2.setEnabled(true);
            return "firstdatabasedeleted";
        } else if (Secondtimestaus.equals("filedeleted")) {
            jButton2.setEnabled(false);
            jButton2.setBackground(new java.awt.Color(16, 37, 63));
            jButton2.setForeground(new java.awt.Color(255, 255, 255));
            jButton1.setEnabled(false);
            jLabel18.setVisible(false);

            jLabel16.setVisible(false);
            jLabel19.setVisible(false);
            jLabel17.setVisible(false);
            jButton3.setEnabled(true);
            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.WARNING_MESSAGE);
            return "firstfiledeleted";
        } else if (Secondtimestaus.equals("newstart")) {

            jButton2.setEnabled(true);
            jButton1.setEnabled(false);
            jLabel18.setVisible(false);

            jLabel16.setVisible(false);
            jLabel19.setVisible(false);
            jLabel17.setVisible(false);
            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.WARNING_MESSAGE);
            return "firstnewstart";
        }
        return "";
    }

    public void databsseconnectionfirst() throws SQLException {
        MastsvLP.Loadproperties();

        try {

            connect = config.MastsvConn.create(MastsvRDT.databasename.get(0));

        } catch (Exception f) {
            System.out.println("FLTCONDBEXC");
            
            JOptionPane.showMessageDialog(null, "Sorry! Something went wrong.\nPlease contact MASTS support team for any technical assistance.", "MASTS", JOptionPane.WARNING_MESSAGE);
            jButton2.setEnabled(true);
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
            System.out.println("Exception Act- first conn: " + " MSG: " + f.getMessage() + "\nTime: " + new Timestamp(date.getTime()));
            
            config.MastsvCExcep.handleException(f, "Actor");
        }
    }

    public void stopserveraction() throws IOException {

        File xx = new File(destfile);
        xx.deleteOnExit();
        xx.delete();

    }

    /**
     * @param args the command line arguments
     */
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jButton1;
    public static javax.swing.JButton jButton2;
    public static javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    public static javax.swing.JLabel jLabel12;
    public static javax.swing.JLabel jLabel15;
    public static javax.swing.JLabel jLabel16;
    public static javax.swing.JLabel jLabel17;
    public static javax.swing.JLabel jLabel18;
    public static javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private static javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    public static javax.swing.JTextField jTextField1;
    public static javax.swing.JLabel masts_copyright;
    private javax.swing.JLabel mastsurl;
    public static javax.swing.JLabel warningimg;
    // End of variables declaration//GEN-END:variables
}
