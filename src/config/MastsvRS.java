/**
 * @Class Name : RestartSys.java
 */
package config;

import java.io.File;

import javax.swing.JOptionPane;

import ethernet.MastsvLP;

/**
 * @author : Varutra Consulting Pvt. Ltd.
 * @Create On : Jul 6, 2015 6:23:23 PM
 * @License : Copyright � 2014 Varutra Consulting Pvt. Ltd.- All rights
 * reserved.
 */
public class MastsvRS {

    public boolean restart() {
        boolean res = false;

        int answer = JOptionPane.showConfirmDialog(null, "System restart required to apply the license.\nPlease save your work and close all applications.\nClick Yes to restart now.", "MASTS", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            res = true;
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("shutdown -r -t 30");
                System.out.print("Please wait System is going to SHUT DOWN!!");

                File n = new File(MastsvLP.dummypath + "/2sjd45s56255f455f.vml");
                n.createNewFile();

            } catch (Exception e) {
                res = false;
                config.MastsvCExcep.handleException(e, "RestartSys");

            }
        } else if (answer == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "MASTS setup has detected that there are pending changes that require a system restart.\nPlease restart the system to complete the installation.", "MASTS", JOptionPane.INFORMATION_MESSAGE);
            res = false;

        }

        return res;

    }

    public boolean restartRenew() {
        boolean res = false;

		//JOptionPane.showMessageDialog(null,"Please copy the text and send to us ! Then cancle the window your system need restart","Stop",JOptionPane.WARNING_MESSAGE);
        res = true;
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("shutdown -r -t 30");
            System.out.print("Please wait System is going to SHUT DOWN!!");

            //Restart Forcefully.....
            File n = new File(MastsvLP.dummypath + "/2sjd45s56255f455f.vml");
            n.createNewFile();
            //Restart Forcefully.....
        } catch (Exception e) {
            res = false;
            config.MastsvCExcep.handleException(e, "RestartSys");
        }

        return res;
    }

    //Restart Forcefully.....
    public boolean check() {
        MastsvLP.Loadproperties();
        if (new File(MastsvLP.dummypath).exists()) {
            if (new File(MastsvLP.dummypath).list().length == 2) {
                return true;
            } else if (new File(MastsvLP.dummypath + "/2sjd45s56255f455f.vml").exists()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public boolean checkF() {
        MastsvLP.Loadproperties();
        if (new File(MastsvLP.dummypath).exists()) {
            if (new File(MastsvLP.dummypath + "/2sjd45s56255f455f.vml").exists()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

	//Restart Forcefully.....
}
