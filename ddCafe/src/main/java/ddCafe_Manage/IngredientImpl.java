package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class IngredientImpl implements IngredientDAO{

	@Override
	public List<IngredientDTO> vendorList() {
		return null;
	}

	@Override
	public Map<String, Integer> left_ingredient() {
		return null;
	}

	@Override
	public Map<String, Integer> in_ingredient(String ingredient, String date) {
		return null;
	}

	@Override
	public Map<String, Integer> in_ingredient(String date) {
		return null;
	}

	@Override
	public int add_ingredient(String ingredient, int qty, String vendor) throws SQLException {
		return 0;
	}

	@Override
	public int sub_ingredient(String ingredient, int qty, String vendor) throws SQLException {
		return 0;
	}

}
