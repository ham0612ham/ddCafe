package ddCafe_Customer;

import java.sql.SQLException;
import java.util.List;

public interface KioskDAO {
	// public void choiceTogo(int a); // 포장, 매장 선택
	public List<String> showCategory(); // 카테고리 불러오기
	public List<MenuDTO> showMenues(int category_num); // 카테고리 선택 후 메뉴 불러오기
	public List<String> showMenues2(int category_num); // 메뉴 이름만 불러옴
	public int orderMenues(List<MenuDTO> list, String takeout_togo, int member_code, String payment_method, int stampUse_price)throws SQLException; // 주문이 완료되어 모든 테이블 업데이트
	public List<MenuDTO> bestMenues();// 베스트메뉴 정하는 인터페이스
	public int addMember(String name, String tel) throws MyDuplicationException, SQLException ; // 회원가입
	public MemberDTO findMember(String tel) throws SQLException; // 회원 찾기
	public int usableStamp(int member_code) throws SQLException;// 사용 가능한 스탬프 개수 확인
	public List<String> showPaymentMethod(); // 결제 방법 선택
	public int totalPrice(List<MenuDTO> list); // 총가격 구함
	public List<Integer> selectPlastic(List<MenuDTO> list); // 플라스틱 합계, R/L판별
	public boolean calculateMenu(List<MenuDTO> list); // 구매하는 제품들의 재고량이 충분한지 판별
}