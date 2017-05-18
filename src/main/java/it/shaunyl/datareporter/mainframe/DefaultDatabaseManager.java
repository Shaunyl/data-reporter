package it.shaunyl.datareporter.mainframe;

import it.shaunyl.datareporter.login.ConnectionManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo
 */
@Service @Slf4j
public class DefaultDatabaseManager implements DatabaseManager {

    @Inject // spring (in realtù è uno standard, qui usa spring perchè lo usiamo noi) capisce che serve sta istanza e se la va allora a cercare da solo..
    private ConnectionManager connectionManager;

    private ResultSet resultSet = null;

    private String lastQuery;

    @Override //FIXME.. non va qui, e forse manco dovrebbe esistere..
    public String getLastQueryExecuted() {
        if (lastQuery == null || lastQuery.isEmpty()) {
            log.error("No data available. In order to import some data, you have to retrieve a valid, not empty result set first.");
            throw new RuntimeException(""); //FIXME
        }
        return lastQuery;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        lastQuery = sql;
        return this.executeQuery(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public ResultSet executeQuery(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        PreparedStatement statement = this.createPreparedStatement(sql, resultSetType, resultSetConcurrency);
        this.resultSet = statement.executeQuery();
        lastQuery = sql;
        return resultSet;
    }

    @Override
    public CallableStatement createProcedure(String sql) throws SQLException {
        CallableStatement callable = this.createCallableStatement(sql);
        lastQuery = sql;
        return callable;
    }

    @Override
    public ResultSet executeProcedure(CallableStatement callable, String sql) throws SQLException {
        this.resultSet = callable.executeQuery();
        lastQuery = sql;
        return resultSet;
    }

    @Override
    public PreparedStatement createPreparedStatement(String sql) throws SQLException {
        return this.createPreparedStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public PreparedStatement createPreparedStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        Connection connection = connectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        lastQuery = sql;
        return statement;
    }

    private CallableStatement createCallableStatement(String sql) throws SQLException {
        Connection connection = connectionManager.getConnection();
        CallableStatement callable = connection.prepareCall(sql);
        return callable;
    }
}
