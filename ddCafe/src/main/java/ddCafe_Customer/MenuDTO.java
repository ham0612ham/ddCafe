package ddCafe_Customer;

public class MenuDTO {
	private String category;
	private String menu;
	private int qty;
	private int price;
	private boolean best;
	private String panmai_date;
	private String takeout_togo;
	private int yesOrNo;
	private String size;
	private int menu_detail_code;
	private String status;
	private int rank;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isBest() {
		return best;
	}
	public void setBest(boolean best) {
		this.best = best;
	}
	public String getPanmai_date() {
		return panmai_date;
	}
	public void setPanmai_date(String panmai_date) {
		this.panmai_date = panmai_date;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public String getTakeout_togo() {
		return takeout_togo;
	}
	public void setTakeout_togo(String takeout_togo) {
		this.takeout_togo = takeout_togo;
	}
	public int getYesOrNo() {
		return yesOrNo;
	}
	public void setYesOrNo(int yesOrNo) {
		this.yesOrNo = yesOrNo;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getMenu_detail_code() {
		return menu_detail_code;
	}
	public void setMenu_detail_code(int menu_detail_code) {
		this.menu_detail_code = menu_detail_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
