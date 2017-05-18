package it.shaunyl.datareporter.mainframe;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Filippo Testino
 */
public interface DatabaseManager {

    ResultSet executeQuery(String sql) throws SQLException;

    ResultSet executeQuery(String sql, int resultSetType, int resultSetConcurrency) throws SQLException;

    String getLastQueryExecuted();

    CallableStatement createProcedure(String sql) throws SQLException;
    
    ResultSet executeProcedure(CallableStatement callable, String sql) throws SQLException;
    
    PreparedStatement createPreparedStatement(String sql) throws SQLException;
    
    PreparedStatement createPreparedStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException;
}
