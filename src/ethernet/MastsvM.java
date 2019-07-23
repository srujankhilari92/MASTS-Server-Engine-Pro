package ethernet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import config.MastsvCong;
import config.MastsvL;

public class MastsvM {

    public static void main(String args[]) throws Exception {
    	
        MastsvSC s = new MastsvSC();
        s.setVisible(true);

        Thread.sleep(6500);
        s.dispose();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                MastsvCong c = new MastsvCong();
                c.makeConfigProp();
                MastsvLP.Loadproperties();
                MastsvL.createLoggs();
                try {
                    new MastsvA().setVisible(true);
                } catch (IOException ex) {
                	config.MastsvCExcep.handleException(ex, "MainActivity");
                    Logger.getLogger(MastsvM.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
