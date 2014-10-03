
package exercise;

import java.lang.Math;
import java.io.PrintWriter;
import java.lang.IllegalArgumentException;

/**
 * The class builds an amortization schedule 
 *  
 *  Amortization Formula:
 *  This will get you your monthly payment. 
 *  M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
 *  
 *  Where:
 *  P = Principal
 *  I = Interest
 *  J = Monthly Interest in decimal form: I / (12 * 100)
 *  N = Number of months of loan
 *  M = Monthly Payment Amount
 *  
 *  To create the amortization table, loop through following steps:
 *  1. Calculate H = P x J, this is your current monthly interest
 *  2. Calculate C = M - H, this is your monthly payment minus your monthly interest, 
 *     so it is the amount of principal you pay for that month
 *  3. Calculate Q = P - C, this is the new balance of your principal of your loan.
 *  4. Set P equal to Q and go back to Step 1: You thusly loop around 
 *     until the value Q (and hence P) goes to zero.
 * 
 * @author Ashley
 */
public class AmortizationSchedule {
	private static final String AMOUNT_PROMPT = "Please enter the amount you would like to borrow: ";
	private static final String APR_PROMPT = "Please enter the annual percentage rate used to repay the loan: ";
	private static final String TERM_PROMPT = "Please enter the term, in years, over which the loan is repaid: ";
	private static final double[] AMOUNT_RANGE = new double[] { 0.01d, 1000000000000d };
	private static final double[] APR_RANGE = new double[] { 0.000001d, 100d };
	private static final int[] TERM_RANGE = new int[] { 1, 1000000 };
	private static final double MONTHLY_INTEREST_DIVISOR = 12d * 100d;
	
	public static class AmountInDollars extends DoubleInput {
		public AmountInDollars() {
			super(AMOUNT_PROMPT, AMOUNT_RANGE[0], AMOUNT_RANGE[1]);
		}
	}
	
	public static class AprInPercent extends DoubleInput {
		public AprInPercent() {
			super(APR_PROMPT, APR_RANGE[0], APR_RANGE[1]);
		}
	}
	
	public static class TermInYears extends IntegerInput {
		public TermInYears() {
			super(TERM_PROMPT, TERM_RANGE[0], TERM_RANGE[1]);
		}
	}
	
	public AmortizationSchedule() {}
	
	/**
	 * Outputs the amortization schedule.
	 * 
	 * The first column identifies the payment number.
	 * The second column contains the amount of the payment.
	 * The third column shows the amount paid to interest.
	 * The fourth column has the current balance. 
	 * The total payment amount and the interest paid fields.
	 *
	 * @param writer
	 * @param amountInDollars
	 * @param aprInPercent
	 * @param termInYears
	 */
	public void outputAmortizationSchedule(
			PrintWriter writer,
			AmountInDollars amountInDollars,
			AprInPercent aprInPercent,
			TermInYears termInYears) {
		if (!amountInDollars.isValid()) {
			writer.printf("Invalid amount: " + amountInDollars.getHint());
			return;
		}
		if (!aprInPercent.isValid()) {
			writer.printf("Invalid interest rate: " + aprInPercent.getHint());
			return;
		}
		if (!termInYears.isValid()) {
			writer.printf("Invalid term: " + termInYears.getHint());
			return;
		}
		
		long amountBorrowedInCents = Math.round(amountInDollars.get() * 100);
		double apr = aprInPercent.get();
		int initialTermMonths = termInYears.get() * 12;
		
		// calculate J
		// J = Monthly Interest in decimal form: I / (12 * 100)
		double monthlyInterest = apr / MONTHLY_INTEREST_DIVISOR;
		
		// calculate monthly payment in cents
		long monthlyPaymentAmountInCents = calculateMonthlyPayment(
				amountBorrowedInCents, monthlyInterest, initialTermMonths);
		
		// the following shouldn't happen with the available valid ranges
		// for borrow amount, apr, and term; however, without range validation,
		// monthlyPaymentAmount as calculated by calculateMonthlyPayment()
		// may yield incorrect values with extreme input values
		if (monthlyPaymentAmountInCents > amountBorrowedInCents) {
			throw new IllegalArgumentException();
		}
		
		outputAmortizationSchedule(
				writer, 
				amountBorrowedInCents, 
				monthlyInterest,
				initialTermMonths,
				monthlyPaymentAmountInCents);
	}

	private long calculateMonthlyPayment(
			long amountBorrowed,
			double monthlyInterest,
			int initialTermMonths) {
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		//
		// Where:
		// P = Principal
		// I = Interest
		// J = Monthly Interest in decimal form: I / (12 * 100)
		// N = Number of months of loan
		// M = Monthly Payment Amount
		//
		// this is 1 / (1 + J)
		double tmp = Math.pow(1d + monthlyInterest, -1);
		// this is Math.pow(1/(1 + J), N)
		tmp = Math.pow(tmp, initialTermMonths);
		// this is 1 / (1 - (Math.pow(1/(1 + J), N))))
		tmp = Math.pow(1d - tmp, -1);
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		double rc = amountBorrowed * monthlyInterest * tmp;
		return Math.round(rc);
	}

	// The output should include:
	// The first column identifies the payment number.
	// The second column contains the amount of the payment.
	// The third column shows the amount paid to interest.
	// The fourth column has the current balance. The total payment amount and
	// the interest paid fields.
	private void outputAmortizationSchedule(
			PrintWriter writer,
			long amountBorrowedInCents,
			double monthlyInterest,
			int initialTermMonths,
			long monthlyPaymentAmountInCents) {
		//
		// To create the amortization table, follow these steps:
		// 1. Calculate H = P x J, this is your current monthly interest
		// 2. Calculate C = M - H, this is your monthly payment minus your
		//    monthly interest, so it is the amount of principal you pay 		
		//    for that month
		// 3. Calculate Q = P - C, this is the new balance of your principal 
		//    of your loan.
		// 4. Set P equal to Q and go back to Step 1: You thusly loop around
		//    until the value Q (and hence P) goes to zero.
		//
		String formatString = "%1$-20s%2$-20s%3$-20s%4$s,%5$s,%6$s\n";
		writer.printf(formatString, "PaymentNumber", "PaymentAmount",
				"PaymentInterest", "CurrentBalance", "TotalPayments",
				"TotalInterestPaid");
		long balance = amountBorrowedInCents;
		int paymentNumber = 0;
		long totalPayments = 0;
		long totalInterestPaid = 0;
		// output is in dollars
		formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$.2f,%5$.2f,%6$.2f\n";
		writer.printf(formatString, paymentNumber++, 0d, 0d,
				((double) amountBorrowedInCents) / 100d,
				((double) totalPayments) / 100d,
				((double) totalInterestPaid) / 100d);
		final int maxNumberOfPayments = initialTermMonths + 1;
		while ((balance > 0) && (paymentNumber <= maxNumberOfPayments)) {
			// Calculate H = P x J, this is your current monthly interest
			long curMonthlyInterest = Math.round(((double) balance)
					* monthlyInterest);// the amount required to payoff the loan
			long curPayoffAmount = balance + curMonthlyInterest;
			// the amount to payoff the remaining balance may be less than the
			// calculated monthlyPaymentAmount
			long curMonthlyPaymentAmount = Math.min(monthlyPaymentAmountInCents,
					curPayoffAmount);
			// it's possible that the calculated monthlyPaymentAmount is 0,
			// or the monthly payment only covers the interest payment - i.e. no
			// principal
			// so the last payment needs to payoff the loan
			if ((paymentNumber == maxNumberOfPayments)
					&& ((curMonthlyPaymentAmount == 0) || (curMonthlyPaymentAmount == curMonthlyInterest))) {
				curMonthlyPaymentAmount = curPayoffAmount;
			}
			// Calculate C = M - H, this is your monthly payment minus your
			// monthly interest,
			// so it is the amount of principal you pay for that month
			long curMonthlyPrincipalPaid = curMonthlyPaymentAmount
					- curMonthlyInterest;
			// Calculate Q = P - C, this is the new balance of your principal of
			// your loan.
			long curBalance = balance - curMonthlyPrincipalPaid;
			totalPayments += curMonthlyPaymentAmount;
			totalInterestPaid += curMonthlyInterest;
			// output is in dollars
			writer.printf(formatString, paymentNumber++,
					((double) curMonthlyPaymentAmount) / 100d,
					((double) curMonthlyInterest) / 100d,
					((double) curBalance) / 100d,
					((double) totalPayments) / 100d,
					((double) totalInterestPaid) / 100d);
			// Set P equal to Q and go back to Step 1: You thusly loop around
			// until the value Q (and hence P) goes to zero.
			balance = curBalance;
		}
	}
}