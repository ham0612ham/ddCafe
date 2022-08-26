package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IngredientDAO {
	public List<IngredientDTO>vendorList(); // 납품업체 리스트 확인
	public Map<String, Integer> left_ingredient(); // 현재 재고 확인
	public Map<String, Integer> in_ingredient(String ingredient, String date); // 입고된 재료 수량 확인
	public Map<String, Integer> in_ingredient(String date); // 입고된 재료 수량 확인
	public int add_ingredient(String ingredient, int qty, String vendor)throws SQLException; // 재고 추가 주문
	public int sub_ingredient(String ingredient, int qty, String vendor)throws SQLException; // 재고 삭제
}