package de.escalon.rest.bank;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MoneyTransferAccess {

	Map<Long, MoneyTransfer> transfers = new HashMap<Long, MoneyTransfer>();
	
	public MoneyTransfer createTransfer(BankingConnection from, BankingConnection to, BigInteger amount) {
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setFrom(from);
		moneyTransfer.setTo(to);
		moneyTransfer.setAmount(amount);
		transfers.put(moneyTransfer.getId(), moneyTransfer);
		return moneyTransfer;
	}

	public MoneyTransfer getTransfer(Long id) {
		return transfers.get(id);
	}

}
