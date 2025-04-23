/**
 * Custom exception class for handling invalid grade values.
 * This demonstrates how to create a custom exception in Java.
 */
public class InvalidGradeException extends Exception {
    /**
     * Constructor that accepts an error message
     * @param message The detailed message explaining why the exception occurred
     */
    public InvalidGradeException(String message) {
        super(message);
    }
}