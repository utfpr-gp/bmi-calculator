package br.edu.utfpr.error;

/**
 * Created by ronifabio on 07/05/2019.
 */
public class ParamException extends RuntimeException {

    public ParamException() {
        super("Algum parâmetro é inválido.");
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
