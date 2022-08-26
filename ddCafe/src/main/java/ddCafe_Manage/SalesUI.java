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
			System.out.println("\\n✦ 매출 관리 ✦");
			
			try {
				do {
					System.out.print("1.메뉴 판매량 2.베스트 메뉴 확인 3.매출 확인 4.종료 => ");
					ch = Integer.parseInt(br.readLine());
					
				} while(ch<1||ch>4);
				
				if(ch==4) {
					DBConn.close();
					return;
				}
				switch(ch) {
				case 1: break;
				case 2: break;
				case 3: break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void sales_by_menu() {
		
	}
	
	public void best_menu() {
		
	}
	
	public void check_sales() {
		
	}
}
