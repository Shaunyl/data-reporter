package it.shaunyl.datareporter.exporter.core.html;

import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.FileWriter;
import it.shaunyl.datareporter.mainframe.ResultSetManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@NoArgsConstructor @Scope(value = "prototype") @Service("htmlWriter")
public class HTMLWriter implements FileWriter {

    @Inject
    private ResultSetManager resultSetManager;
    private Writer rawWriter;
    private PrintWriter printer; 
    private StringBuilder html = new StringBuilder();

    public HTMLWriter(@Qualifier final Writer writer) throws IOException {
        this.rawWriter = writer;
        this.printer = new PrintWriter(writer);
        html.append(Tag.open(Tag.HTML));
        html.append(Tag.open(Tag.HEAD));
        html.append(Tag.fill(Tag.TITLE, "Data Report"));
        html.append("<link href=\"main.css\" rel=\"stylesheet\" type=\"text/css\">");
        html.append(Tag.close(Tag.HEAD));
        html.append(Tag.open(Tag.BODY));
        html.append(Tag.open(Tag.DIV, "class=\"main_style\""));
        html.append(Tag.open(Tag.TABLE, "width=\"100%\" cellpadding=\"2\" cellspacing =\"2\""));
    }

    public String[] getValidFileExtensions() {
        return new String[]{"html"};
    }

    public void writeAll(List allLines) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeAll(ResultSet rs, boolean includeColumnNames) throws SQLException, IOException, ExporterTaskException {
        ResultSetMetaData metadata = rs.getMetaData();

        if (includeColumnNames) {
            html.append(Tag.open(Tag.THEAD));
            html.append(Tag.open(Tag.TR));
            writeColumnNames(metadata);
            html.append(Tag.close(Tag.TR));
            html.append(Tag.close(Tag.THEAD));
        }

        int columnCount = metadata.getColumnCount();

        if (!rs.isBeforeFirst()) {
            throw new ExporterTaskException("Warnings: The table is empty. The HTML file has not been created.\n");
        } else {
            rs.beforeFirst();
        }

        html.append(Tag.open(Tag.TBODY));
        while (rs.next()) {
            String[] nextLine = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                nextLine[i] = resultSetManager.extractStringValue(rs, metadata.getColumnType(i + 1), i + 1);
            }

            writeNext(nextLine);
        }
        html.append(Tag.close(Tag.TBODY));
        html.append(Tag.close(Tag.TABLE));
        html.append(Tag.close(Tag.DIV));
        html.append(Tag.close(Tag.BODY));
        html.append(Tag.close(Tag.HTML));
        this.printer.write(html.toString());
    }

    public void writeNext(String[] nextLine) {

        if (nextLine == null) {
            return;
        }

        html.append(Tag.open(Tag.TR));
        for (int i = 0; i < nextLine.length; i++) {
            String nextElement = nextLine[i];
            html.append(Tag.fill(Tag.TD, nextElement));
        }
        html.append(Tag.close(Tag.TR));
    }

    protected void writeColumnNames(@NonNull final ResultSetMetaData metadata)
            throws SQLException {

        int columnCount = metadata.getColumnCount();

        String[] nextLine = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            nextLine[i] = metadata.getColumnName(i + 1);
            html.append(Tag.fill(Tag.TH, nextLine[i]));
        }
    }

    public void close() throws IOException {
        this.printer.flush();
        this.printer.close();
        this.rawWriter.close();
    }

    enum Tag {

        TH, TR, TD, HTML, HEAD, TITLE, BODY, H1, DIV, TABLE, THEAD, TBODY, P;

        public static String fill(final Tag tag, final String content) {
            return Tag.open(tag) + content + Tag.close(tag);
        }
        
        public static String open(final Tag tag) {
            return "<" + tag.name() + ">";
        }

        public static String close(final Tag tag) {
            return "</" + tag.name() + ">";
        }
        
        public static String open(final Tag tag, final String... attributes) {
            String text = "<" + tag.name();
            for (String attribute : attributes) {
                text += " " + attribute;
            }
            text += ">";
            return text;
        }
    };
}
