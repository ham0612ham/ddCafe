package ddCafe_Customer;

public class MenuDTO {
	private String category;
	private String menu;
	private int qty;
	private int price;
	private boolean best; // 불린으로 해도 되고, int로 해도 될 것 같음
	private String panmai_date;
	
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return menu;
	}
	public void setName(String name) {
		this.menu = name;
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
	
	
}
