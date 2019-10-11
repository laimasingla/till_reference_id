package com.cg.ibs.cardmanagement.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cg.ibs.cardmanagement.bean.CaseIdBean;
import com.cg.ibs.cardmanagement.bean.CreditCardBean;
import com.cg.ibs.cardmanagement.bean.CreditCardTransaction;
import com.cg.ibs.cardmanagement.bean.CustomerBean;
import com.cg.ibs.cardmanagement.bean.DebitCardBean;
import com.cg.ibs.cardmanagement.bean.DebitCardTransaction;
import com.cg.ibs.cardmanagement.dao.BankDao;
import com.cg.ibs.cardmanagement.dao.CustomerDao;
import com.cg.ibs.cardmanagement.exceptionhandling.IBSException;
import com.cg.ibs.cardmanagement.dao.CardManagementDaoImpl;

public class CustomerServiceImpl implements CustomerService {

	public void getCardLength(BigInteger card) throws IBSException {
		int length = card.toString().length();
		if (length != 16)
			throw new IBSException("Incorrect Length of pin ");

	}

	CustomerDao customerDao;

	public CustomerServiceImpl() {
		customerDao = new CardManagementDaoImpl();

	}

	CustomerBean customObj = new CustomerBean();

	String UCI = "7894561239632587";

	String caseIdGenOne = " ";
	static String caseIdTotal = " ";
	static int caseIdGenTwo = 0;
	LocalDateTime timestamp;
	LocalDateTime fromDate;
	LocalDateTime toDate;
	static String setCardType = null;
	static String uniqueID = null;
	static String customerReferenceID = null;

	String addToQueryTable(String caseIdGenOne) {
		caseIdTotal = caseIdGenOne + caseIdGenTwo;
		caseIdGenTwo++;
		return caseIdTotal;
	}

	@Override
	public String applyNewDebitCard(BigInteger accountNumber, String newCardType) throws IBSException {

		CaseIdBean caseIdObj = new CaseIdBean();

		caseIdGenOne = "ANDC";

		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + accountNumber);
		timestamp = LocalDateTime.now();
		caseIdObj.setDefineQuery(newCardType);
		caseIdObj.setAccountNumber(accountNumber);
		caseIdObj.setCardNumber("NA");
		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		customerDao.newDebitCard(caseIdObj, accountNumber);
		return customerReferenceID;

	}

	public String getNewCardtype(int newCardType) throws IBSException {
		String cardType = newCardType + "";
		Pattern pattern = Pattern.compile("[123]");
		Matcher matcher = pattern.matcher(cardType);
		if (!(matcher.find() && matcher.group().equals(cardType)))
			throw new IBSException("Not a valid input");

		switch (newCardType) {
		case 1:

			setCardType = "Platinum";
			break;
		case 2:
			setCardType = "Gold";
			break;
		case 3:
			setCardType = "Silver";
			break;

		}
		return setCardType;
	}

	public static String getSetCardType() {
		return setCardType;
	}

	public static void setSetCardType(String setCardType) {
		CustomerServiceImpl.setCardType = setCardType;
	}

	@Override
	public String applyNewCreditCard(String newcardType) {
		CaseIdBean caseIdObj = new CaseIdBean();
		caseIdGenOne = "ANCC";
		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + UCI.toString().substring(6));
		timestamp = LocalDateTime.now();
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		caseIdObj.setDefineQuery(newcardType);
		customerDao.newCreditCard(caseIdObj);
		return customerReferenceID;
	}

	@Override
	public String requestDebitCardLost(BigInteger debitCardNumber) throws IBSException {
		caseIdGenOne = "RDCL";
		CaseIdBean caseIdObj = new CaseIdBean();
		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal.concat(debitCardNumber.toString().substring(6)));

		timestamp = LocalDateTime.now();

		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		caseIdObj.setDefineQuery("Debit card Number" + debitCardNumber);
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		customerDao.requestDebitCardLost(caseIdObj, debitCardNumber);
		return ("Ticket Raised successfully . Your reference Id is " + customerReferenceID);

	}

	@Override
	public String requestCreditCardLost(BigInteger creditCardNumber) throws IBSException {

		CaseIdBean caseIdObj = new CaseIdBean();

		caseIdGenOne = "RCCL";
		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + creditCardNumber.toString().substring(6));
		timestamp = LocalDateTime.now();

		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		caseIdObj.setDefineQuery("Credit card number" + creditCardNumber);
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		customerDao.requestCreditCardLost(caseIdObj, creditCardNumber);
		return ("Ticket Raised successfully . Your reference Id is " + customerReferenceID);
	}

	public String raiseDebitMismatchTicket(String transactionId) throws IBSException {

		CaseIdBean caseIdObj = new CaseIdBean();
		caseIdGenOne = "RDMT";

		timestamp = LocalDateTime.now();
		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + transactionId);
		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		caseIdObj.setDefineQuery("Transaction ID" + transactionId);
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		customerDao.raiseDebitMismatchTicket(caseIdObj, transactionId);
		return ("Ticket Raised successfully . Your reference Id is " + customerReferenceID);

	}

	public List<DebitCardBean> viewAllDebitCards() {

		return customerDao.viewAllDebitCards();
	}

	@Override
	public List<CreditCardBean> viewAllCreditCards() {

		return customerDao.viewAllCreditCards();

	}

	public String verifyDebitcardType(BigInteger debitCardNumber) throws IBSException {

		boolean check = customerDao.verifyDebitCardNumber(debitCardNumber);
		if (check) {
			String type = customerDao.getdebitCardType(debitCardNumber);
			return type;
		} else {

			throw new NullPointerException("Debit card not found");
		}
	}

	@Override
	public String requestDebitCardUpgrade(BigInteger debitCardNumber, String myChoice) {

		CaseIdBean caseIdObj = new CaseIdBean();

		caseIdGenOne = "RDCU";
		timestamp = LocalDateTime.now();
		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + debitCardNumber.toString().substring(6));
		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		caseIdObj.setUCI(UCI);

		caseIdObj.setDefineQuery("Upgrade to " + myChoice + " for card Number :" + debitCardNumber);

		customerDao.requestDebitCardUpgrade(caseIdObj, debitCardNumber);
		return ("Ticket Raised successfully . Your reference Id is " + customerReferenceID);

	}

	public boolean verifyDebitCardNumber(BigInteger debitCardNumber) throws IBSException {
		String debitCardNum = debitCardNumber.toString();
		Pattern pattern = Pattern.compile("[0-9]{16}");
		Matcher matcher = pattern.matcher(debitCardNum);
		if (!(matcher.find() && matcher.group().equals(debitCardNum)))
			throw new IBSException("Incorrect  length");
		boolean check = customerDao.verifyDebitCardNumber(debitCardNumber);
		if (!check)
			throw new IBSException(" Debit Card Number does not exist");
		return (check);

	}

	public boolean verifyAccountNumber(BigInteger accountNumber) throws IBSException {
		String accountNum = accountNumber.toString();
		Pattern pattern = Pattern.compile("[0-9]{10}");
		Matcher matcher = pattern.matcher(accountNum);
		if (!(matcher.find() && matcher.group().equals(accountNum)))
			throw new IBSException("Incorrect  length");
		boolean check = customerDao.verifyAccountNumber(accountNumber);
		if (!check)
			throw new IBSException(" Account Number does not exist");
		return (check);
	}

	public boolean verifyDebitPin(int pin, BigInteger debitCardNumber) {
		if (pin == customerDao.getDebitCardPin(debitCardNumber)) {

			return true;
		} else {
			return false;
		}
	}

	public String resetDebitPin(BigInteger debitCardNumber, int newPin) {

		customerDao.setNewDebitPin(debitCardNumber, newPin);
		return ("PIN UPDATED SUCCESSFULLY!");
	}

	public boolean verifyCreditCardNumber(BigInteger creditCardNumber) throws IBSException {
		String creditCardNum = creditCardNumber.toString();
		Pattern pattern = Pattern.compile("[0-9]{16}");
		Matcher matcher = pattern.matcher(creditCardNum);
		if (!(matcher.find() && matcher.group().equals(creditCardNum)))
			throw new IBSException("Incorrect  length");
		boolean check1 = customerDao.verifyCreditCardNumber(creditCardNumber);
		if (!check1)
			throw new IBSException(" Credit Card Number does not exist");
		return (check1);
	}

	public boolean verifyCreditPin(int pin, BigInteger creditCardNumber) {

		if (pin == customerDao.getCreditCardPin(creditCardNumber)) {

			return true;
		} else {
			return false;
		}
	}

	public String resetCreditPin(BigInteger creditCardNumber, int newPin) {

		customerDao.setNewCreditPin(creditCardNumber, newPin);
		return ("PIN UPDATED SUCCESSFULLY!");
	}

	@Override
	public String requestCreditCardUpgrade(BigInteger creditCardNumber, int myChoice) {
		CaseIdBean caseIdObj = new CaseIdBean();
		caseIdGenOne = "RCCU";
		timestamp = LocalDateTime.now();

		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + creditCardNumber.toString().substring(6));
		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		if (myChoice == 1) {
			caseIdObj.setDefineQuery("Upgrade to Gold for credit card number:" + creditCardNumber);

			customerDao.requestCreditCardUpgrade(caseIdObj, creditCardNumber);
			return (" Successful Application for Upgradation to GOLD . Your Reference ID is :  " + customerReferenceID);
		} else if (myChoice == 2) {
			caseIdObj.setDefineQuery("Upgrade to Platinum for credit card number:" + creditCardNumber);
			customerDao.requestDebitCardUpgrade(caseIdObj, creditCardNumber);
			return (" Successful Application for Upgradation to PLATINUM . Your Reference ID is :  "
					+ customerReferenceID);
		} else {
			return ("Choose a valid option");
		}

	}

	@Override
	public String verifyCreditcardType(BigInteger creditCardNumber) {
		boolean check = customerDao.verifyCreditCardNumber(creditCardNumber);
		if (check) {
			String type = customerDao.getcreditCardType(creditCardNumber);
			return type;
		} else {

			throw new NullPointerException("Credit Card not found");
		}

	}

	@Override
	public String raiseCreditMismatchTicket(String transactionId) throws IBSException {

		CaseIdBean caseIdObj = new CaseIdBean();
		caseIdGenOne = "RCMT";

		timestamp = LocalDateTime.now();
		caseIdTotal = addToQueryTable(caseIdGenOne);
		customerReferenceID = (caseIdTotal + transactionId);
		caseIdObj.setCaseIdTotal(caseIdTotal);
		caseIdObj.setCaseTimeStamp(timestamp);
		caseIdObj.setStatusOfQuery("Pending");
		caseIdObj.setUCI(UCI);
		caseIdObj.setDefineQuery("Transaction ID:" + transactionId);
		caseIdObj.setCustomerReferenceId(customerReferenceID);
		customerDao.raiseCreditMismatchTicket(caseIdObj, transactionId);
		return ("Ticket Raised successfully . Your reference Id is " + customerReferenceID);

	}

	public List<DebitCardTransaction> getDebitTransactions(int days, BigInteger debitCardNumber) throws IBSException {

		
			List<DebitCardTransaction> debitCardBeanTrns = customerDao.getDebitTrans(days, debitCardNumber);
			if (debitCardBeanTrns.size() == 0)
				throw new IBSException("NO TRANSACTIONS");
			return customerDao.getDebitTrans(days, debitCardNumber);
		
	}

	@Override
	public List<CreditCardTransaction> getCreditTrans(int days, BigInteger creditCardNumber) throws IBSException {

		List<CreditCardTransaction> creditCardBeanTrns = customerDao.getCreditTrans(days, creditCardNumber);
		if (creditCardBeanTrns.size() == 0)
			throw new IBSException("NO TRANSACTIONS");
		return customerDao.getCreditTrans(days, creditCardNumber);

	

	}

	@Override
	public int getPinLength(int pin) throws IBSException {
		int count = 0;
		while (pin != 0) {
			pin = pin / 10;
			++count;
		}
		if (count != 4)
			throw new IBSException("Incorrect Length of pin ");
		return count;
	}

	@Override
	public String viewQueryStatus(String customerReferenceId) throws IBSException {
		CaseIdBean caseIdObj = new CaseIdBean();
		caseIdObj.setCustomerReferenceId(customerReferenceId);

		String currentQueryStatus = customerDao.getCustomerReferenceId(caseIdObj, customerReferenceId);
		if (currentQueryStatus == null)
			throw new IBSException("Invalid Transaction Id");
		return currentQueryStatus;

	}

	@Override
	public boolean checkDebitTransactionId(String transactionId) throws IBSException {
		boolean transactionResult = customerDao.verifyDebitTransactionId(transactionId);
		if (!transactionResult)
			throw new IBSException(" Transaction ID does not exist");

		return transactionResult;
	}

	@Override
	public boolean verifyCreditCardTransactionId(String transactionId) throws IBSException {
		boolean transactionResult = customerDao.verifyCreditTransactionId(transactionId);
		if (!transactionResult)
			throw new IBSException(" Transaction ID does not exist");

		return transactionResult;
	}

	@Override
	public String checkMyChoice(int myChoice) throws IBSException {
		String cardType = myChoice + "";
		Pattern pattern = Pattern.compile("[12]");
		Matcher matcher = pattern.matcher(cardType);
		if (!(matcher.find() && matcher.group().equals(cardType)))
			throw new IBSException("Not a valid input");

		switch (myChoice) {
		case 1:
			setCardType = "Platinum";
			break;
		case 2:
			setCardType = "Gold";
			break;

		}
		return setCardType;

	}

	@Override
	public String checkMyChoiceGold(int myChoice) throws IBSException {
		String cardType = myChoice + "";
		Pattern pattern = Pattern.compile("[2]");
		Matcher matcher = pattern.matcher(cardType);
		if (!(matcher.find() && matcher.group().equals(cardType)))
			throw new IBSException("Not a valid input");
		return ("Platinum");
	}

	@Override
	public void checkDays(int days1) throws IBSException {
		if (days1 < 1) {

			throw new IBSException("Statement can not be generated for less than 1 day");

		} else if (days1 >= 730) {

			throw new IBSException("Enter days less than 730");
		}

	}

}