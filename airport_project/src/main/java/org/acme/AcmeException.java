package org.acme;


@SuppressWarnings("serial")
public class AcmeException extends RuntimeException {

    public AcmeException() { }
    
    public AcmeException(String message) {
        super(message);
    }

    public AcmeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcmeException(Throwable cause) {
        super(cause);
    }
}
 