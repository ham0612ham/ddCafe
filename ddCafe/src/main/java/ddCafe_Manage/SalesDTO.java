package ddCafe_Manage;

public class SalesDTO {
	private String saledate;
	private int seq;
	private String qty;
	private int amount;
	private String regdate;
	public char[] getmenu;
		
	public String getSaledate() {
		return saledate;
	}
	public void setSaledate(String saledate) {
		this.saledate = saledate;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public String getQty() {
		return qty;
	}
	public void setQty(String string) {
		this.qty = string;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	@Override
	public String toString() {
		return "SalesDTO [saledate=" + saledate + ", seq=" + seq + ", , qty=" + qty
				+ ", amount=" + amount + ", regdate=" + regdate + "]";
	}
	public String setBest() {
		return null;
	}
	public String getmenu() {
		return null;
	}
	public void setName(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setmenu(String string) {
		// TODO Auto-generated method stub
		
	}

	}

		