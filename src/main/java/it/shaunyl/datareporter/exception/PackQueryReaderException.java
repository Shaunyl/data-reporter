/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.shaunyl.datareporter.exception;

/**
 *
 * @author Filippo Testino
 */
public class PackQueryReaderException extends Exception {

    private static final long serialVersionUID = 1L;

    public PackQueryReaderException() {
    }

    public PackQueryReaderException(final String message) {
        super(message);
    }

    public PackQueryReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PackQueryReaderException(final Throwable cause) {
        super(cause);
    }
}
