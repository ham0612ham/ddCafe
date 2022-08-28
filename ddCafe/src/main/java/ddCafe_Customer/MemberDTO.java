package ddCafe_Customer;

public class MemberDTO {
	private int member_code;
	private String member_name;
	private String member_tel;
	private String member_date;
	private int stamp_num;
	private int order_num;
	private int stampUse_date;
	private int usable_stamp;
	
	public int getMember_code() {
		return member_code;
	}
	public void setMember_code(int member_code) {
		this.member_code = member_code;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMemeber_tel() {
		return member_tel;
	}
	public void setMemeber_tel(String memeber_tel) {
		this.member_tel = memeber_tel;
	}
	public String getMember_date() {
		return member_date;
	}
	public void setMember_date(String member_date) {
		this.member_date = member_date;
	}
	public int getStamp_num() {
		return stamp_num;
	}
	public void setStamp_num(int stamp_num) {
		this.stamp_num = stamp_num;
	}
	public int getOrder_num() {
		return order_num;
	}
	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}
	public int getStampUse_date() {
		return stampUse_date;
	}
	public void setStampUse_date(int stampUse_date) {
		this.stampUse_date = stampUse_date;
	}
	public String getMember_tel() {
		return member_tel;
	}
	public void setMember_tel(String member_tel) {
		this.member_tel = member_tel;
	}
	public int getStamp_count() {
		return usable_stamp;
	}
	public void setStamp_count(int stamp_count) {
		this.usable_stamp = stamp_count;
	}
	
}
