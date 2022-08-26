package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import db.util.DBConn;

public class MemberUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAOImpl();
	
	public void menu() {
		
		int ch;
		
		while(true) {
			System.out.println("\n✦ 회원 메뉴 ✦");
			try {
				do {
					System.out.print("\n1.회원정보확인 2.회원리스트 3.회원정보수정 4.회원탈퇴 5.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>5);
				
				if(ch==5) {
					System.out.println();
					DBConn.close();
					return;
				}
				
				switch (ch) {
				case 1 : readMember(); break;
				case 2 : listMember(); break;
				case 3 : updateMember(); break;
				case 4 : deleteMember(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void readMember() {
		System.out.println("\n✦ 회원 정보 확인 ︎✦");
	}
	
	public void listMember() {
		System.out.println("\n✦ 회원 리스트 ︎✦");
	}
	
	public void updateMember() {
		System.out.println("\n✦ 회원 정보 수정 ︎✦");
	}

	public void deleteMember() {
		System.out.println("\n✦ 회원 탈퇴 ︎✦");
	}
}
