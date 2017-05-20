package it.shaunyl.datareporter;

import it.shaunyl.datareporter.events.ConnectionManagerEvent;
import it.shaunyl.datareporter.events.LoginEvent;
import it.shaunyl.datareporter.events.QuickQueryEvent;
import it.shaunyl.datareporter.events.RiseConnectionManagerEvent;
import it.shaunyl.datareporter.events.RiseMainEvent;
import it.shaunyl.datareporter.events.RiseQuickQueryEvent;
import it.shaunyl.datareporter.events.CManStatusChangedEvent;
import it.shaunyl.datareporter.events.UpdateConnectionsEvent;
import it.shaunyl.eventbus.EventBus;
import it.shaunyl.eventbus.EventBusListener;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class EventRouter {
    @Inject
    private EventBus eventBus;

    private final EventBusListener<LoginEvent> loginEventListener = new EventBusListener<LoginEvent>() {
        @Override
        public void notify(final LoginEvent event) {
            eventBus.publish(new RiseMainEvent());
        }
    };

    private final EventBusListener<ConnectionManagerEvent> connectionManagerEventListener = new EventBusListener<ConnectionManagerEvent>() {
        @Override
        public void notify(final ConnectionManagerEvent event) {
            eventBus.publish(new RiseConnectionManagerEvent());
        }
    };     
    
    private final EventBusListener<QuickQueryEvent> quickQueryEventListener = new EventBusListener<QuickQueryEvent>() {
        @Override
        public void notify(final QuickQueryEvent event) {
            eventBus.publish(new RiseQuickQueryEvent());
        }
    };   
    
    private final EventBusListener<CManStatusChangedEvent> updateConnectionsEventListener = new EventBusListener<CManStatusChangedEvent>() {
        @Override
        public void notify(final CManStatusChangedEvent event) {
            eventBus.publish(new UpdateConnectionsEvent());
        }
    };
    
    @PostConstruct
    public void initialize () {
        eventBus.subscribe(LoginEvent.class, loginEventListener);
        eventBus.subscribe(ConnectionManagerEvent.class, connectionManagerEventListener);
        eventBus.subscribe(QuickQueryEvent.class, quickQueryEventListener);
        eventBus.subscribe(CManStatusChangedEvent.class, updateConnectionsEventListener);
    }
    
    @PreDestroy
    public void destroy () {
        eventBus.unsubscribe(loginEventListener);
        eventBus.unsubscribe(connectionManagerEventListener);
        eventBus.subscribe(QuickQueryEvent.class, quickQueryEventListener);
        eventBus.subscribe(CManStatusChangedEvent.class, updateConnectionsEventListener);
    }
}
