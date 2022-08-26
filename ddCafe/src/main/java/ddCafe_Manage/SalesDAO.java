package ddCafe_Manage;

import java.util.List;
import java.util.Map;

public interface SalesDAO {
	public Map<String, Integer> countTakeOut(); // 매장 / 포장 카운트
	public List<MenuDTO> listPanmai(); // 메뉴별 판매량 읽기
	public List<MenuDTO> listBestMenu(); // 판매량 확인
	public List<MenuDTO> bestMenues(); // 베스트3 메뉴 확인
	public List<SalesDTO> listLately(); // 최근 매출 확인
	public SalesDTO listToday(); // 오늘 매출 확인
	public List<SalesDTO> listWeek(); // 일주일간 매출 확인
	public List<SalesDTO> listMonth(); // 한 달간 매출 확인
	public List<SalesDTO> listYear(); // 일 년간 매출 확인
	public Map<String, Integer> read_week_sales(String week); // 지정주간 매출 확인 20220825
	public Map<String, Integer> read_month_sales(String month); // 지정달 매출 확인 202208
	public Map<String, Integer> read_year_sales(String year); // 지정년도 매출 확인
}
