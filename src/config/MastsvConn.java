package config;

import ethernet.MastsvCB;
import java.sql.DriverManager;
import java.sql.SQLException;

import ethernet.MastsvLP;

public class MastsvConn {

    public static java.sql.Connection create(String dbName) {
    	System.setProperty("derby.stream.error.field", "MastsvConnF.DEV_NULL");
        java.sql.Connection con = null;
        MastsvCB cobj = new MastsvCB();
        String dbkey = cobj.getKey3();
        dbkey = dbkey.concat(cobj.getKey4());
        
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        MastsvLP.Loadproperties();
        String connectionURL = MastsvLP.dbpath + dbName + ";create=true;dataEncryption=true;encryptionKey=" + dbkey + ";";
        String t1 = MastsvLP.test1;
        String t2 = MastsvLP.test2;

        try {

            Class.forName(driver).newInstance();

            con = DriverManager.getConnection(connectionURL, t1, t2);

        } catch (Exception e) {

        	config.MastsvCExcep.handleException(e, "Connec");
        }

        return con;
    }

    public static java.sql.Connection createagain(String dbName) throws SQLException {
    	System.setProperty("derby.stream.error.field", "MastsvConnF.DEV_NULL");
    	java.sql.Connection conn = null;
        MastsvCB cobj = new MastsvCB();
        String dbkey = cobj.getKey3();
        dbkey = dbkey.concat(cobj.getKey4());
        MastsvLP.Loadproperties();
        String connectionURL = MastsvLP.dbpath + dbName + ";create=true;dataEncryption=true;dataEncryption=true;encryptionKey=" + dbkey + ";";
        String t1 = MastsvLP.test1;
        String t2 = MastsvLP.test2;


        try {

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

            conn = DriverManager.getConnection(connectionURL, t1, t2);

        } catch (Exception e) {
        	config.MastsvCExcep.handleException(e, "Connec");
        }

        return conn;
    }

}
