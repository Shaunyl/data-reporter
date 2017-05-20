package it.shaunyl.datareporter.login;

import java.sql.*;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Slf4j @Service
public class DefaultConnectionManager implements ConnectionManager {

    private static OracleDataSource datasource = null;
    
    private Connection connection = null;

    static {
        try {
            datasource = new OracleDataSource();
        } catch (SQLException sqle) {
            log.error("Oracle Datasource cannot be loaded. Maybe bad arguments in input.", sqle);
        }
    }

    @Override
    public void connect(final String url, final String user, final String pwd) throws SQLException {
        datasource.setURL(url);
        connection = datasource.getConnection(user, pwd);
        connection.setReadOnly(true);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public Connection getConnection() {      
        return this.connection;
    }
}
