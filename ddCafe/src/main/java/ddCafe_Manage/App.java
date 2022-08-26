package ddCafe_Manage;

import java.util.Scanner;

import db.util.DBConn;

public class App {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		
		int ch;
		while(true) {
			System.out.println("\nミ★ 관리자 프로그램 ミ★");
			
			IngredientUI in = new IngredientUI();
			MemberUI member = new MemberUI();
			MenuUI menu = new MenuUI();
			SalesUI sale = new SalesUI();
			
			try {
				do {
					System.out.print("1.메뉴관리 2.매출관리 3.재고관리 4.회원관리 5.종료");
					ch = sc.nextInt();
				} while(ch<1||ch>5);
				
				if(ch==5) break;
				
				switch(ch) {
				case 1: menu.menu();
				case 2: sale.menu();
				case 3: in.menu();
				case 4: member.menu();
				}
				
			} catch (Exception e) {
			}
		}
		sc.close();
		DBConn.close();
	}
}
