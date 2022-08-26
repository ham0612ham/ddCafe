package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;
import oracle.security.crypto.core.SHA1RandomBitsSource;

public class IngredientUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private IngredientDAO dao = new IngredientImpl();
	
	public void menu() {
		System.out.println("\n✦ 재료 메뉴 ✦");
		
		int ch;
		
		while(true) {
			try {
				do {
					System.out.print("\n1.재고확인 2.재료추가주문 3.입고내역 4.새로운재료추가 5.납품업체확인 6.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>6);
				
				if(ch==6) {
					System.out.println();
					DBConn.close();
					return;
				}
				
				switch (ch) {
				case 1 : check_ingredient(); break;
				case 2 : order_ingredietn(); break;
				case 3 : receiving_history(); break;
				case 4 : add_ingredient(); break;
				case 5 : check_vendor(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void check_ingredient() {
		System.out.println("\n✦ 재료 확인 ︎✦");
		return;
	}
	
	public void order_ingredietn() {
		System.out.println("\n✦ 재료 추가 주문 ︎✦");
		return;
	}
	
	public void receiving_history() {
		System.out.println("\n✦ 입고 내역 ︎✦");
		return;
	}

	public void add_ingredient() {
		System.out.println("\n✦ 새로운 재료 추가 ︎✦");
		return;
	}
	
	public void check_vendor() {
		System.out.println("\n✦ 납품업체 확인 ︎✦");
		return;
	}
}
