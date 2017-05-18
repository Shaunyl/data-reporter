package it.shaunyl.datareporter.login;

/**
 *
 * @author Filippo Testino
 */
public interface UrlBuilder {

    public String build(String host, int port, String sid);
    
}
