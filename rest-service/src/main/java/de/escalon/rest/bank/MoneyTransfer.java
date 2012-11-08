package de.escalon.rest.bank;

import java.math.BigInteger;

import org.springframework.hateoas.Identifiable;

public class MoneyTransfer implements Identifiable<Long> {

	enum Status {
		ACCEPTED, REJECTED, DONE;
	}

	
	
	private static Long count = 0L;
	
	private BankingConnection from;
	private BankingConnection to;
	private BigInteger amount;
	private Status status = Status.ACCEPTED;
	private Long id;
	
	public MoneyTransfer() {
		this.id = ++count;
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setFrom(BankingConnection from) {
		this.from = from;
	}
	
	public void setTo(BankingConnection to) {
		this.to = to;
	}
	
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public BankingConnection getFrom() {
		return from;
	}

	public BankingConnection getTo() {
		return to;
	}

	public BigInteger getAmount() {
		return amount;
	}

	public Status getStatus() {
		return status;
	}
	
	
	
}
