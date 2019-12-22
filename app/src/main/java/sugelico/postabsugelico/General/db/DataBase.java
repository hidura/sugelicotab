package sugelico.postabsugelico.General.db;

import java.sql.*;

/**
 * Created by hidura on 7/2/2015.
 */
public class DataBase {
    public static final String dbname="AlacartaMT";
    public Connection DataBase() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:alacarta.db");
        return conn;
    }
}
