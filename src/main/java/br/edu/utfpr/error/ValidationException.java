package br.edu.utfpr.error;

/**
 * Created by ronifabio on 07/05/2019.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
