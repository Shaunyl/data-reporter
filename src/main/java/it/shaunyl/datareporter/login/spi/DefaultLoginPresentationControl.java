package it.shaunyl.datareporter.login.spi;

import it.shaunyl.datareporter.connectionmanager.ConnectionModel;
import it.shaunyl.datareporter.connectionmanager.DerbyManager;
import it.shaunyl.datareporter.events.ConnectionManagerEvent;
import it.shaunyl.datareporter.events.LoginEvent;
import it.shaunyl.datareporter.events.UpdateConnectionsEvent;
import it.shaunyl.datareporter.login.ConnectionManager;
import it.shaunyl.datareporter.login.UrlBuilder;
import it.shaunyl.datareporter.login.ui.LoginPresentation;
import it.shaunyl.datareporter.login.ui.LoginPresentationControl;
import it.shaunyl.eventbus.EventBus;
import it.shaunyl.eventbus.EventBusListener;
import it.tidalwave.role.ui.UserAction;
import it.tidalwave.role.ui.spi.UserActionSupport;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Slf4j @Service
public class DefaultLoginPresentationControl implements LoginPresentationControl {

    @Inject //@Qualifier(value = "javafx-login-presentation")
    private LoginPresentation presentation;

    @Inject
    private ConnectionManager connectionManager;

    @Inject
    private DerbyManager derbyManager;

    @Inject
    private UrlBuilder urlBuilder;

    @Inject
    private EventBus eventBus;

    @Inject
    private ExecutorService executorService;

    public DefaultLoginPresentationControl() {
    }

    private final EventBusListener<UpdateConnectionsEvent> updateConnectionsEvent = new EventBusListener<UpdateConnectionsEvent>() {
        @Override
        public void notify(final UpdateConnectionsEvent event) {
            refreshConnections();
        }
    };

    private final UserAction userNewAction = new UserActionSupport() {
        @Override
        public void actionPerformed() {
            eventBus.publish(new ConnectionManagerEvent());
        }
    };

    private final UserAction userSelectAction = new UserActionSupport() {
        @Override
        public void actionPerformed() {
            String connectionName = presentation.getConnectionName();
            try {
                ConnectionModel connectionModel = derbyManager.read(connectionName);
                presentation.setPassword(connectionModel.getPassword());
            } catch (SQLException sqle) {
                log.error("", sqle);
            }
        }
    };
    
    private final UserAction userConnectAction = new UserActionSupport() {
        @Override
        public void actionPerformed() {
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    String connName = presentation.getConnectionName();
                    try {
                        presentation.notifyFailedLogin("Connecting...");
                        ConnectionModel connectionModel = derbyManager.read(connName);
                        String host = connectionModel.getHost();
                        int port = connectionModel.getPort();
                        String sid = connectionModel.getSid();
                        String url = urlBuilder.build(host, port, sid);

                        String username = connectionModel.getUsername();
                        String pwd = presentation.getPassword();

                        connectionManager.connect(url, username, pwd);
                        log.info("connecting to {} as {}", url, username);
                    } catch (SQLException sqle) {
                        presentation.notifyFailedLogin("Cannot be established a connection to the database");
                        log.error("Cannot be established a connection to the database.", sqle);
                        return;
                    }
                    log.info("starting application...");
                    presentation.dismiss();
                    eventBus.publish(new LoginEvent());
                }
            });
        }
    };

    @Override
    public void login() {
        presentation.bind(userConnectAction, userNewAction, userSelectAction);
        presentation.showUp();
        this.refreshConnections();
    }

    @Override
    public void refreshConnections() {
        List<String> connNames = new ArrayList<>();
        try {
            List<ConnectionModel> connections = derbyManager.read();
            for (ConnectionModel connection : connections) {
                connNames.add(connection.getName());
            }
        } catch (SQLException sqle) {
            log.error("", sqle);
        }

        presentation.addAllConnectionsToCombo(connNames);
        if (connNames.isEmpty()) {
            presentation.setPassword("");
        }
    }

    @PostConstruct
    public void initialize() {
	  log.debug("Spring Bean " + this.getClass().getSimpleName() + " initialized.");
          this.eventBus.subscribe(UpdateConnectionsEvent.class, updateConnectionsEvent);
    }

    @PreDestroy
    public void destroy() {
        log.debug("Spring container has been destroyed.");
        this.eventBus.unsubscribe(updateConnectionsEvent);
    }
}
