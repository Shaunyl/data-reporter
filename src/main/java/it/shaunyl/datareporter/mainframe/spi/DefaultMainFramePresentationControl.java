package it.shaunyl.datareporter.mainframe.spi;

import it.shaunyl.datareporter.events.PopulateGridEvent;
import it.shaunyl.datareporter.events.QuickQueryEvent;
import it.shaunyl.datareporter.events.RiseMainEvent;
import it.shaunyl.datareporter.mainframe.Row;
import it.shaunyl.datareporter.exception.ExporterTaskException;
import it.shaunyl.datareporter.exporter.ExporterFactory;
import it.shaunyl.datareporter.exporter.FileWriterOptions;
import it.shaunyl.datareporter.mainframe.DatabaseManager;
import it.shaunyl.datareporter.mainframe.PackCategory;
import it.shaunyl.datareporter.mainframe.PackCategoryReader;
import it.shaunyl.datareporter.mainframe.PackQuery;
import it.shaunyl.datareporter.mainframe.ResultSetManager;
import it.shaunyl.datareporter.mainframe.ui.MainFramePresentation;
import it.shaunyl.eventbus.EventBus;
import it.shaunyl.eventbus.EventBusListener;
import it.shaunyl.presentationmodel.role.GridHeaderProvider;
import it.shaunyl.presentationmodel.role.spi.DefaultDescribable;
import it.tidalwave.role.*;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.role.ui.spi.*;
import it.tidalwave.util.Finder;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.util.spi.FinderSupport;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.*;
import javax.inject.Inject;
import javax.swing.*;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Filippo Testino
 */
@Slf4j @Service
public class DefaultMainFramePresentationControl {

    @Inject
    private MainFramePresentation presentation;

    @Inject
    private DatabaseManager databaseManager;

    @Inject
    private ResultSetManager resultSetManager;

    @Inject
    private PackCategoryReader packQueryManager;

    @Inject
    private EventBus eventBus;

    @Inject
    private BeanFactory beanFactory;

    @Inject
    private FileWriterOptions.DataExporterBuilder dataExporterBuilder;

    @Inject
    private ExecutorService executorService;

    private final EventBusListener<RiseMainEvent> riseEventListener = new EventBusListener<RiseMainEvent>() {
        @Override
        public void notify(final RiseMainEvent event) {
            rise();
        }
    };
    
    private final EventBusListener<PopulateGridEvent> populateGridEventListener = new EventBusListener<PopulateGridEvent>() {
        @Override
        public void notify(final PopulateGridEvent event) {
            fetchData(event.getSql());
        }
    };    
    
    private final Action goAction = new AbstractAction("Go!") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent ae) {
            PresentationModel pm = presentation.getQueryCurrentlySelected();
            final PackQuery query = pm.as(PackQuery.class);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    fetchData(query.getSql().trim().replace(";", ""));
                }
            });
        }
    };

    private final Action quickQueryAction = new AbstractAction("Quick Query") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            eventBus.publish(new QuickQueryEvent());
        }
    };

    private final Action selectQueryCategoryAction = new AbstractAction("select") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            presentation.populateQueryCombo(createPresentationModelQueryPack());
        }
    };

    private final Action exportXmlAction = new AbstractAction("XML") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String sql = databaseManager.getLastQueryExecuted();

                    dataExporterBuilder.setFormat("xml");
                    dataExporterBuilder.setQuery(sql);

                    exportToFile("xml");
                }
            });
        }
    };

    private final Action exportExcelAction = new AbstractAction("EXCEL") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String sql = databaseManager.getLastQueryExecuted();

                    dataExporterBuilder.setSheet("Sheet Prova");
                    dataExporterBuilder.setFlush(10);
                    dataExporterBuilder.setFormat("xls");
                    dataExporterBuilder.setQuery(sql);

                    exportToFile("excel");
                }
            });
        }
    };

    private final Action exportHtmlAction = new AbstractAction("HTML") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String sql = databaseManager.getLastQueryExecuted();

                    dataExporterBuilder.setFormat("html");
                    dataExporterBuilder.setQuery(sql);

                    exportToFile("html");
                }
            });
        }
    };

    private final Action exportGraphAction = new AbstractAction("Graph") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String sql = databaseManager.getLastQueryExecuted();

                    dataExporterBuilder.setFormat("png");
                    dataExporterBuilder.setQuery(sql);
                    dataExporterBuilder.setMode("cartesian");
                    dataExporterBuilder.setTitle("Titolo Grafico");

                    exportToFile("graph");
                }
            });
        }
    };

    private void fetchData(final String query) {
        PresentationModel pm = createPresentationModelGrid(query);
        presentation.populateGrid(pm);

        int rows = pm.as(Composite.class).findChildren().count();
        setEnabledExportOps(rows > 0);
        if (rows > 0) {
            presentation.notifyDataReturned(String.format("%d rows have been returned from", rows));
        } else {
            presentation.notifyDataReturned("No data has been returned");
        }
    }

    private void exportToFile(final String fileType) {
        final FileWriterOptions options = dataExporterBuilder.build();
        try {
            ExporterFactory exporterFactory = (ExporterFactory) beanFactory.getBean(fileType + "Factory");
            exporterFactory.createExporter(options).run();

            presentation.notifyExporterStatus("The " + fileType + " file has been exported successfully");
        } catch (ExporterTaskException ex) {
            log.error("Something goes wrong with the " + fileType + " writer.", ex);
            presentation.notifyExporterStatus("Some error occurs during the generation of the " + fileType + " file");
        } catch (SQLException | IOException sqle) {
            log.error("", sqle);
        }
    }

    private void rise() {
        presentation.bind(quickQueryAction, selectQueryCategoryAction, exportGraphAction, exportXmlAction, exportExcelAction, exportHtmlAction, goAction);
        presentation.showUp();
        this.setEnabledExportOps(false);
        presentation.populateCategoryCombo(this.createPresentationModelCategoryPack());
    }

    private PresentationModel createPresentationModelQueryPack() {
        PresentationModel pm = presentation.getQueryCategoryCurrentlySelected();

        final List<PresentationModel> pms = new ArrayList<>();
        if (pm != null) {
            PackCategory category = pm.as(PackCategory.class);
            for (PackQuery pq : category.getQueries()) {
                pms.add(new DefaultPresentationModel(null, pq, new DefaultDescribable(pq.getDescription())));
            }
        }

        return new DefaultPresentationModel("", pms);
    }

    private PresentationModel createPresentationModelCategoryPack() {
        final List<PresentationModel> pms = new ArrayList<>();

        try {
            final Document document = packQueryManager.parse(new File("./config/categories.xml"));
            List<PackCategory> categories = packQueryManager.readCategories(document);
            for (PackCategory pcm : categories) {
                pms.add(new DefaultPresentationModel(null, pcm, new DefaultDescribable(pcm.getDescription())));
            }
        } catch (IOException ioe) {
            log.error("The configuration file 'categories.xml' was not found. No data were imported from.", ioe);
        } catch (SAXException saxe) {
            log.error("Seems something goes bad with the XML Reader.", saxe);
        }
        return new DefaultPresentationModel("", pms);
    }

    private PresentationModel createPresentationModelGrid(final String query) {
        final AtomicReference<String[]> columns = new AtomicReference<>();

        SimpleComposite<PresentationModel> composite = new SimpleComposite<PresentationModel>() {

            @Override
            public Finder<PresentationModel> findChildren() {
                return new FinderSupport<PresentationModel, Finder<PresentationModel>>() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public int count() {
                        try {
                            try (PreparedStatement statement = databaseManager.createPreparedStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                                try (ResultSet resultSet = statement.executeQuery()) {
                                    return resultSetManager.resultSetCount(resultSet);
                                }
                            }
                        } catch (SQLException sqle) {
                            log.error("", sqle);
                            return 0;
                        }
                    }

                    @Override
                    protected List<? extends PresentationModel> computeNeededResults() {
                        final List<PresentationModel> pms = new ArrayList<>();

                        try {
                            try (PreparedStatement preparedStatement = databaseManager.createPreparedStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                                @Cleanup
                                ResultSet resultSet = preparedStatement.executeQuery();
                                resultSet.absolute(1);
                                resultSet.relative(-1);

                                final ResultSetMetaData metadata = resultSet.getMetaData();
                                columns.set(resultSetManager.getColumnNames(metadata));

                                final int columnCount = columns.get().length;
                                int todo = count();

                                while (resultSet.next() && (todo-- > 0)) {
                                    PresentationModel[] pma = new PresentationModel[columnCount];
                                    for (int i = 0; i < columnCount; i++) {
                                        String nextLine = resultSetManager.extractStringValue(resultSet, metadata.getColumnType(i + 1), i + 1);
                                        pma[i] = new DefaultPresentationModel(null, new DefaultDisplayable(nextLine));
                                    }
                                    pms.add(new DefaultPresentationModel(null, new Row(pma)));
                                }
                            }
                        } catch (SQLException sqle) {
                            log.error("", sqle);
                        }
                        return pms;
                    }
                };
            }
        };

        final GridHeaderProvider gridHeader = new GridHeaderProvider() {
            @Override
            public List<String> getHeaderLabels() {
                final String[] c = columns.get();
                return (c == null) ? Collections.<String>emptyList() : Arrays.asList(c);
            }
        };
        return new DefaultPresentationModel("", composite, gridHeader);
    }

    private void setEnabledExportOps(boolean enable) {
        exportGraphAction.setEnabled(enable);
        exportExcelAction.setEnabled(enable);
        exportXmlAction.setEnabled(enable);
        exportHtmlAction.setEnabled(enable);
    }
    
    @PostConstruct
    public void initialize() {
        this.eventBus.subscribe(PopulateGridEvent.class, populateGridEventListener);
        this.eventBus.subscribe(RiseMainEvent.class, riseEventListener);
    }

    @PreDestroy
    public void destroy() {
        this.eventBus.unsubscribe(populateGridEventListener);
        this.eventBus.unsubscribe(riseEventListener);
    }
}
