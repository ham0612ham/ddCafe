package ddCafe_Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface KioskDAO {
	// public void choiceTogo(int a); // 포장, 매장 선택
	public List<String> showCategory(); // 카테고리 불러오기
	public List<MenuDTO> showMenues(int category_num); // 카테고리 선택 후 메뉴 불러오기
	public List<String> showMenues2(int category_num); // 메뉴 이름만 불러옴
	public int orderMenues(List<MenuDTO> list, String takeout_togo, int member_code)throws SQLException; // 메뉴 선택, 수량 선택
	public void bestMenues();// 베스트메뉴 정하는 인터페이스
	public int addMember(String name, String tel) throws SQLException; // 회원가입
	public MemberDTO findMember(String tel); // 회원 찾기
	public boolean useStamp(boolean a) throws SQLException;// 스탬프 사용 여부
	public List<String> showPaymentMethod(int choice); // 결제 방법 선택
	public int totalPrice(List<MenuDTO> list); // 총가격 구함
}