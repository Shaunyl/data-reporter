package it.shaunyl.datareporter.login.ui;

import it.tidalwave.role.ui.UserAction;
import java.util.List;
import lombok.NonNull;

/**
 *
 * @author Filippo Testino
 */
public interface LoginPresentation {    
    void bind(final @NonNull UserAction userConnectAction, final @NonNull UserAction userNewAction, final @NonNull UserAction userSelectAction);
    
    void showUp();
    
    void addAllConnectionsToCombo(final List<String> connNames);

    void setPassword(final String pwd);

    void dismiss();
    
    String getPassword();

    void notifyFailedLogin(final String text);

    String getConnectionName();
}
