package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IngredientDAO {
	
	public List<IngredientDTO> leftingredient(); // 1.현재 재고 확인
	

	
	public int add_ingredient(String ingredient, int qty)throws SQLException; // 2.재고 추가 주문
  //public int add_ingredient(IngredientDTO dto) throws SQLException; // 2.재고 추가 주문

	
	public List<IngredientDTO> receiving_history(); // 3. 입고된 재료 수량 확인
	
	
	public int new_ingredient(String ingredient, int qty) throws SQLException; // 4.새로운재료추가
	
	
  //public int sub_ingredient(Number ingredient, int qty, String vendor)throws SQLException; // 5.재고 삭제
	public int sub_ingredient(IngredientDTO dto) throws SQLException; // 5.재고 삭제
	
	
	public List<IngredientDTO>vendorList(); // 6.납품업체 리스트 확인
} 