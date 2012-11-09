package de.escalon.rest.bank;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.Identifiable;

public class Banking implements Identifiable<String> {

	Map<String, Account> accounts = new HashMap<String, Account>();

	public Account getAccount(String number) {
		return accounts.get(number);
	}

	private String bankName;
	private String blz;
	private String bic;

	public Banking(String bankName, String blz, String bic) {
		super();
		this.bankName = bankName;
		this.blz = blz;
		this.bic = bic;
	}

	public String getBankName() {
		return bankName;
	}

	public String getBlz() {
		return blz;
	}

	public String getBic() {
		return bic;
	}

	public String getId() {
		return blz;
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void addAccount(Account account) {
		accounts.put(account.getNumber(), account);
	}

}
