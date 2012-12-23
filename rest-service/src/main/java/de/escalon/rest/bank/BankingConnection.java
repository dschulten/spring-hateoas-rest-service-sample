package de.escalon.rest.bank;

import java.io.Serializable;

import de.escalon.rest.Person;

public class BankingConnection implements Serializable {

	private static final long serialVersionUID = 1L;
	private Person accountHolder;
	private String accountNumber;
	private String bankIdBlz;
	public Person getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(Person accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getBankIdBlz() {
		return bankIdBlz;
	}

	public void setBankIdBlz(String bankIdBlz) {
		this.bankIdBlz = bankIdBlz;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankLocation() {
		return bankLocation;
	}

	public void setBankLocation(String bankLocation) {
		this.bankLocation = bankLocation;
	}

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
