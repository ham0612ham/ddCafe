package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


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
					System.out.print("\n1.메뉴추가 2.전체메뉴보기 3.메뉴검색  4.품절메뉴보기 5.메뉴삭제 6.품절처리 7.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>7);
				
				if(ch==7) {
					System.out.println();
					DBConn.close();
					return;
				}
				
				switch (ch) {
				case 1 : add_menu(); break;
				case 2 : list_menu(); break;
				case 3 : read_menu(); break;
				case 4 : read_sold_out(); break;
				case 5 : delete_menu(); break;
				case 6 : sold_out(); break;
				}
			} catch (Exception e) {
			}
		}
	}
	

	public void add_menu() {
		System.out.println("\n✦ 메뉴 추가 ︎✦");
		
		try {
			MenuDTO dto = new MenuDTO();
			
			System.out.print("추가하고싶은 메뉴?");
			dto.setMenuName(br.readLine());
			
			System.out.print("카테고리 번호");
			dto.setCategoryNum(Integer.parseInt(br.readLine()));
			
			System.out.print("현재 상태?");
			dto.setStatus(br.readLine());
			
			System.out.print("메뉴 가격?");
			dto.setMenuPrice(Integer.parseInt(br.readLine()));
			
			System.out.print("사이즈?");
			dto.setMenuSize(br.readLine());
			
			dao.addMenu(dto);
		} catch (Exception e) {
			System.out.println("메뉴 등록이 실패했습니다.");
		}
	}
	
	public void list_menu() {
		System.out.println("\n메뉴 리스트 !!!");
		System.out.println("메뉴번호\t메뉴이름\t카테고리번호\t상태\t가격\t사이즈");
		System.out.println("-----------------------------------------------------------------------");
		
		List<MenuDTO> list = dao.listMenu();
		for(MenuDTO dto : list) {
			System.out.print(dto.getMenuNum()+"\t");
			System.out.print(dto.getMenuName()+"\t\t");
			System.out.print(dto.getCategoryNum()+"\t");
			System.out.print(dto.getStatus()+"\t");
			System.out.print(dto.getMenuPrice()+"\t");
			System.out.println(dto.getMenuSize());
		
		}
		System.out.println();
		
	}
	
	public void read_menu() {
		System.out.println("\n메뉴 검색 !!!");
		
		String name;
		
		try {
			System.out.print("검색할 메뉴?");
			name = br.readLine();
			
			List<MenuDTO> list=dao.listMenu(name);
			
			if(list.size() ==0) {
				System.out.println("등록된 자료가 없습니다.");
				return;
			}
			for(MenuDTO dto : list) {
				System.out.print(dto.getMenuNum()+"\t");
				System.out.print(dto.getMenuName()+"\t");
				System.out.print(dto.getCategoryNum()+"\t");
				System.out.print(dto.getStatus()+"\t");
				System.out.print(dto.getMenuPrice()+"\t");
				System.out.println(dto.getMenuSize());
			}
		} catch (Exception e) {
			System.out.println("이름 검색 실패");
		}
		System.out.println();
	}
	
	public void read_sold_out() {
		System.out.println("\n✦ 품절 메뉴 보기 ︎✦");
		
		System.out.println("메뉴");
		System.out.println("-------------------------");
		
		List<MenuDTO> list = dao.listSoldout();
		for(MenuDTO dto : list) {
			System.out.println(dto.getSoldMenu());
		}
		System.out.println();
	}
	
	public void delete_menu() {
		System.out.println("\n✦ 메뉴 삭제 ︎✦");
		
		int code;
		try {
			System.out.print("삭제할 메뉴코드?");
			code = Integer.parseInt(br.readLine());
			
			dao.deleteMenu(code);

			System.out.println("메뉴를 삭제 했습니다.");
			
		} catch (Exception e) {
			System.out.println("데이터 삭제 실패 !!");
		}
		
		System.out.println();
		
	}
	public void sold_out() {
		System.out.println("\n 메뉴 품절 처리");
		int code;
		
		try {
			System.out.println("품절 처리할 메뉴코드?");
			code = Integer.parseInt(br.readLine());
			
			dao.updateSoldOut(code);
			System.out.println("메뉴 품절처리 완료");
		} catch (Exception e) {
			System.out.println("품절처리 실패!");
		}
	}
}
