package de.escalon.rest.bank;

import org.springframework.stereotype.Component;

@Component
public class BankingAccess {

	public Banking getBanking() {
		Banking banking = new Banking("Kreissparkasse Heilbronn", "62050000", "HEISDE66XXX");
		Account account = new Account("123456");
		banking.addAccount(account);
		return banking;
	}

	public Banking getBankingByBlz(String blz) {
		return getBanking();
	}

}
