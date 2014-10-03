package exercise;

import java.util.Optional;

/**
 * Class that implements Integer {@code Input}
 * 
 */
public class IntegerInput implements Input<Integer> {

	private final String userPrompt;
	private final int min;
	private final int max;
	private Optional<Integer> input;
	
	public IntegerInput(String userPrompt, int min, int max) {
		this.userPrompt = userPrompt;
		this.min = min;
		this.max = max;
		this.input = Optional.<Integer>empty();
	}
	
	@Override
	public Integer get() {
		return input.get();
	}
	
	@Override
	public String getUserPrompt() {
		return userPrompt;
	}

	@Override
	public boolean isValid() {
		if (input.isPresent()) {
			int value = input.get();
			return (value >= min && value <= max);
		} else {
			return false;
		}
	}

	@Override
	public boolean parse(String line) {
		try {
			int value = Integer.parseInt(line);
			input = Optional.<Integer>of(value);
			return isValid();
		} catch (NumberFormatException e) {
			// Log exception
			return false;
		}
	}

	@Override
	public String getHint() {
		return "Please enter a positive integer value between " + min + " and " + max + ".\n";
	}
}
