package it.shaunyl.datareporter.exporter.core.graph;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriterOptions;
import it.shaunyl.datareporter.exporter.core.IWriterManager;
import it.shaunyl.datareporter.exporter.graph.IChart;
import it.shaunyl.datareporter.exporter.graph.core.CartesianChart;
import it.shaunyl.datareporter.mainframe.DatabaseManager;
import java.io.File;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
@NoArgsConstructor @Scope(value = "prototype") @Service("graphWriterManager")
public class GraphWriterManager implements IWriterManager {

    @Inject
    private DatabaseManager databaseManager;
    
    @Inject
    private BeanFactory beanFactory;
    
    private String format, query, directory, title, mode;

    public GraphWriterManager(@Qualifier final FileWriterOptions options) {
        this.format = options.getFormat();
        this.query = options.getQuery();
        this.directory = options.getDirectory();
        this.title = options.getTitle();
        this.mode = options.getMode();

    }

    public void run() throws SQLException, IOException, ExporterTaskException {
        String filename = String.format("%s/image_%d.%s", directory, System.currentTimeMillis(), format);

        @Cleanup
        ResultSet resultSet = databaseManager.executeQuery(query);

        ResultSetMetaData metaData = resultSet.getMetaData();

        String xlabel = metaData.getColumnName(1);

        String ylabel = "Temp"; // FIXME

        IChart chart = null;
        try {
            if (mode.matches("cartesian|timeseries")) {
                chart = new CartesianChart(title, true, xlabel, ylabel);
            }
            
            GraphWriter writer = (GraphWriter) beanFactory.getBean("graphWriter", new File(filename), chart, format, mode);
            
            writer.writeAll(resultSet, true);
            writer.close();
        } catch (IOException e) {
            throw new UnexpectedException("Seems like something went bad with the Graph writer.", e);
        }
    }
}