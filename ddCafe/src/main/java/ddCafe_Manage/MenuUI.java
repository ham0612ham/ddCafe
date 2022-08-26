package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;


public class MenuUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MenuDAO dao = new MenuDAOmpl();
	
	public void menu() {
		
		int ch;
		while(true) {
			System.out.println("\n✦ 메뉴 관리 ✦");
			try {
				do {
					System.out.print("\n1.메뉴추가 2.품절처리 3.메뉴삭제 4.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>4);
				
				if(ch==4) {
					System.out.println();
					DBConn.close();
					return;
				}
				
				switch (ch) {
				case 1 : add_menu(); break;
				case 2 : sold_out(); break;
				case 3 : delete_menu(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void add_menu() {
		System.out.println("\n✦ 메뉴 추가 ︎✦");
	}
	
	public void sold_out() {
		System.out.println("\n✦ 품절 처리 ︎✦");
	}
	
	public void delete_menu() {
		System.out.println("\n✦ 메뉴 삭제 ︎✦");
	}
}
