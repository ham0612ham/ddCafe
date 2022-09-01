package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;

public interface IngredientDAO {
	
	public List<IngredientDTO> left_ingredient(); // 1.현재 재고 확인
	
 
	public int add_ingredient(IngredientDTO dto) throws SQLException; // 2.재고 추가 주문
	public List<IngredientDTO> show_orderlist(); // 2.현재 재고와 vendor list
	
	public List<IngredientDTO> receiving_history(); // 3. 입고된 재료 수량 확인
	

	public int newIngredient(IngredientDTO dto) throws SQLException; // 4.새로운재료추가
	public int newVendor(String vendorName, String managerName, String managerTel, String compRegisNum) throws SQLException; // 4. 납품업체 추가
	
	
	public int sub_ingredient(IngredientDTO dto) throws SQLException; // 5.재고 삭제
	public List<IngredientDTO> trash_ingredientcode(); // 5.재고들 보여주는 list
	
	public List<IngredientDTO>vendorList(); // 6.납품업체 리스트 확인
} 