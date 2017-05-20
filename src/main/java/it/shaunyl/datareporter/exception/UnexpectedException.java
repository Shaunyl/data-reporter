/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exception;

/**
 * 
 * @author Filippo Testino (filippo.testino@gmail.com)
 */
public class UnexpectedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedException(Throwable cause) {
        super(cause);
    }
}
