package it.shaunyl.datareporter.exporter.core;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Filippo Testino
 */
public interface IWriterManager {
    void run() throws SQLException, IOException, ExporterTaskException;
}
