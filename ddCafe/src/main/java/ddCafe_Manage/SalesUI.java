package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import db.util.DBConn;

public class SalesUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
					sales_menu();
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	public void sales_menu() {

		int ch;

		while (true) {
			System.out.println("\n✦ 매출확인 ✦");

			try {
				do {
					System.out.print("\n1.일 매출 2.주 매출 3.월 매출 4.년 매출 5.뒤로가기 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 5);

				if (ch == 5) {
					System.out.println();
					DBConn.close();
					return;
				}
				switch (ch) {
				case 1:
					today_sales();
					return;
				case 2:
					week_sales();
					return;
				case 3:
					month_sales();
					return;
				case 4:
					year_sales();
					return;
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

	public void today_sales() {
		System.out.println("\n✦ 일 매출 확인 ︎✦");

		int ch;
		String saledate;
		String p = "\\d{4}\\d{2}\\d{2}";

		try {

			do {
				System.out.println("1.매출리스트 2.오늘매출총합[종료 : 0] => ");
				ch = Integer.parseInt(br.readLine());
			} while (ch < 0 || ch > 2);

			if (ch == 0) {
				return;
			} else if (ch == 1) {
				System.out.print("검색할 날짜 =>");
				saledate = br.readLine();

				if (!saledate.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[YYYYMMDD]");
					return;
				} else {
					List<SalesDTO> list = dao.selectToday(saledate);

					for (SalesDTO dto : list) {
						String date;
						date = dto.getSaledate() == null ? "총 매출액 : " : dto.getSaledate() + " 매출액 : ";
						System.out.print(date);
						System.out.println(dto.getPrice());
					}
				}

			} else if (ch == 2) {
				List<SalesDTO> list = dao.listToday();

				for (SalesDTO dto : list) {
					System.out.println();
					System.out.print(dto.getSaledate());
					System.out.print(" 매출액 : " + dto.getPrice());
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void week_sales() {
		System.out.println("\n✦ 주 매출 확인 ︎✦");

		int ch;
		String saledate = null;
		String p = "\\d{4}\\d{2}\\d{2}";

		try {

			do {
				System.out.println("1.매출리스트 2.주매출총합[종료 : 0] => ");
				ch = Integer.parseInt(br.readLine());
			} while (ch < 0 || ch > 2);

			if (ch == 0) {
				return;
			} else if (ch == 1) {
				System.out.print("검색할 날짜 =>");
				saledate = br.readLine();

				if (!saledate.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[YYYYMMDD]");
					return;
				} else {
					List<SalesDTO> list = dao.selectWeek(saledate);

					for (SalesDTO dto : list) {
						String date;
						date = dto.getSaledate() == null ? "총 매출액 : " : dto.getSaledate() + " 매출액 : ";
						System.out.print(date);
						System.out.println(dto.getPrice());
					}
				}

			} else if (ch == 2) {
				List<SalesDTO> list = dao.listWeek();

				for (SalesDTO dto : list) {
					System.out.println();
					System.out.print(dto.getWeek1() + " ~ " + dto.getWeek2());
					System.out.print(" 매출액 : " + dto.getPrice());
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void month_sales() {
		System.out.println("\n✦ 월 매출 확인 ︎✦");

		int ch;
		String saledate;
		String p = "\\d{4}\\d{2}";

		try {

			do {
				System.out.println("1.매출리스트 2.월매출총합[종료 : 0] => ");
				ch = Integer.parseInt(br.readLine());
			} while (ch < 0 || ch > 2);

			if (ch == 0) {
				return;
			} else if (ch == 1) {
				System.out.print("검색할 날짜 =>");
				saledate = br.readLine();

				if (!saledate.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[YYYYMM]");
					return;
				} else {
					List<SalesDTO> list = dao.selectMonth(saledate);

					for (SalesDTO dto : list) {
						String date;
						date = dto.getSaledate() == null ? "총 매출액 : " : dto.getSaledate() + " 매출액 : ";
						System.out.print(date);
						System.out.println(dto.getPrice());
					}
				}

			} else if (ch == 2) {
				List<SalesDTO> list = dao.listMonth();

				for (SalesDTO dto : list) {
					System.out.println();
					System.out.print(dto.getSaledate());
					System.out.print("월 매출액 : " + dto.getPrice());
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void year_sales() {
		System.out.println("\n✦ 년 매출 확인 ︎✦");

		int ch;
		String saledate;
		String p = "\\d{4}";

		try {

			do {
				System.out.println("1.매출리스트 2.년매출총합[종료 : 0] => ");
				ch = Integer.parseInt(br.readLine());
			} while (ch < 0 || ch > 2);

			if (ch == 0) {
				return;
			} else if (ch == 1) {
				System.out.print("검색할 날짜 =>");
				saledate = br.readLine();

				if (!saledate.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[YYYY]");
					return;
				} else {
					List<SalesDTO> list = dao.selectYear(saledate);

					for (SalesDTO dto : list) {
						String date;
						date = dto.getSaledate() == null ? "총 매출액 : " : dto.getSaledate() + " 매출액 : ";
						System.out.print(date);
						System.out.println(dto.getPrice());
					}
				}

			} else if (ch == 2) {
				List<SalesDTO> list = dao.listYear();

				for (SalesDTO dto : list) {
					System.out.println();
					System.out.print(dto.getSaledate());
					System.out.print("년 매출액 : " + dto.getPrice());
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
