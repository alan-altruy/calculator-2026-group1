package calculator;

/**
 * Exception that will be used when an incorrectly constructed arithmetic expression is encountered.
 */
public class IllegalConstruction extends RuntimeException {

	// This field is required for all Serializable classes, which includes all subclasses of RuntimeException.
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor of the class, with no message or cause.
	 * It is not recommended to use this constructor, since it does not provide any information about
	 * the reason for the exception. It is better to use one of the other constructors that allow to specify a message and/or a cause.
	 * @see #IllegalConstruction(String)
	 * @see #IllegalConstruction(String, Throwable)
	 * @see #IllegalConstruction(Throwable)
	 */
	public IllegalConstruction() {
		super();
	}

	/**
	 * Constructor of the class with a message describing the reason for the exception.
	 * @param message the message describing the reason for the exception
	 */
	public IllegalConstruction(String message) {
		super(message);
	}

	/**
	 * Constructor of the class with a message and a cause.
	 * @param message the message describing the reason for the exception
	 * @param cause the cause of the exception
	 */
	public IllegalConstruction(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor of the class with a cause.
	 * @param cause the cause of the exception
	 */
	public IllegalConstruction(Throwable cause) {
		super(cause);
	}

}
