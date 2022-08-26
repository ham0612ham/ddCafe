package ddCafe_Manage;

public class IngredientDTO {
	private String ingredient_name;
	private int ingredient_qty;
	private String vendor_name;
	private String manager_name;
	private String manager_tel;
	
	public String getIngredient_name() {
		return ingredient_name;
	}
	
	public void setIngredient_name(String ingredient_name) {
		this.ingredient_name = ingredient_name;
	}
	
	public int getIngredient_qty() {
		return ingredient_qty;
	}
	
	public void setIngredient_qty(int ingredient_qty) {
		this.ingredient_qty = ingredient_qty;
	}
	
	public String getVendor_name() {
		return vendor_name;
	}
	
	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}
	
	public String getManager_name() {
		return manager_name;
	}
	
	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}
	
	public String getManager_tel() {
		return manager_tel;
	}

	public void setManager_tel(String manager_tel) {
		this.manager_tel = manager_tel;
	}
}
