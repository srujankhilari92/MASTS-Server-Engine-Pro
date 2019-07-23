package ethernet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class MastsvSC extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 7117311585263715260L;
    private JLabel imglabel;
    private ImageIcon img;
    private static JProgressBar pbar;
    Thread t = null;

    public MastsvSC() {

        super("MASTS");
        setSize(500, 287);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ethernet/images/NEW-MASTS_LOGO.png")));

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 3 - this.getSize().width / 3, dim.height / 6 - this.getSize().height / 6);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        img = new ImageIcon(getClass().getResource("/ethernet/images/splash_screen_masts_server.png"));
        imglabel = new JLabel(img);
        add(imglabel);
        setLayout(null);
        pbar = new JProgressBar();

        pbar.setMinimum(0);
        pbar.setMaximum(100);

        pbar.setForeground(new Color(182, 10, 10));
        imglabel.setBounds(0, 0, 500, 287);
        add(pbar);
        pbar.setPreferredSize(new Dimension(330, 5));
        pbar.setBounds(0, 282, 500, 5);

        Thread t = new Thread() {

            public void run() {
                int i = 0;
                while (i <= 100) {
                    pbar.setValue(i);
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MastsvSC.class.getName()).log(Level.SEVERE, null, ex);
                        config.MastsvCExcep.handleException(ex, "Splscreen");
                    }
                    if (i == 10) {
                        i = i + 30;
                    } else if (i == 50) {
                        i = i + 20;
                    } else if (i == 70) {
                        i = i + 30;
                    } else {
                        i = i + 10;
                    }

                }
            }
        };
        t.start();
    }
}
