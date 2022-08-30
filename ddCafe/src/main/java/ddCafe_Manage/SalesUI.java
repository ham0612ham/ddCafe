package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import db.util.DBConn;

public class SalesUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	@SuppressWarnings("rawtypes")
	private SalesDAO dao = new SalesDAOImpl();

	public void menu() {

		int ch;

		while (true) {
			System.out.println("\n✦ 매출 관리 ✦");

			try {
				do {
					System.out.print("\n1.메뉴판매량 2.베스트메뉴확인 3.매출확인 4.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 4);

				if (ch == 4) {
					System.out.println();
					DBConn.close();
					return;
				}
				switch (ch) {
				case 1:
					sales_by_menu();
					break;
				case 2:
					best_menu();
					break;
				case 3:
					check_sales();
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	public void sales_by_menu() {
		System.out.println("\n✦ 메뉴 판매량 ︎✦");
		Map<String, Integer> map = dao.countTakeOut();

		int store = map.get("store");
		int takeout = map.get("takeout");

		System.out.println("매장 : " + store);
		System.out.println("포장 : " + takeout);

		System.out.println();

	}

	public Map<String, Integer> best_menu() {
		System.out.println("\n✦ 베스트 메뉴 확인 ︎✦");
		@SuppressWarnings("unused")
		List<MenuDTO> map = dao.bestMenues();

		System.out.println(((SalesDTO) dao).setBest() + "\t");
		System.out.println(((MenuDTO) dao).getQty() + "\t");
		System.out.println(((SalesDTO) dao).getSeq() + "\t");
		System.out.println(((SalesDTO) dao).getmenu() + "\t");

		System.out.println();
		return null;

	}

	public void check_sales() {
		System.out.println("\n✦ 매출 확인 ︎✦");
		List<SalesDTO> list = dao.listWeek();

		for (SalesDTO dto : list) {
			System.out.print(dto.getSeq() + "\t");
			System.out.print(dto.getAmount() + "\t");
			System.out.print(dto.getRegdate() + "\t");
			System.out.print(dto.getQty());
		}
	}
}
