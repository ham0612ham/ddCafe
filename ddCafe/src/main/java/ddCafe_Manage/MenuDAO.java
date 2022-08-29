package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;

public interface MenuDAO {
	public int addMenu(MenuDTO dto) throws SQLException; // 메뉴 추가
	public int updateMenuprice(MenuDTO dto) throws SQLException; // 메뉴 가격 변경
	public int deleteMenu(int code) throws SQLException; //메뉴 삭제
	public List<MenuDTO> listMenu(); // 메뉴 리스트
	public List<MenuDTO> listMenu(String menuName); // 메뉴검색
	public int addCategory() throws SQLException; // 카테고리 추가
	public int selectIngredient() throws SQLException; // 재료 선택
	public int updateSoldOut() throws SQLException; // 메뉴 품절 처리
	public int seachSoldOut() throws SQLException; // 품절메뉴 확인 ??
	public int seachSoldOut(String menu) throws SQLException; // 해당 메뉴 품절 여부 확인
}