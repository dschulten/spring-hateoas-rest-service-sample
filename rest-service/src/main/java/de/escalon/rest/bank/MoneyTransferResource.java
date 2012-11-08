package de.escalon.rest.bank;

import java.math.BigInteger;

import org.springframework.hateoas.Resource;

import de.escalon.rest.bank.MoneyTransfer.Status;

public class MoneyTransferResource extends Resource<MoneyTransfer> {
	
	private BankingConnection from;
	private BankingConnection to;
	private BigInteger amount;
	private Status status = Status.ACCEPTED;
	public BankingConnection getFrom() {
		return from;
	}
	public void setFrom(BankingConnection from) {
		this.from = from;
	}
	public BankingConnection getTo() {
		return to;
	}
	public void setTo(BankingConnection to) {
		this.to = to;
	}
	public BigInteger getAmount() {
		return amount;
	}
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

}
