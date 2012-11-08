package de.escalon.rest.bank;

import java.math.BigInteger;

import org.springframework.hateoas.Identifiable;

public class Account implements Identifiable<BankingConnection> {

	String number;
	BigInteger deposit;

	public Account(String number) {
		super();
		this.number = number;
	}

	public BigInteger getDeposit() {
		return deposit;
	}

	public void setDeposit(BigInteger deposit) {
		this.deposit = deposit;
	}

	public BankingConnection getId() {
		BankingConnection id = new BankingConnection(number);
		return id;
	}

	public String getNumber() {
		return number;
	}

}
