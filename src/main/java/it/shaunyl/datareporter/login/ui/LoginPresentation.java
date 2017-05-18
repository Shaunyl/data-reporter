package it.shaunyl.datareporter.login.ui;

import java.util.List;
import javax.swing.Action;

/**
 *
 * @author Filippo Testino
 */
public interface LoginPresentation {

    void bind(final Action connectAction, final Action newConnectionAction, final Action selectConnectionAction);

    void showUp();
    
    void addAllConnectionsToCombo(final List<String> connNames);

    void setPassword(final String pwd);

    void dismiss();
    
    String getPassword();

    void notifyFailedLogin(final String text);

    String getUsername();

    String getConnectionName();
}
