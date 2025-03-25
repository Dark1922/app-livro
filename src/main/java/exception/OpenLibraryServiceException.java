package exception;

public class OpenLibraryServiceException  extends Exception {

    public OpenLibraryServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenLibraryServiceException(String message) {
        super(message);
    }
}
