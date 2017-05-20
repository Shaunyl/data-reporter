package it.shaunyl.datareporter.exporter.core.xml;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Filippo Testino
 */
public class XMLWriter implements FileWriter {
    
    private Writer rawWriter;
    
    private PrintWriter printer;
    
    private String endline;

    public static final String DEFAULT_LINE_END = "\n";

    public XMLWriter(Writer writer) {
        this.rawWriter = writer;
        this.printer = new PrintWriter(writer);
    }
    
    @Override
    public String[] getValidFileExtensions() {
        return new String[]{"xml"};
    }

    @Override
    public void writeAll(List lines) {
        for (Iterator iter = lines.iterator(); iter.hasNext();) {
            String[] nextLine = (String[]) iter.next();
            writeNext(nextLine);
        }
    }

    @Override
    public void writeAll(ResultSet rs, boolean includeColumnNames)
            throws SQLException, IOException, ExporterTaskException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeNext(String[] nextLine) {
        if (nextLine == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextLine.length; i++) {
            String nextElement = nextLine[i];
            if (nextElement == null) {
                continue;
            }
            sb.append(nextElement);
        }
        sb.append(endline);
        printer.write(sb.toString());
    }

    public void flush() throws IOException {
        printer.flush();
    }

    public void close() throws IOException {
        printer.flush();
        printer.close();
        rawWriter.close();
    }
}
