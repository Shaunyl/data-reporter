package it.shaunyl.datareporter.exporter.core.excel;

import it.shaunyl.datareporter.exporter.ExporterFactory;
import it.shaunyl.datareporter.exporter.FileWriterOptions;
import it.shaunyl.datareporter.exporter.core.IWriterManager;
import javax.inject.Inject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service("excelFactory")
public class EXCELWriterManagerFactory implements ExporterFactory {
    
    @Inject
    private BeanFactory beanFactory;
    
    public IWriterManager createExporter(final FileWriterOptions options) {
        return (EXCELWriterManager) beanFactory.getBean("excelWriterManager", options);
    }
}
