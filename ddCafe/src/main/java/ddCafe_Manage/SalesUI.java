package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;

public class SalesUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private SalesDAO dao = new SalesDAOImpl();
	
	public void menu() {
	
		int ch;
		
		while(true) {
			System.out.println("\n✦ 매출 관리 ✦");
			
			try {
				do {
					System.out.print("\n1.메뉴판매량 2.베스트메뉴확인 3.매출확인 4.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>4);
				
				if(ch==4) {
					System.out.println();
					DBConn.close();
					return;
				}
				switch(ch) {
				case 1: sales_by_menu(); break;
				case 2: best_menu(); break;
				case 3: check_sales(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void sales_by_menu() {
		System.out.println("\n✦ 메뉴 판매량 ︎✦");
	}
	
	public void best_menu() {
		System.out.println("\n✦ 베스트 메뉴 확인 ︎✦");
	}
	
	public void check_sales() {
		System.out.println("\n✦ 매출 확인 ︎✦");
	}
}
