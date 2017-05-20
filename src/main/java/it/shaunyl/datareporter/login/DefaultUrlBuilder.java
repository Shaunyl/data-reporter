package it.shaunyl.datareporter.login;

import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class DefaultUrlBuilder implements UrlBuilder {

    @Override
    public String build(String host, int port, String sid) {
        return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, sid);
    }
    
}
