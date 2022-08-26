package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;

public class IngredientUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private IngredientDAO dao = new IngredientImpl();
	
	public void menu() {
		System.out.println("\n✦ 재료 메뉴 ✦");
		
		int ch;
		
		while(true) {
			try {
				do {
					System.out.println("1.재고확인 2.재고추가주문 3.입고내역 4.새로운재료추가 5.납품업체확인 6.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>6);
				
				if(ch==6) {
					DBConn.close();
					return;
				}
				
				switch (ch) {
				case 1 : break;
				case 2 : break;
				case 3 : break;
				case 4 : break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void check_ingredient() {
		
	}
	
	public void order_ingredietn() {
		
	}
	
	public void receiving_history() {
		
	}

	public void add_ingredient() {
		
	}
	
	public void check_vendor() {
		
	}
}
