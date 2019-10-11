package com.cg.ibs.cardmanagement.service;

import java.math.BigInteger;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cg.ibs.cardmanagement.bean.CaseIdBean;
import com.cg.ibs.cardmanagement.bean.CreditCardTransaction;
import com.cg.ibs.cardmanagement.bean.DebitCardTransaction;
import com.cg.ibs.cardmanagement.dao.BankDao;
import com.cg.ibs.cardmanagement.dao.CardManagementDaoImpl;
import com.cg.ibs.cardmanagement.exceptionhandling.IBSException;

public class BankServiceClassImpl implements BankService {

	BankDao bankDao = new CardManagementDaoImpl();
	CaseIdBean caseIdObj = new CaseIdBean();

	@Override
	public List<CaseIdBean> viewQueries() {

		List<CaseIdBean> caseBeans = bankDao.viewAllQueries();

		return bankDao.viewAllQueries();

	}
	public List<DebitCardTransaction> getDebitTransactions(int dys, BigInteger debitCardNumber) throws IBSException {
		
				List<DebitCardTransaction> debitCardBeanTrns = bankDao.getDebitTrans(dys, debitCardNumber);
				if (debitCardBeanTrns.size() == 0)
					throw new IBSException("NO TRANSACTIONS");
				return bankDao.getDebitTrans(dys, debitCardNumber);
			} 	

	

	@Override
	public boolean verifyQueryId(String queryId) throws IBSException {

		boolean check = bankDao.verifyQueryId(queryId);
		if (check) {
			return true;
		} else {
		
				throw new IBSException("Invalid Query Id");
		}
	}

	@Override
	public void setQueryStatus(String queryId, String newStatus) {
	
		bankDao.setQueryStatus(queryId, newStatus);

	}
	public boolean verifyDebitCardNumber(BigInteger debitCardNumber) throws IBSException {
		String debitCardNum = debitCardNumber.toString();
		Pattern pattern = Pattern.compile("[0-9]{16}");
		Matcher matcher = pattern.matcher(debitCardNum);
		if (!(matcher.find() && matcher.group().equals(debitCardNum)))
			throw new IBSException("Incorrect  length");
		boolean check = bankDao.verifyDebitCardNumber(debitCardNumber);
		if (!check)
			throw new IBSException(" Debit Card Number does not exist");
		return (check);

	}
	public boolean verifyCreditCardNumber(BigInteger creditCardNumber) throws IBSException {
		String creditCardNum = creditCardNumber.toString();
		Pattern pattern = Pattern.compile("[0-9]{16}");
		Matcher matcher = pattern.matcher(creditCardNum);
		if (!(matcher.find() && matcher.group().equals(creditCardNum)))
			throw new IBSException("Incorrect  length");
		boolean check1 = bankDao.verifyCreditCardNumber(creditCardNumber);
		if (!check1)
			throw new IBSException(" Credit Card Number does not exist");
		return (check1);
	}
	
	@Override
	public List<CreditCardTransaction> getCreditTrans(int days, BigInteger creditCardNumber) throws IBSException {
		

				List<CreditCardTransaction> creditCardBeanTrns = bankDao.getCreditTrans(days, creditCardNumber);
				if (creditCardBeanTrns.size() == 0)
					throw new IBSException("NO TRANSACTIONS");
				return bankDao.getCreditTrans(days, creditCardNumber);
			
		
	}
	
	public void checkDays(int days1) throws IBSException {
		if (days1 < 1) {

			throw new IBSException("Statement can not be generated for less than 1 day");

		} else if (days1 >= 730) {

			throw new IBSException("Enter days less than 730");
		}

	}
}