package de.escalon.rest.bank;

import java.io.Serializable;

import de.escalon.rest.Person;

public class BankingConnection implements Serializable {

	private static final long serialVersionUID = 1L;
	private Person accountHolder;
	private String accountNumber;
	private String bankIdBlz;
	private String iban;
	private String bankName;
	private String bankLocation;

	@Override
	public String toString() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BankingConnection(String accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	
	
	
	

}
