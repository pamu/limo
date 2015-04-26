package utils;

import java.math.BigDecimal;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;

public class Payment {

	private String cardName;
	private String cardNumber;
	private String cardMonth;
	private String cardYear;

	public Payment(String cardName, String cardNumber, String cardMonth,
			String cardYear) {

		this.cardName = cardName;
		this.cardNumber = cardNumber;
		this.cardMonth = cardMonth;
		this.cardYear = cardYear;

	}

	public boolean makePayment() {
		BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX,
				Constants.Payment_Merchant_Id, Constants.Payment_Public_Id,
				Constants.Payment_Private_Id);

		TransactionRequest request = new TransactionRequest()
				.amount(new BigDecimal("1000.00")).creditCard()
				.number(cardNumber).expirationMonth(cardMonth)
				.expirationYear(cardYear).done();

		Result<Transaction> result = gateway.transaction().sale(request);

		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();
			System.out.println("Success!: " + transaction.getId());
		} else if (result.getTransaction() != null) {
			System.out.println("Message: " + result.getMessage());
			Transaction transaction = result.getTransaction();
			System.out.println("Error processing transaction:");
			System.out.println("  Status: " + transaction.getStatus());
			System.out.println("  Code: "
					+ transaction.getProcessorResponseCode());
			System.out.println("  Text: "
					+ transaction.getProcessorResponseText());
		} else {
			System.out.println("Message: " + result.getMessage());
			for (ValidationError error : result.getErrors()
					.getAllDeepValidationErrors()) {
				System.out.println("Attribute: " + error.getAttribute());
				System.out.println("  Code: " + error.getCode());
				System.out.println("  Message: " + error.getMessage());
			}
		}
		return result.isSuccess();
	}
}