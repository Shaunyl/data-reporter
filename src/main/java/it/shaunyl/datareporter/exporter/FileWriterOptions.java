package it.shaunyl.datareporter.exporter;

import lombok.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
public final class FileWriterOptions {

    @Getter
    private final String query, format, filename, directory, sheet, title, mode;

    @Getter
    private final Integer flush;

    @Service
    public static final class DataExporterBuilder {
        @Setter
        private String query, format, filename, directory = ".", sheet = "Sheet 1", title, mode;

        @Setter
        private Integer flush;

        public DataExporterBuilder() {
        }

        public FileWriterOptions build() {
            if (this.validate()) {
                return new FileWriterOptions(this);
            }
            return null;
        }

        private boolean validate() {
            return true;
        }
    }

    public FileWriterOptions(final DataExporterBuilder builder) {
        this.query = builder.query;
        this.format = builder.format;
        this.filename = builder.filename;
        this.directory = builder.directory;
        this.flush = builder.flush;
        this.sheet = builder.sheet;
        this.title = builder.title;
        this.mode = builder.mode;
    }
}
