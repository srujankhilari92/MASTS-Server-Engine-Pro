/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ethernet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import config.MastsvCong;
import config.MastsvFED;

public class MastsvLP {

    public static String source = "s";
    public static String target = "t";
    public static String dbpath = "p";
    public static String dummypath = "d";
    public static String renewpath = "r";
    public static String renewdestpath = "rd";

    public static String test1 = "t1";
    public static String test2 = "t2";

    public static void Loadproperties() {
        MastsvLP load = new MastsvLP();
        load.load();
    }

    public String format() {
        String dest = new File("afile").getAbsolutePath().replaceAll("afile", "");

        dest = dest.replaceAll("%5b", "[").replaceAll("%5B", "[").replaceAll("%5d", "]").replaceAll("%5D", "]").replaceAll("%20", " ");
        dest = dest.replaceAll("%7b", "{").replaceAll("%7B", "{").replaceAll("%7d", "}").replaceAll("%7D", "}").replaceAll("%23", "#");
        dest = dest.replaceAll("%25", "%").replaceAll("%5e", "^").replaceAll("%5E", "^").replaceAll("%3D", "=").replaceAll("%3d", "=").replaceAll("%60", "`");

        return dest;
    }

    public void load() {
        MastsvLP l = new MastsvLP();

        String in = l.format();

        if (new File(in + "/WebClient/build-1Conf.properties").exists()) {

            if (source == "s" && target == "t" && dummypath == "d" && dbpath == "p" && renewpath == "r" && renewdestpath == "rd") {

                MastsvCong c = new MastsvCong();
                String key = c.toChange(115) + c.toChange(48) + c.toChange(113) + c.toChange(114)
                        + c.toChange(98) + c.toChange(49) + c.toChange(116) + c.toChange(119) + c.toChange(97) + c.toChange(52);

                try {
                    FileInputStream fis111 = new FileInputStream(in + "/WebClient/build-1Conf.properties");
                    FileOutputStream fos111 = new FileOutputStream(in + "/WebClient/build-2Conf.properties");
                    MastsvFED.decrypt(key, fis111, fos111);
                } catch (Throwable e) {

                    System.out.println("Exception Load Line No.: " + e.getStackTrace()[2].getLineNumber());
                }
                try {

                    Properties props = new Properties();

                    InputStream inputStream = new FileInputStream(new File(in + "/WebClient/build-2Conf.properties"));

                    if (inputStream != null) {
                        props.load(inputStream);

                        MastsvCB cc = new MastsvCB();

                        source = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("s").toString());
                        target = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("t").toString());
                        dummypath = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("d").toString());

                        dbpath = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("p").toString()).replaceAll("derby:/", "derby:");;
                        renewpath = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("r").toString());;
                        renewdestpath = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("rd").toString());;

                        test1 = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("t1").toString());;
                        test2 = MastsvdAD.decrypt(cc.getKey1(), cc.getKey2(), props.getProperty("t2").toString());;

                    }
                    inputStream.close();
                } catch (Exception ex) {
                	config.MastsvCExcep.handleException(ex, "Loaperties");
                    
                }

                new File(in + "/WebClient/build-2Conf.properties").delete();
            }
        }

    }

}
