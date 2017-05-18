package it.shaunyl.datareporter.exporter;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 *
 * @author Filippo Testino (filippo.testino@gmail.com)
 * @version 1.0
 */
public interface FileWriter {

    String[] getValidFileExtensions();

    void writeAll(final List allLines) throws IOException;

    void writeAll(final ResultSet rs, final boolean includeColumnNames)
            throws SQLException, IOException, ExporterTaskException;
}
