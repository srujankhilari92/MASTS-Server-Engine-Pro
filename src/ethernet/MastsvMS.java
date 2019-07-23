package ethernet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import config.MastsvCong;

public class MastsvMS extends Thread {

    public void run() {
        try {

            MastsvCong c = new MastsvCong();
           String res= c.config();
           if(res.equals("done")){
        	   ProcessBuilder builder = new ProcessBuilder(
                       "cmd.exe", "/c", "cd " + MastsvLP.dummypath + "MASTS && mvn jetty:stop");

               builder.redirectErrorStream(true);
               Process p = builder.start();

               BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
               String line;
               int s = 0;
               while (true) {

                   line = r.readLine();
                   if (line == null) {
                       break;
                   } else if (line.contains("[INFO] Final Memory:") || line.equals("[INFO] Jetty not running")) {
                       s = 1;

                       break;
                   } else {
                	   
                       s = 2;

                   }

               }
               if (s == 1) {

                   if (new File(MastsvLP.dummypath + "MASTS").exists()) {
                       c.config();
                      MastsvCT cd = new MastsvCT();
                       try {
                           cd.startserver();
                           MastsvA.jLabel17.setEnabled(false);
                           MastsvA.jLabel16.setVisible(false);
                           MastsvA.jLabel19.setVisible(false);
                           MastsvA.jLabel17.setVisible(false);

                           MastsvA.jLabel12.setVisible(true);

                       } catch (Exception e) {
                         
                           config.MastsvCExcep.handleException(e, "Mavstop");
                       }
                   } else {
                       c.config();
                      MastsvCT cd = new MastsvCT();
                       try {
                           cd.startserver();
                       } catch (Exception e) {
                    	   config.MastsvCExcep.handleException(e, "Mavstop");
                       }
                   }
               } else {
                   System.out.println("Processing Not Started Properly, Please Restart the Server..." + s);

                   MastsvA.jButton2.setEnabled(false);
                   MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                   MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                   MastsvA.jButton1.setEnabled(false);
                   MastsvA.jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12));
                   MastsvA.jLabel12.setForeground(new java.awt.Color(0, 102, 0));

                   MastsvA.jLabel12.setText("Configuration completed! Please restart MASTS Server Manager.");
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
                  
               } 
           }else{

               System.out.println("Processing Not Started Properly, Already Used...");
               MastsvA.jButton2.setEnabled(false);
               MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
               MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
               MastsvA.jButton1.setEnabled(false);
               MastsvA.jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12));
               MastsvA.jLabel12.setForeground(new java.awt.Color(255, 51, 51));
               MastsvA.jLabel12.setText("Configuration failed! MASTS Server Manager cannot proceed.");
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

           
           }
            

        } catch (Exception ex) {
        	config.MastsvCExcep.handleException(ex, "Mavstop");
        }
    }

}
