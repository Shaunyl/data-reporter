package it.shaunyl.datareporter.exporter.core.excel;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriter;
import it.shaunyl.datareporter.mainframe.ResultSetManager;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.inject.Inject;
import lombok.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino (filippo.testino@gmail.com)
 * @version 1.0
 */
@NoArgsConstructor @Scope(value = "prototype") @Service("excelWriter")
public class EXCELWriter implements FileWriter {

    @Inject
    private ResultSetManager resultSetManager;
    
    private FileOutputStream fileOutputStream;

    @Setter @Getter
    private Workbook workbook;

    private Sheet sheet;

    private String format;

    public static final String DEFAULT_FORMAT = "xls";

    public static final int EXCEL_XLS_ROW_LIMIT = 65536;
    
    public EXCELWriter(@Qualifier File file, @Qualifier Sheet sheet, @Qualifier String format) throws IOException {
        this.fileOutputStream = new FileOutputStream(file);
        this.sheet = sheet;
        this.workbook = sheet.getWorkbook();
        this.format = format;
    }

    @Override
    public void writeAll(@NonNull final ResultSet rs, boolean includeColumnNames)
            throws SQLException, IOException, ExporterTaskException {

        ResultSetMetaData metadata = rs.getMetaData();

        if (includeColumnNames) {
            writeColumnNames(metadata);
        }

        int columnCount = metadata.getColumnCount();

        int k = includeColumnNames ? 1 : 0;
        this.sheet = workbook.getSheet(sheet.getSheetName());

        if (!rs.isBeforeFirst()) {
            throw new ExporterTaskException("Warnings: The table is empty. The EXCEL file was not created.\n");
        } else {
            rs.beforeFirst();
        }

        int r = k;
        while (rs.next()) {
            if (k + 1 > EXCEL_XLS_ROW_LIMIT) {
                throw new ExporterTaskException(String.format("EXCEL: Outside allowable range (0, %d). Data was truncated\n", EXCEL_XLS_ROW_LIMIT));
            }

            String[] nextLine = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                nextLine[i] = resultSetManager.extractStringValue(rs, metadata.getColumnType(i + 1), i + 1);
            }

            Row row = sheet.createRow((k++) - r + 1);
            writeNext(nextLine, row);
        }
    }

    @Override
    public void writeAll(final List lines) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeNext(String[] nextLine, Row row) {

        if (nextLine == null) {
            return;
        }

        for (int i = 0; i < nextLine.length; i++) {

            String nextElement = nextLine[i];

            Cell cell = row.createCell(i);
            cell.setCellValue(nextElement == null ? "" : nextElement);
            if (format.equals("xlsx")) {
                cell.setCellStyle(sheet.getColumnStyle(i));
            }
        }
    }

    protected void writeColumnNames(@NonNull final ResultSetMetaData metadata)
            throws SQLException {

        Row row = sheet.createRow(0);

        int columnCount = metadata.getColumnCount();

        String[] nextLine = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            nextLine[i] = metadata.getColumnName(i + 1);
            Cell cell = row.createCell(i);
            cell.setCellValue(nextLine[i] == null ? "" : nextLine[i]);
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    public String[] getValidFileExtensions() {
        return new String[]{ "xls" };
    }

    public void dispose() throws IOException {
        if (workbook instanceof SXSSFWorkbook) {
            ((SXSSFWorkbook) workbook).dispose();
        }
    }

    public void close() throws IOException {
        workbook.write(fileOutputStream);
        this.fileOutputStream.close();
        this.dispose();
    }
}