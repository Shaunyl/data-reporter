package it.shaunyl.datareporter.connectionmanager.spi;

import it.shaunyl.datareporter.connectionmanager.ConnectionModel;
import it.shaunyl.datareporter.connectionmanager.DerbyManager;
import it.shaunyl.datareporter.connectionmanager.ui.ConnectionManagerPresentation;
import it.shaunyl.datareporter.events.RiseConnectionManagerEvent;
import it.shaunyl.datareporter.events.CManStatusChangedEvent;
import it.shaunyl.eventbus.EventBus;
import it.shaunyl.eventbus.EventBusListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Slf4j @Service
public class DefaultConnectionManagerPresentationControl {

    @Inject
    private ConnectionManagerPresentation presentation;
    
    @Inject
    private DerbyManager connectionsManager;
    
    @Inject
    private EventBus eventBus;
    
    private EventBusListener<RiseConnectionManagerEvent> riseCManEventListener = new EventBusListener<RiseConnectionManagerEvent>() {
        @Override
        public void notify(final RiseConnectionManagerEvent event) {
            manageConnection();
        }
    };

    private Action saveConnectionAction = new AbstractAction("Save") {
        private static final long serialVersionUID = 1L;
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            String name = presentation.getConnectionName();

            String host = presentation.getHost();
            String sid = presentation.getSID();
            int port = presentation.getPort();
            String username = presentation.getUsername();
            String pwd = presentation.getPassword();

            ConnectionModel connection = new ConnectionModel(name, host, sid, port, username, pwd);
            if (connection.isNotValid()) {
                presentation.notifyStatus("Something seems bad with input data");
                log.error("");
                return;
            }

            try {
                boolean isExisting = connectionsManager.find(name);
                if (!isExisting) {
                    connectionsManager.insert(connection);
                    presentation.addConnectionToList(connection.getName());
                } else {
                    connectionsManager.update(connection);
                }
            } catch (SQLException sqle) {
                presentation.notifyStatus("The transaction has been rolled back");
                log.error("", sqle);
                return;
            }

            presentation.notifyStatus("The transaction has been committed successfully");
        }
    };

    private final Action closeDialogAction = new AbstractAction("close") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            eventBus.publish(new CManStatusChangedEvent());
            presentation.dismiss();
        }
    };
    
    private final Action selectItemAction = new AbstractAction("select") {
        private static final long serialVersionUID = 1L;
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            String connectionName = presentation.getConnectionCurrentlySelected();
            try {
                ConnectionModel connectionModel = connectionsManager.read(connectionName);
                if (connectionModel == null) {
                    return;
                }
                presentation.loadConnectionFieldsIntoTextFields(connectionModel);
            } catch (SQLException se) {
                log.error("", se);
            }
        }
    };
    
    private final Action removeConnectionAction = new AbstractAction("Remove", new ImageIcon(getClass().getResource("RemoveConnection.png"))) {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            
            String connectionName = presentation.getConnectionCurrentlySelected();
            try {
                connectionsManager.delete(connectionName);
                presentation.removeConnectionFromList(connectionName);
                presentation.notifyStatus("The transaction has been committed successfully");
            } catch (SQLException se) {
                presentation.notifyStatus("The transaction has been rolled back");
                log.error("", se);
            }
        }
    }; 
    
    private void manageConnection() {
        presentation.bind(saveConnectionAction, closeDialogAction, selectItemAction, removeConnectionAction);
        presentation.showUp();
        
        List<String> connNames = new ArrayList<>();
        try {
            List<ConnectionModel> connections = connectionsManager.read();
            for (ConnectionModel connection : connections) {
                connNames.add(connection.getName());
            }
        } catch (SQLException se) {
            log.error("", se);
        }

        presentation.addAllConnectionsToList(connNames);
    }
    
    @PostConstruct
    public void initialize() {
        this.eventBus.subscribe(RiseConnectionManagerEvent.class, riseCManEventListener);
    }

    @PreDestroy
    public void destroy() {
        this.eventBus.unsubscribe(riseCManEventListener);
    }   
}