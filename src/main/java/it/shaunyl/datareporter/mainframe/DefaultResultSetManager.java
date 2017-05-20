package it.shaunyl.datareporter.mainframe;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service @Slf4j
public class DefaultResultSetManager implements ResultSetManager {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

    private static final String BLOB_FORMATTER = "<BLOB>";

    @Override
    public final String[] getColumnNames(final ResultSetMetaData metadata) throws SQLException {
        int columnCount = metadata.getColumnCount();
        String[] nextLine = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            nextLine[i] = metadata.getColumnName(i + 1);
        }
        return nextLine;
    }

    @Override
    public String extractStringValue(ResultSet resultSet, int columnType, int columnIndex) throws SQLException {
        String value = "";

        switch (columnType) {
            case Types.BIT:
                Object bit = resultSet.getObject(columnIndex);
                if (bit != null) {
                    value = String.valueOf(bit);
                }
                break;
            case Types.BOOLEAN:
                boolean bool = resultSet.getBoolean(columnIndex);
                if (!resultSet.wasNull()) {
                    value = Boolean.toString(bool);
                }
                break;
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.DOUBLE:
            case Types.REAL:
            case Types.NUMERIC:
                BigDecimal decimal = resultSet.getBigDecimal(columnIndex);
                if (decimal != null) {
                    value = "" + decimal.doubleValue();
                }
                break;
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.SMALLINT:
                int integer = resultSet.getInt(columnIndex);
                if (!resultSet.wasNull()) {
                    value = "" + integer;
                }
                break;
            case Types.DATE:
                Date date = resultSet.getDate(columnIndex);
                if (date != null) {
                    value = DATE_FORMATTER.format(date);
                }
                break;
            case Types.TIME:
                Time time = resultSet.getTime(columnIndex);
                if (time != null) {
                    value = time.toString();
                }
                break;
            case Types.TIMESTAMP:
                Timestamp timestamp = resultSet.getTimestamp(columnIndex);
                if (timestamp != null) {
                    value = TIMESTAMP_FORMATTER.format(timestamp);
                }
                break;
            case Types.LONGNVARCHAR:
            case Types.VARCHAR:
            case Types.CHAR:
                value = resultSet.getString(columnIndex);
                break;
            case Types.BLOB:
                value = BLOB_FORMATTER;
                break;
            default:
                value = "";
        }

        if (value == null) {
            value = "";
        }
        return value;
    }

    @Override
    public int resultSetCount(ResultSet resultSet) {
        try {
            resultSet.last();
            int rows = resultSet.getRow();
            return rows;
        } catch (SQLException sqle) {
            log.error("", sqle);
            return 0;
        }
    }
}
