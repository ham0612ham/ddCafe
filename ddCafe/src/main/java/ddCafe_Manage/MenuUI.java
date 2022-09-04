package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MenuUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MenuDAO dao = new MenuDAOImpl();
	private IngredientDAO dio = new IngredientImpl();
	
	public void menu() {
		
		int ch;
		while(true) {
			System.out.println("\n✦ 메뉴 관리 ✦");
			try {
				do {
					System.out.print("\n1.메뉴추가 2.전체메뉴보기 3.메뉴검색 4.품절메뉴보기 5.메뉴삭제 6.품절처리 7.이전 => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>7);
				
				if(ch==7) {
					System.out.println();
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
		System.out.println("카테고리번호/카테고리");
		try {
			List<MenuDTO> list = dao.selectCategory();
			for(MenuDTO dto : list) {
				System.out.println(dto.getCategoryNum()+". " + dto.getCategoryName());		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
			
		try {
			int a,b;
			MenuDTO dto = new MenuDTO();
			
			System.out.print("추가하고싶은 메뉴를 입력해주세요 => ");
			dto.setMenuName(br.readLine());
		
			System.out.print("카테고리 번호를 입력해주세요 => ");
			b = Integer.parseInt(br.readLine());
			if(b >7) {
				System.out.println("카테고리 번호 오류");
				return;
			}else {
				dto.setCategoryNum(b);
			}			
			
			int ch;
			do {
				System.out.print("현재 상태를 입력해주세요 [1.주문가능/2.품절] => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch<1||ch>2);
			String status = null;
			if(ch==1) {
				status = "null";
			} else if(ch==2) {
				status = "품절";
			}
			dto.setStatus(status);
				
			System.out.print("메뉴 가격을 입력해주세요 ");
			dto.setMenuPrice(Integer.parseInt(br.readLine()));
			int c;
			do {
				System.out.print("사이즈를 입력해주세요 [1.R/2.L/3.사이즈 없음] => ");
				c = Integer.parseInt(br.readLine());
			} while(c<1||c>3);
			String size = null;
			if(c==1) {
				size = "R";
			} else if(c ==2) {
				size = "L";
			} else if(c ==3) {
				size = "null";
			}
			dto.setMenuSize(size);

			try {
				List<IngredientDTO> list = dio.left_ingredient();
				System.out.println("\n✦ 재료 확인 ︎✦");

				for (IngredientDTO dto1 : list) {

					System.out.println(dto1.getIngredient_code()+ ". "+dto1.getIngredient_name());

				}
					
				System.out.println();
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
				e.printStackTrace();
			}
			int d;
			List<Integer> list = new ArrayList<>();
			do {
				System.out.print("메뉴에 맞는 재료번호를 입력해주세요 [0.모든재료 선택완료] => ");
				d=Integer.parseInt(br.readLine());
				list.add(d);
			}while(d!=0);
			list.remove(list.size()-1);
			
            dto.setIngredients(list);
			
			
			System.out.print("정말 추가 하시겠습니까? [1.예/2.아니요] => ");
			a = Integer.parseInt(br.readLine());
			if(a==1) {
				dao.addMenu(dto);
				System.out.println("메뉴 추가 완료");
			}else {
				return;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("숫자만 가능합니다");
		} catch (Exception e) {
			System.out.println("메뉴 등록이 실패했습니다.");
		}
	}
	
	public void list_menu() {
		System.out.println("\n✦ 메뉴 리스트 ✦");
		System.out.println("메뉴번호 / 메뉴이름 / 카테고리번호 / 상태 / 가격 / 사이즈");
		System.out.println("----------------------------------------------");
		
		List<MenuDTO> list = dao.listMenu();
		for(MenuDTO dto : list) {
			System.out.print(dto.getMenuNum()+". ");
			System.out.print(dto.getMenuName()+" / ");
			System.out.print(dto.getCategoryNum()+" / ");
			if(dto.getStatus().equals("품절")) {
				System.out.print("품절 / ");
			} else {
				System.out.print("주문가능 / ");
			}
			System.out.print(dto.getMenuPrice()+" / ");
			if(dto.getMenuSize().equals("null")) {
	            System.out.println("사이즈 없음");
	         } else {
	            System.out.println(dto.getMenuSize());
	         }
		}
		System.out.println();
		
	}
	
	public void read_menu() {
		System.out.println("\n✦ 메뉴 검색 ✦");
		System.out.println("메뉴번호 / 메뉴이름 / 카테고리번호 / 상태 / 가격 / 사이즈");
		System.out.println("----------------------------------------------");
		
		String name;
		
		try {
			System.out.print("검색할 메뉴를 입력해주세요 => ");
			name = br.readLine();
			
			List<MenuDTO> list=dao.listMenu(name);
			
			if(list.size() ==0) {
				System.out.println("등록된 메뉴가 없습니다.");
				return;
			}
			for(MenuDTO dto : list) {
				System.out.print(dto.getMenuNum()+". ");
				System.out.print(dto.getMenuName()+" / ");
				System.out.print(dto.getCategoryNum()+" / ");
				if(dto.getStatus().equals("품절")) {
					System.out.print("품절 / ");
				} else {
					System.out.print("주문가능 / ");
				}
				System.out.print(dto.getMenuPrice()+" / ");
				if(dto.getMenuSize().equals("null")) {
		            System.out.println("사이즈 없음");
		         } else {
		            System.out.println(dto.getMenuSize());
		         }
			}
		} catch (Exception e) {
			System.out.println("메뉴 검색 실패");
		}
		System.out.println();
	}
	
	public void read_sold_out() {
		System.out.println("\n✦ 품절 메뉴 보기 ︎✦");
		
		System.out.println("메뉴");
		System.out.println("--------------");
		
		List<MenuDTO> list = dao.listSoldout();
		for(MenuDTO dto : list) {
			System.out.println(dto.getSoldMenu());
		}
		
		System.out.println();
	}
	
	public void delete_menu() {
		System.out.println("\n✦ 메뉴 삭제 ︎✦");
		
		int code=0,c = 0;
		
		try {
			List<MenuDTO> list=dao.showTwoMenu();
			for(MenuDTO dto : list) {
				System.out.println(dto.getMenuNum()+". "+dto.getMenuName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
		try {
			System.out.print("삭제할 메뉴코드를 입력해주세요 => ");
			code = Integer.parseInt(br.readLine());

			System.out.print("정말 삭제 하시겠습니까 [1.예 2.아니요] => ");
			c = Integer.parseInt(br.readLine());
			if(c == 1) {
				dao.deleteMenu(code);
			}else {
				return;
			}
			System.out.println("메뉴를 삭제 했습니다.");
			
		} catch (Exception e) {
			System.out.println("메뉴 삭제 실패 !!");
		}
		
		System.out.println();
		
	}
	public void sold_out() {
		System.out.println("\n✦ 메뉴 품절 처리 ︎✦");
		int code,c;
		try {
			List<MenuDTO> list=dao.showTwoMenu();
			for(MenuDTO dto : list) {
				System.out.println(dto.getMenuNum()+". "+dto.getMenuName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		try {
			System.out.print("품절 처리할 메뉴코드를 입력해주세요 => ");
			code = Integer.parseInt(br.readLine());
			System.out.print("정말 품절처리 하시겠습니까 [1.예 2.아니요] => ");
			c = Integer.parseInt(br.readLine());
			if(c == 1) {
				dao.updateSoldOut(code);
			}else {
				return;
			}
			
			System.out.println("메뉴 품절처리 완료");
		} catch (Exception e) {
			System.out.println("품절처리 실패!");
		}
	}
}
