package exercise;

/**
 * Interface to get a value of type T input.
 * 
 * @param <T>
 */
public interface Input<T> {
	/**
	 * String to prompt user for the input
	 */
	String getUserPrompt();
	
	/**
	 * @return true if the input value T is present and valid
	 */
	boolean isValid();
	
	/**
	 * Parse input value from a string
	 * 
	 * @param line
	 * @return true if line is a valid input
	 */
	boolean parse(String line);

	/**
	 * Hint for a valid input.
	 */
	String getHint();
	
	/**
	 * @return value of input.
	 */
	T get();
	
	// void set(T value);
}
