package it.shaunyl.datareporter.connectionmanager;

import lombok.*;
import javax.validation.constraints.*;

/**
 *
 * @author Filippo
 */
@AllArgsConstructor
public class ConnectionModel {
    @Getter @Setter @NotNull @Size(min = 1)
    private String name, host, sid;
    @Getter @Setter @Min(1) @Max(65536)
    private int port;
    @Getter @Setter @NotNull @Size(min = 1)
    private String username;
    @Getter @Setter
    private String password;

    public final boolean isNotValid() {
        return (name.isEmpty() || host.isEmpty() || sid.isEmpty() || username.isEmpty() || (port > 65536 || port <= 1024));
    }
}