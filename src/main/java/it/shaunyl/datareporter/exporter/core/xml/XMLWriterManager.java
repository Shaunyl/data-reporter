package it.shaunyl.datareporter.exporter.core.xml;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriterOptions;
import it.shaunyl.datareporter.exporter.core.IWriterManager;
import it.shaunyl.datareporter.mainframe.DatabaseManager;
import it.shaunyl.datareporter.utils.DatabaseUtil;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@NoArgsConstructor @Slf4j @Scope(value = "prototype") @Service("xmlWriterManager")
public class XMLWriterManager implements IWriterManager {

    @Inject
    private DatabaseManager databaseManager;
    
    private String filename, directory, query;
    
    private int clobIndex;
    
    private XMLWriter writer = null;

    public XMLWriterManager(@Qualifier final FileWriterOptions options) {
        this.query = options.getQuery();
        this.directory = options.getDirectory();
        this.filename = options.getFilename();
    }

    @Override
    public void run() throws SQLException, IOException, ExporterTaskException {

        String call = "BEGIN ? := getxml(?, ?); END;";

        CallableStatement callable = databaseManager.createProcedure(call);
        callable.setString(2, query);
        callable.registerOutParameter(3, Types.CLOB);
        callable.registerOutParameter(1, Types.INTEGER);

        filename = (filename != null) ? String.format("%s/%s.xml", directory, filename) : String.format("%s/query_%d.xml", directory, System.currentTimeMillis());

        this.clobIndex = 3;
        databaseManager.executeProcedure(callable, query);
        java.sql.Clob clob = callable.getClob(clobIndex);
        if (clob == null) {
            throw new ExporterTaskException("Failed to extract source text.");
        }
        final String xml = DatabaseUtil.readClob(clob);

        writer = new XMLWriter(new java.io.FileWriter(filename));
        
        List<String[]> data = new ArrayList<String[]>();      
        data.add(new String[]{xml});
        
        writer.writeAll(data);

        if (writer != null) {
            writer.close();
        }
    }
}
