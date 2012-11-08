package de.escalon.rest.bank;

import org.springframework.hateoas.Resource;

public class BankingResource extends Resource<Banking> {

	
	
	private String bankName;
	private String blz;
	private String bic;
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public void setBlz(String blz) {
		this.blz = blz;
	}
	public void setBic(String bic) {
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

	
}
