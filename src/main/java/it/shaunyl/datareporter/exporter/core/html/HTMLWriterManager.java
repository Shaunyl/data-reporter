package it.shaunyl.datareporter.exporter.core.html;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriterOptions;
import it.shaunyl.datareporter.exporter.core.IWriterManager;
import it.shaunyl.datareporter.mainframe.DatabaseManager;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@NoArgsConstructor @Scope(value = "prototype") @Service("htmlWriterManager")
public class HTMLWriterManager implements IWriterManager {

    @Inject
    private DatabaseManager databaseManager;
    
    @Inject
    private BeanFactory beanFactory;
    
    private String format, query, directory;
    
    public HTMLWriterManager(@Qualifier final FileWriterOptions options) {
        this.format = options.getFormat();
        this.query = options.getQuery();
        this.directory = options.getDirectory();
    }

    @Override
    public void run() throws SQLException, IOException, ExporterTaskException {

        String filename = String.format("%s/QUERY-%d.%s", directory, System.currentTimeMillis(), format);

        @Cleanup
        ResultSet resultSet = databaseManager.executeQuery(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        HTMLWriter writer = (HTMLWriter) beanFactory.getBean("htmlWriter", new FileWriter(filename));

        if (!Arrays.asList(writer.getValidFileExtensions()).contains(format)) {
            throw new ExportException("The format '" + format + "' is not supported.");
        }

        writer.writeAll(resultSet, true);
        writer.close();
    }

}