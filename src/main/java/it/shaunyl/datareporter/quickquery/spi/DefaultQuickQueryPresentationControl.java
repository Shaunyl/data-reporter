package it.shaunyl.datareporter.quickquery.spi;

import it.shaunyl.datareporter.events.PopulateGridEvent;
import it.shaunyl.datareporter.events.RiseQuickQueryEvent;
import it.shaunyl.datareporter.quickquery.KeywordHighlighter;
import it.shaunyl.datareporter.quickquery.ui.QuickQueryPresentation;
import it.shaunyl.eventbus.EventBus;
import it.shaunyl.eventbus.EventBusListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Slf4j @Service
public class DefaultQuickQueryPresentationControl {

    @Inject
    private QuickQueryPresentation presentation;
        
    @Inject
    private EventBus eventBus;
    
    @Inject
    private ExecutorService executorService;    
    
    private final EventBusListener<RiseQuickQueryEvent> riseQuickQueryEventListener = new EventBusListener<RiseQuickQueryEvent>() {
        @Override
        public void notify(final RiseQuickQueryEvent event) {
            executeQuickQuery();
        }
    };
    
    private final Action executeQueryAction = new AbstractAction("Execute") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final String sql = presentation.getSQL().trim().replaceFirst(";", "");

            try {
                presentation.dismiss();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.publish(new PopulateGridEvent(sql));
                    }
                });
            } 
            catch (OutOfMemoryError ome) {
                presentation.notifyFailedQueryExecution("No enough memory for storing the result set.");
                log.error("", ome);
            }
        }
    };
    
    private final Action highlightQueryAction = new AbstractAction("Highlight") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            presentation.clearQueryColors();

            Pattern pattern = Pattern.compile(KeywordHighlighter.KEYWORDS_REGEX);
            String text = presentation.getSQL();
            Matcher match = pattern.matcher(text.toLowerCase());
            while (match.find()) {
                presentation.updateQueryColor(match.start(), match.end() - match.start());
            }
        }
    };
    
    @PostConstruct
    public void initialize() {
        this.eventBus.subscribe(RiseQuickQueryEvent.class, riseQuickQueryEventListener);
    }

    @PreDestroy
    public void destroy() {
        this.eventBus.unsubscribe(riseQuickQueryEventListener);
    } 

    public void executeQuickQuery() {
        presentation.bind(executeQueryAction, highlightQueryAction);
        presentation.showUp();
    }
}
