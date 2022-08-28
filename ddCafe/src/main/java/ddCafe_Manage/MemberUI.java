package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

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
					System.out.print("\n1.회원정보확인[이름] 2.회원정보확인[전화번호] 3.회원리스트 4.회원정보수정 5.회원탈퇴 6.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>6);
				
				if(ch==6) {
					System.out.println();
					DBConn.close();
					return;
				}
				
				switch (ch) {
				case 1 : findByName(); break;
				case 2 : findByTel(); break;
				case 3 : listMember(); break;
				case 4 : updateMember(); break;
				case 5 : deleteMember(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void findByName() {
		System.out.println("\n✦ 회원 정보 확인 ︎[이름] ✦");
		String name;
		
		try {
			System.out.println("검색할 이름을 입력해 주세요.");
			name = br.readLine();
			
			List<MemberDTO> list = dao.readMemberByName(name);
			
			if(list.size() == 0) {
				System.out.println("등록된 회원 정보가 없습니다.\n");
				return;
			}
			
			System.out.println();
			System.out.println("회원번호\t회원이름\t전화번호\t\t회원등록일");
			System.out.println("-----------------------------------------------------");
			
			for(MemberDTO dto : list) {
				System.out.print(dto.getMemberNum()+"\t");
				System.out.print(dto.getName()+"\t");
				System.out.print(dto.getTel()+"\t");
				System.out.println(dto.getDate()+"\t");
			}
			System.out.println();
			
		} catch (Exception e) {
			System.out.println("이름 검색에 실패했습니다.");
		}
		System.out.println();
	}
	
	public void findByTel() {
		System.out.println("\n✦ 회원 정보 확인 ︎[전화번호] ✦");
		String tel;
		String p = "010-\\d{4}-\\d{4}";
		
		try {
			System.out.println("검색할 전화번호를 입력해 주세요.");
			tel = br.readLine();
			
			if(!tel.matches(p)) {
				System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
			}
			
			MemberDTO dto = dao.readMemberByTel(tel);
			
			if(dto == null) {
				System.out.println("등록된 회원 정보가 없습니다.\n");
				return;
			}
			
			System.out.println();
			System.out.println("회원번호\t회원이름\t전화번호\t\t회원등록일");
			System.out.println("-----------------------------------------------------");
			
			
			System.out.print(dto.getMemberNum()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getTel()+"\t");
			System.out.println(dto.getDate()+"\t");
		
			System.out.println();
			
		} catch (Exception e) {
			System.out.println("전화번호 검색에 실패했습니다.");
		}
		System.out.println();
	}
	
	public void listMember() {
		System.out.println("\n✦ 회원 리스트 ︎✦");
		
		System.out.println();
		System.out.println("회원번호\t회원이름\t전화번호\t\t회원등록일");
		System.out.println("-----------------------------------------------------");
		
		List<MemberDTO> list = dao.listMember();
		
		for(MemberDTO dto : list) {
			System.out.print(dto.getMemberNum()+"\t");
			System.out.print(dto.getName()+"\t");
			System.out.print(dto.getTel()+"\t");
			System.out.println(dto.getDate()+"\t");
		}
		System.out.println();
	}
	
	public void updateMember() {
		System.out.println("\n✦ 회원 정보 수정 ︎✦");
		String old_name, old_tel;
		String new_name, new_tel;
		String p = "010-\\d{4}-\\d{4}";
		int member_Num;
		int result=0;
		
		
		try {			
			MemberDTO dto = new MemberDTO();
			System.out.print("기존 이름를 입력해 주세요.");
			old_name = br.readLine();
			
			System.out.print("기존 전화번호를 입력해 주세요.");
			old_tel = br.readLine();
			dto = dao.readMemberByTel(old_tel);
			
			if(!old_tel.matches(p)) {
				System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
			}
			
			System.out.print("수정할 이름를 입력해 주세요.");
			new_name = br.readLine();
			
			System.out.print("수정할 전화번호를 입력해 주세요.");
			new_tel = br.readLine();
			
			if(!new_tel.matches(p)) {
				System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
			}
			
			result = dao.updateMember(new_name, new_tel, dto.getMemberNum());
			
			if(result == 0) {
				System.out.println("등록된 자료가 아닙니다.");
			} else {
				System.out.println("데이터가 수정 되었습니다.");
			}
			
		} catch (NumberFormatException e) {
			System.out.println("회원번호는 숫자만 가능합니다.");
		} catch (Exception e) {
			System.out.println("회원 정보 수정에 실패했습니다.");
		} 
		System.out.println();
	}

	public void deleteMember() {
		System.out.println("\n✦ 회원 탈퇴 ︎✦");
		
		String name, tel;
		String p = "010-\\d{4}-\\d{4}";
		
		try {
			System.out.println("삭제할 이름을 입력해 주세요.");
			name = br.readLine();
			
			System.out.println("삭제할 전화번호를 입력해 주세요.");
			tel = br.readLine();
			
			if(!tel.matches(p)) {
				System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
			}
			
			int result = dao.deleteMember(name, tel);
			
			if(result == 0) {
				System.out.println("등록된 회원 정보가 없습니다.\n");
				return;
			} else {
				System.out.println("회원 탈퇴가 완료되었습니다.");
			}
			
		} catch (Exception e) {
			System.out.println("회원 탈퇴에 실패했습니다.");
		}
		System.out.println();
	}
}
