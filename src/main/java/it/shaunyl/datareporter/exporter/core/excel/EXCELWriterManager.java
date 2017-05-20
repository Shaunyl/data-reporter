package it.shaunyl.datareporter.exporter.core.excel;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriterOptions;
import it.shaunyl.datareporter.exporter.core.IWriterManager;
import it.shaunyl.datareporter.mainframe.DatabaseManager;
import java.io.File;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@NoArgsConstructor @Scope(value = "prototype") @Service("excelWriterManager")
public class EXCELWriterManager implements IWriterManager {

    @Inject
    private DatabaseManager databaseManager;
    
    @Inject
    private BeanFactory beanFactory;
    
    private String format, query, sheetName, directory;
    
    private int flush;

    public EXCELWriterManager(@Qualifier final FileWriterOptions options) {
        this.format = options.getFormat();
        this.query = options.getQuery();
        this.sheetName = options.getSheet();
        this.directory = options.getDirectory();
        this.flush = options.getFlush();
    }

    public void run() throws SQLException, IOException, ExporterTaskException {

        Workbook workbook = null;
        if (format.equals("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook(flush);
        }

        Sheet sheet = workbook.createSheet(sheetName);

        String filename = String.format("%s/QUERY-%d.%s", directory, System.currentTimeMillis(), format);

        @Cleanup
        ResultSet resultSet = databaseManager.executeQuery(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        EXCELWriter writer = (EXCELWriter) beanFactory.getBean("excelWriter", new File(filename), sheet, format);
        
        if (!Arrays.asList(writer.getValidFileExtensions()).contains(format)) {
            throw new ExportException("The format '" + format + "' is not supported.");
        }

        if (writer.getWorkbook() != null) {
            writer.setWorkbook(workbook);
        }
        writer.writeAll(resultSet, true);
        writer.close();
    }
}
