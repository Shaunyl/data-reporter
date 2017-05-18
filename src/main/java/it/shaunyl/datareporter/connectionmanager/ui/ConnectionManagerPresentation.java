package it.shaunyl.datareporter.connectionmanager.ui;

import it.shaunyl.datareporter.connectionmanager.ConnectionModel;
import java.util.List;
import javax.swing.Action;

/**
 *
 * @author Filippo
 */
 public interface ConnectionManagerPresentation {

     void bind(Action saveConnectionAction, Action closeDialogAction, Action selectItemAction, Action removeConnectionAction);
    
     void showUp();
    
     void addAllConnectionsToList(List<String> connections);
    
     void dismiss();

     String getHost();

     String getSID();

     String getPassword();

     String getUsername();

     int getPort();

     String getConnectionName();
    
     void notifyStatus(String text);

     void addConnectionToList(String connectionName);

     void loadConnectionFieldsIntoTextFields(ConnectionModel connectionModel);

     String getConnectionCurrentlySelected();

     void removeConnectionFromList(String connectionName);
}
