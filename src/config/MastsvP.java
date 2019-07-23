package config;

import java.io.File;

public class MastsvP {

    public void setPer(String s) {

        try {
            File f = new File(s);

            displayIt(f);
        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Permin");
        }

    }

    public void displayIt(File node) {

        try {

            if (node.isDirectory() && !node.getAbsolutePath().contains("/Logs")) {

                Runtime.getRuntime().exec("attrib +h +s +r " + node.getAbsolutePath().toString());
                String[] subNote = node.list();
                for (String filename : subNote) {
                    displayIt(new File(node, filename));
                }
            }

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Permin");
        }
    }

}
