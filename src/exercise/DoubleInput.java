package exercise;

import java.util.Optional;

/**
 * Class that implements Double {@code Input}
 *
 */
public class DoubleInput implements Input<Double> {

	private final String userPrompt;
	private final double min;
	private final double max;
	private Optional<Double> input;
	
	public DoubleInput(String userPrompt, double min, double max) {
		this.userPrompt = userPrompt;
		this.min = min;
		this.max = max;
		this.input = Optional.<Double>empty();
	}
	
	@Override
	public Double get() {
		return input.get();
	}
	
	@Override
	public String getUserPrompt() {
		return userPrompt;
	}

	@Override
	public boolean isValid() {
		if (input.isPresent()) {
			double value = input.get();
			return (value >= min && value <= max);
		} else {
			return false;
		}
	}

	@Override
	public boolean parse(String line) {
		try {
			double value = Double.parseDouble(line);
			input = Optional.<Double>of(value);
			return isValid();
		} catch (NumberFormatException e) {
			// Log exception
			return false;
		}
	}

	@Override
	public String getHint() {
		return "Please enter a positive value between " + min + " and " + max + ".\n";
	}
}
