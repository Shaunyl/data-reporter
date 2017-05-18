package it.shaunyl.datareporter.exporter.core.html;

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
@Service("htmlFactory")
public class HTMLWriterManagerFactory implements ExporterFactory {

    @Inject
    private BeanFactory beanFactory;
    
    public IWriterManager createExporter(final FileWriterOptions options) {
        return (HTMLWriterManager) beanFactory.getBean("htmlWriterManager", options);
    }
}
