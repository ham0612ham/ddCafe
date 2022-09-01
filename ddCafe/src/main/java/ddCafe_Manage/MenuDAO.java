package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;

public interface MenuDAO {
	public int addMenu(MenuDTO dto) throws SQLException; // 메뉴 추가
	public int updateMenuprice(MenuDTO dto) throws SQLException; // 메뉴 가격 변경
	public int deleteMenu(int code) throws SQLException; //메뉴 삭제
	public List<MenuDTO> listMenu(); // 메뉴 리스트
	public List<MenuDTO> listMenu(String menuName); // 메뉴검색
	public List<MenuDTO> listSoldout();  // 품절메뉴 확인
	public List<MenuDTO> selectCategory() throws SQLException; // 카테고리 가져오기
	public List<MenuDTO> showTwoMenu(); // 메뉴코드 메뉴명 가져오기
	
	public int insertIngredient(IngredientDTO dto) throws SQLException; // 재료 추가
	
	public int updateSoldOut(int code) throws SQLException; // 메뉴 품절 처리
}