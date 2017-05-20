package it.shaunyl.datareporter.exporter.core.graph;

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
@Service("graphFactory")
public class GraphWriterManagerFactory implements ExporterFactory {
    
    @Inject
    private BeanFactory beanFactory;
    
    public IWriterManager createExporter(final FileWriterOptions options) {
        return (GraphWriterManager) beanFactory.getBean("graphWriterManager", options);
    }
}
