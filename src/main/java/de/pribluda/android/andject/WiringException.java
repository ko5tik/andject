package de.pribluda.android.andject;

/**
 * signals problems with wiring
 * @author Konstantin Pribluda - konstantin@pribluda.de
 *
 */
public class WiringException extends RuntimeException {
    public WiringException() {
    }

    public WiringException(Throwable cause) {
        super(cause);
    }

    public WiringException(String message) {
        super(message);
    }

    public WiringException(String message, Throwable cause) {
        super(message, cause);
    }
}
