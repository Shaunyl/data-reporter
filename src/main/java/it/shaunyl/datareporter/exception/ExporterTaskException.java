/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exception;

/**
 *
 * @author Filippo Testino
 */
public class ExporterTaskException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public ExporterTaskException() {   
    }
    
    public ExporterTaskException(String message) {
        super(message);
    }
    
    public ExporterTaskException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ExporterTaskException(Throwable cause) {
        super(cause);
    }
}
