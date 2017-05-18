/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.connectionmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo
 */
@Slf4j @Service
public class JdbcDerbyManager implements DerbyManager {

    private static final EmbeddedDataSource datasource = new EmbeddedDataSource(); 
    
    static {
        datasource.setDatabaseName("localdb");
        datasource.setUser("app");
        datasource.setPassword("app");
    }

    public int insert(ConnectionModel connections) throws SQLException {
        String sql = "INSERT INTO connections VALUES(?, ?, ?, ?, ?, ?)";
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, connections.getName());
        statement.setString(2, connections.getHost());
        statement.setString(3, connections.getSid());
        statement.setInt(4, connections.getPort());
        statement.setString(5, connections.getUsername());
        statement.setString(6, connections.getPassword());
        int rows = statement.executeUpdate();
        connection.commit();
        connection.close();
        statement.close();
        return rows;
    }

    public List<ConnectionModel> read() throws SQLException {
        String sql = "SELECT * FROM connections";
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        List<ConnectionModel> connections = new ArrayList<ConnectionModel>();
        while (resultSet.next()) {
            String name = resultSet.getString("NAME");
            String host = resultSet.getString("HOST");
            String sid = resultSet.getString("SID");
            int port = resultSet.getInt("PORT");
            String username = resultSet.getString("USERNAME");
            String password = resultSet.getString("PASSWORD");

            ConnectionModel connectionModel = new ConnectionModel(name, host, sid, port, username, password);
            connections.add(connectionModel);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return connections;
    }

    public ConnectionModel read(String connectionName) throws SQLException {
        String sql = "SELECT * FROM connections WHERE name = ?";
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, connectionName);
        ResultSet resultSet = statement.executeQuery();

        ConnectionModel connectionModel = null;
        while (resultSet.next()) {
            String name = resultSet.getString("NAME");
            String host = resultSet.getString("HOST");
            String sid = resultSet.getString("SID");
            int port = resultSet.getInt("PORT");
            String username = resultSet.getString("USERNAME");
            String password = resultSet.getString("PASSWORD");

            connectionModel = new ConnectionModel(name, host, sid, port, username, password);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return connectionModel;
    }

    public void delete(String connectionName) throws SQLException {
        String sql = "DELETE FROM connections WHERE name = ?";
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, connectionName);
        statement.executeUpdate();
        
        statement.close();
        connection.close();
    }
    
    public int update(ConnectionModel connections)  throws SQLException {
        String sql = "UPDATE connections SET host = ?, sid = ?, port = ?, username = ?, password = ? WHERE name = ?";
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, connections.getHost());
        statement.setString(2, connections.getSid());
        statement.setInt(3, connections.getPort());
        statement.setString(4, connections.getUsername());
        statement.setString(5, connections.getPassword());
        statement.setString(6, connections.getName());
        int rows = statement.executeUpdate();
        connection.commit();
        
        statement.close();
        connection.close();
        
        return rows;
    }

    public boolean find(String name) throws SQLException {
        String sql = "SELECT count(*) FROM connections WHERE name = ?";
        Connection connection = datasource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        int rows = 0;
        while (resultSet.next()) {
            rows = resultSet.getInt(1);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return rows > 0;
    } 
}