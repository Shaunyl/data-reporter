package it.shaunyl.datareporter.exporter;

import it.shaunyl.datareporter.exporter.core.IWriterManager;

/**
 *
 * @author Filippo Testino
 */
public interface ExporterFactory {
    IWriterManager createExporter(final FileWriterOptions options);
}
