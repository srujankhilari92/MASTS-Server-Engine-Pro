package ethernet;

import static ethernet.MastsvA.jLabel12;
import static ethernet.MastsvA.jLabel15;
import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

/**
 *
 * @author swapnil
 */
public class MastsvLHP {

    boolean online;
    private final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(1);

    public void startScheduleTask() {

        final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
                new Runnable() {
                    public void run() {
                        try {
                            getDataFromDatabase();
                        } catch (Exception ex) {
                        	config.MastsvCExcep.handleException(ex, "Locping");
                        }
                    }
                }, 0, 20, TimeUnit.SECONDS);
    }

    private void getDataFromDatabase() {

        
        String port = MastsvA.port;
        int p = Integer.parseInt(port);
        
        try {
            while (true) {
                SocketAddress sockaddr = new InetSocketAddress("localhost", p);
                Socket socket = new Socket();
                online = true;
                try {
                    socket.connect(sockaddr, 10000);
                    
                } catch (IOException e) {
                	config.MastsvCExcep.handleException(e, "Locping");
                    online = false;
                    MastsvA.jTextField1.setEditable(true);
                    MastsvA.jButton2.setEnabled(false);
                    MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                    MastsvA.jButton1.setEnabled(true);
                    MastsvA.jButton1.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton1.setForeground(new java.awt.Color(255, 255, 255));
                    MastsvA.jButton3.setEnabled(false);
                    MastsvA.jButton2.setEnabled(false);
                    MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                    jLabel15.setText("Please start MASTS Server Engine!");
                    jLabel15.setVisible(true);
                    jLabel12.setVisible(false);
                    MastsvA.jLabel16.setVisible(false);
                    MastsvA.jLabel19.setVisible(false);
                    MastsvA.jLabel17.setVisible(false);
                    MastsvA.jLabel17.setEnabled(false);
                    MastsvA.jLabel16.setVisible(false);
                    MastsvA.jLabel19.setVisible(false);

                    if (!MastsvA.jLabel12.getText().equals("Server Stopped!")) {
                        JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please start MASTS server engine.", "MASTS", JOptionPane.ERROR_MESSAGE);
                    }

                    break;
                }
                if (!online) {

                    MastsvA.jButton2.setEnabled(false);
                    MastsvA.jButton2.setBackground(new java.awt.Color(16, 37, 63));
                    MastsvA.jButton2.setForeground(new java.awt.Color(255, 255, 255));
                    MastsvA.jButton1.setEnabled(false);
                    MastsvA.jButton3.setEnabled(false);

                    jLabel15.setText("Please start MASTS server engine!");
                    jLabel15.setVisible(true);
                    MastsvA.jLabel16.setVisible(false);

                    MastsvA.jLabel17.setVisible(false);
                    MastsvA.jLabel17.setEnabled(false);
                    MastsvA.jLabel16.setVisible(false);
                    MastsvA.jLabel19.setVisible(false);
                    if (!MastsvA.jLabel12.getText().equals("Server Stopped!")) {
                        JOptionPane.showMessageDialog(null, "Sorry! Something went wrong. Please start MASTS server engine.", "MASTS", JOptionPane.ERROR_MESSAGE);
                    }

                }
                if (online) {

                }
                Thread.sleep(1 * 10000);
            }

            scheduler.shutdown();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdowh() {

        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

}
