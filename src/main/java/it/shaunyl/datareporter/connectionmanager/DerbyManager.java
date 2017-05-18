/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.connectionmanager;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Filippo
 */
public interface DerbyManager {

    int insert(ConnectionModel connections) throws SQLException;

    List<ConnectionModel> read() throws SQLException;

    ConnectionModel read(String connectionName) throws SQLException;

    void delete(String connectionName) throws SQLException;

    int update(ConnectionModel connectionModel) throws SQLException;

    boolean find(String name) throws SQLException;
}
