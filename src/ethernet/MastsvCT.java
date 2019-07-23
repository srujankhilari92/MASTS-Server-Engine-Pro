package ethernet;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class MastsvCT {

    public void startserver() throws IOException, URISyntaxException {
        try {

            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "cd " + MastsvLP.dummypath + "MASTS && mvn jetty:run");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "Start", msg = "";
            int s = 0;
            int c = 0;
            System.out.println("Please Wait Server is going to Started.");

            while (true) {
            	
                if (r.ready()) {

                    line = r.readLine();

                  
                    if (s == 1) {
                        System.out.println("Server Started  Now, we have to Check in..");

                        if (line.contains("licFile") || line.contains("decision==") || line.contains("file present")) {

                            msg = "Checking For Error...";
                        } else if (line.contains("Server Sttoped...") || line.contains("javax.faces.application.ViewExpiredException: viewId:/MASTS/Design/MainPage.jsf")) {

                            s = 3;

                            break;
                        } else {

                        }

                    } else if (line == null) {

                        break;
                    } else if (line.equals("[INFO] Starting scanner at interval of 5 seconds.")) {

                        s = 1;
                        msg = "Server Started";

                    } else {

                        s = 2;

                    }

                } else if (s == 1) {
                    c++;
                    if (msg == "Checking For Error...") {
                        //System.out.println("Checking For Next Step...");
                    }

                    if (c > 505050 && msg.equals("Server Started")) {
                        break;
                    }

                } else if (s == 3) {
                    break;
                }

            }
            if (s == 1) {
                MastsvA.jButton2.setEnabled(true);
                MastsvA.jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14));
                MastsvA.jLabel12.setForeground(new java.awt.Color(0, 102, 0));
                MastsvA.jLabel12.setText("Server Started Successfully!");
                MastsvA.jLabel12.setVisible(true);
                MastsvA.jLabel16.setVisible(false);
                MastsvA.jLabel19.setVisible(false);
                MastsvA.jLabel17.setVisible(false);
                MastsvA.jLabel17.setEnabled(false);
                MastsvA.jLabel16.setVisible(false);
                MastsvA.jLabel19.setVisible(false);

                String url = "";
                if (MastsvA.port != null && MastsvA.port != "" && MastsvA.port.length() > 0) {
                    url = "http://localhost:" + MastsvA.port + "/MASTS/Login/VMastsLogin.jsf";

                } else {
                    url = "http://localhost:9091/MASTS/Login/VMastsLogin.jsf";

                }
                boolean gotSQLExc = false;
                try {
                	System.out.println("Trying 'jdbc:derby:;shutdown=true'");
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                	config.MastsvCExcep.handleException(se, "Cmdtest1");
                    if (se.getSQLState().equals("XJ015")) {
                        gotSQLExc = true;
                    }
                }
                if (!gotSQLExc) {

                } else {
                    System.out.println("MASTS Processing Started..");
                }
                if (Desktop.isDesktopSupported()) {

                    Desktop.getDesktop().browse(new URI(url));

                } else {

                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("/usr/bin/firefox -new-window " + url);

                }

                final MastsvLHP ste = new MastsvLHP();
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        ste.shutdowh();
                    }
                });
                ste.startScheduleTask();

            } else if (s == 3) {
                MastsvA.jButton2.setEnabled(false);
                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                MastsvA.jButton1.setEnabled(true);
                MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
                MastsvA.jLabel16.setVisible(false);
                MastsvA.jLabel19.setVisible(false);
                MastsvA.jLabel17.setVisible(false);
                MastsvA.jLabel17.setEnabled(false);
                MastsvA.jLabel16.setVisible(false);
                MastsvA.jLabel19.setVisible(false);
                JOptionPane.showMessageDialog(null, "Error! Please close any open instance of MASTS in web browser.\nRestart MASTS Server Manager and try again.", "MASTS", JOptionPane.ERROR_MESSAGE);
                MastsvA.jLabel15.setText("Warning! Unable to proceed.");
                MastsvA.jLabel15.setVisible(true);
                MastsvA.warningimg.setVisible(true);
                MastsvA.jLabel12.setText("");
                MastsvA.jLabel12.setVisible(false);

                boolean gotSQLExc = false;
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                	config.MastsvCExcep.handleException(se, "Cmdtest2");
                    if (se.getSQLState().equals("XJ015")) {
                        gotSQLExc = true;
                    }
                }
                if (!gotSQLExc) {

                } else {
                    System.out.println("MASTS Processing Started! Please check your browser.");
                }
				MastsvST stop=new MastsvST();
				stop.stop();
                
            } else {

                System.out.println("Processing Not Started Properly, Please Restart the Server..." + s);
                MastsvA.jButton2.setEnabled(false);
                MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                MastsvA.jButton1.setEnabled(true);
                MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));

                MastsvA.jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14));
                MastsvA.jLabel12.setForeground(new java.awt.Color(0, 102, 0));

                MastsvA.jLabel12.setText("Error! Please restart MASTS Server Manager.");
                MastsvA.jLabel12.setVisible(true);

                MastsvA.jLabel16.setVisible(false);
                MastsvA.jLabel19.setVisible(false);
                MastsvA.jLabel17.setVisible(false);

                MastsvA.jLabel16.setVisible(false);
                MastsvA.jLabel19.setVisible(false);

                MastsvA.jTextField1.setEditable(true);
                MastsvA.jLabel17.setVisible(false);
                MastsvA.jLabel17.setEnabled(false);

                MastsvA.jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource(" ")));
                MastsvA.jLabel12.setVisible(true);
                JOptionPane.showMessageDialog(null, "Error! Please restart MASTS Server Manager.", "MASTS", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception ex) {
        	config.MastsvCExcep.handleException(ex, "Cmdtest3");
        }
    }
}
