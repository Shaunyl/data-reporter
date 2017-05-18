package it.shaunyl.datareporter.login;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Filippo Testino
 */
public interface ConnectionManager {

    void connect(String url, String user, String pwd) throws SQLException;
    
    Connection getConnection();
}
