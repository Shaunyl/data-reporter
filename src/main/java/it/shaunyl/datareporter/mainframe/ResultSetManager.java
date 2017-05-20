package it.shaunyl.datareporter.mainframe;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author Filippo Testino
 */
public interface ResultSetManager {

    String[] getColumnNames(final ResultSetMetaData metadata) throws SQLException;

    String extractStringValue(ResultSet resultSet, int columnType, int columnIndex) throws SQLException;
    
    int resultSetCount(final ResultSet resultSet);
}
