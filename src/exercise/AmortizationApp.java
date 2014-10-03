package exercise;

import java.io.IOException;

import exercise.AmortizationSchedule.AmountInDollars;
import exercise.AmortizationSchedule.AprInPercent;
import exercise.AmortizationSchedule.TermInYears;

/**
 * Main class that can be run to start the application which 
 * calculates amortized monthly payments.
 * 
 * It prompts the user for:
 * the amount he or she is borrowing,
 * the annual percentage rate used to repay the loan,
 * the term, in years, over which the loan is repaid. 
 * 
 * @author Ashley 
 *
 */
public class AmortizationApp {
	
	public static void main(String[] args) {
		AmountInDollars amountInDollars = new AmountInDollars();
		AprInPercent aprInPercent = new AprInPercent();
		TermInYears termInYears = new TermInYears();
		
		Input[] inputs = new Input[] { amountInDollars, aprInPercent, termInYears };
		InputOutput io = new InputOutput(System.console());
		try {
			for (int i = 0; i < inputs.length;) {
				String line = io.readLine(inputs[i].getUserPrompt());
				if (!inputs[i].parse(line)) {
					io.print(inputs[i].getHint());
				} else {
					i++;    
				}
			}
			AmortizationSchedule as = new AmortizationSchedule();
			as.outputAmortizationSchedule(io.writer(), amountInDollars, aprInPercent, termInYears);
			io.flush();
		} catch (IOException e) {
			io.print("An IOException was encountered. Terminating program.\n");
 		} catch (IllegalArgumentException e) {
			io.print("Unable to process the values entered. Terminating program.\n");
		}
	}
}