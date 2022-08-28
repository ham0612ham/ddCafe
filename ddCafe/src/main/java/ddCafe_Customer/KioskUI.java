package ddCafe_Customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.util.DBConn;

public class KioskUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	List<MenuDTO> shoppingList = new ArrayList<>();
	NumberFormat nf = NumberFormat.getCurrencyInstance();
	private KioskDAO dao = new KioskDAOImpl();
	MenuDTO dto = new MenuDTO();
	int choice, category_num, menu_num, qty, size_num, price;
	String takeoutTogo, category, menu, size;
	
	public void menu() {
		System.out.println("\n𓂃𓂃𓂃𓂃𓊝𓄹𓄺𓂃𓂃𓆞𓂃𓂃𓂃");
		System.out.println("\n🜚 어서오세요 🜚");
		
		choiceTogo();
		choiceCategory();
	}
	
	public void choiceTogo() {
		System.out.println("\n🜚 매장 or 포장 🜚");
		
		try {
			do {
				System.out.print("1.매장 2.포장 => ");
				choice = Integer.parseInt(br.readLine());
			} while(choice<0||choice>2);
			if(choice==1) {takeoutTogo = "매장";}
			else { takeoutTogo = "포장"; };
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void choiceCategory() {
		System.out.println("\n🜚 카테고리 🜚");
		List<String> list = dao.showCategory();
		int ch;
		try {
			int n = 1;
			for(String s : list) {
				System.out.print(n + "." + s + " ");
				n++;
			}
			do {
				System.out.print("[종료 : 0] => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch<0||ch>list.size());
			if(ch==0) {
				// menu();
				return;
			}
			System.out.println();
			category_num = ch;
			category = list.get(ch-1);
			
			showMenues();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showMenues() {
		System.out.println("\n🜚 "+category+" 🜚");
		List<MenuDTO> list = dao.showMenues(category_num);
		
		try {
			int n = 1;
			System.out.println("\n메뉴 / 사이즈 / 가격");
			for(MenuDTO dto : list) {
				System.out.print(n+"."+dto.getMenu()+" / ");
				System.out.print(dto.getSize()+" / ");
				System.out.print(nf.format(dto.getPrice())+" / ");
				if(dto.getStatus()==null) {
					System.out.println("주문가능");
				} else {
					System.out.println("품절");
				}
				n++;
			}
			selectMenu();
		} catch (Exception e) {
		}
	}
	
	public void selectMenu() {
		List<MenuDTO> list = dao.showMenues(category_num);
		List<String> list2 = dao.showMenues2(category_num);
		MenuDTO dto = new MenuDTO();
		MenuDTO dto2 = new MenuDTO();
		
		try {
			int ch;
			do {
				System.out.print("\n메뉴를 골라주세요 [카테고리 : 0] => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch<1||ch>list.size());
			System.out.print("개수를 입력해주세요 [카테고리 : 0] => ");
			qty = Integer.parseInt(br.readLine()); // 재료의개수가 충분한지 여부를 확인할 수 있게 해야함
			
			if(ch==0||qty==0) {
				choiceCategory();
			}
			dto2 = list.get(ch - 1);
			// System.out.println("\n🜚 "+dto2.getMenu()+" / "+dto2.getSize()+" / "+qty+"개 🜚");
			menu_num = dto2.getMenu_detail_code();
			menu = list2.get(ch - 1);
			size = dto2.getSize();
			price = dto2.getPrice();

			dto.setMenu(menu);
			dto.setQty(qty);
			dto.setSize(category);
			dto.setMenu_detail_code(menu_num);
			dto.setTakeout_togo(takeoutTogo);
			dto.setSize(size);
			dto.setPrice(price);
			shoppingList.add(dto);
			
			System.out.println("메뉴 추가가 완료되었습니다.");
			
			showShoppingBag();
		} catch (Exception e) {
		}
	}
	
	public void showShoppingBag() {
		System.out.println("\n🜚 장바구니 🜚");
		for(MenuDTO dto : shoppingList) {
			System.out.println(dto.getMenu()+" / "+dto.getSize()+" / "+dto.getQty()+"개");
		}
		System.out.println("총가격 : " + dao.totalPrice(shoppingList));
		afterchoice();
	}
	
	public void afterchoice() {
		int ch;
		try {
			do {
				System.out.print("\n1.추가주문 2.메뉴삭제 3.개수변경 4.결제 5.종료 => ");
				ch = Integer.parseInt(br.readLine());
				if(ch==5) {return;}
				switch(ch) {
				case 1 : choiceCategory(); break;
				case 2 : deleteMenu(); break;
				case 3 : changeQty(); break;
				case 4 : beforePay(); break;
				}
			} while(ch < 1||ch > 5);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteMenu() { // 고른 메뉴를 삭제
		System.out.println("\n🜚 메뉴 삭제 🜚");
		int ch;
		int n = 1;
		for(MenuDTO dto : shoppingList) {
			System.out.println(n +". "+ dto.getMenu()+" / "+dto.getSize()+" / "+dto.getQty()+"개");
			n++;
		}
		try {
			do {
				System.out.print("삭제할 메뉴 [카테고리 : 0] => ");
				ch = Integer.parseInt(br.readLine());
				if(ch==0) {choiceCategory();}
			} while (ch<0||ch>shoppingList.size());
			shoppingList.remove(ch-1);
			System.out.println("삭제가 완료되었습니다");
			afterchoice();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeQty() { // 고른 메뉴의 주문 개수를 바꿈
		System.out.println("\n🜚 개수 변경 🜚");
		int ch, qty;
		int n = 1;
		for(MenuDTO dto : shoppingList) {
			System.out.println(n +". "+ dto.getMenu()+" / "+dto.getSize()+" / "+dto.getQty()+"개");
			n++;
		}
		try {
			do {
				System.out.print("수정할 메뉴 [카테고리 : 0] => ");
				ch = Integer.parseInt(br.readLine());
				if(ch==0) {choiceCategory();}
			} while (ch<0||ch>shoppingList.size());
			System.out.print("개 수 [카테고리 : 0] => ");
			qty = Integer.parseInt(br.readLine()); // ## 바꾼 개수로 재료 확보 가능한지 여부 <- 추가해야함
			if(qty==0) {choiceCategory();}
			MenuDTO dto = shoppingList.get(ch-1);
			dto.setQty(qty);
			shoppingList.add(dto);
			shoppingList.remove(ch-1);
			System.out.println("수정이 완료되었습니다");
			for(MenuDTO dto2 : shoppingList) {
				System.out.println(dto2.getMenu()+" / "+dto2.getSize()+" / "+dto2.getQty()+"개");
			}
			afterchoice();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void beforePay() {
		int ch;
		try {
			do {
				System.out.print("포인트 적립 하시겠습니까? [1.예/2.아니오/3.신규가입] => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch<1||ch>3);
			if(ch==1) {
				point();
			} else if(ch==2){
				pay();
			} else if(ch==3) {
				add_member();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void point() {
		String tel;
		String p = "010-\\d{4}-\\d{4}";
		MemberDTO dto = new MemberDTO();
		try {
			do {
				System.out.print("전화번호[종료 : 0] => ");
				tel = br.readLine();
				if(tel.equals("0")) beforePay();
				if(!tel.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
				} 
			} while(!tel.matches(p));
			
			dto = dao.findMember(tel);
			if(dto.getMember_name()==null) {
				System.out.println("존재하지 않는 회원입니다.");
				beforePay();
			}
			System.out.println("환영합니다 " + dto.getMember_name() + " 님!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void add_member() {
		System.out.println("\n🜚 회원가입 🜚");
		String name, tel;
		int result;
		String p = "010-\\d{4}-\\d{4}";
		try {
			System.out.println("이름 or 별명 => ");
			name = br.readLine();
			System.out.println("휴대폰번호[010-0000-0000] => ");
			tel = br.readLine();
			if(!tel.matches(p)) {
				System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
			} 
			result = dao.addMember(name, tel);
			if(result==0) {
				System.out.println("회원 등록이 실패됐습니다.");
				System.out.println("메뉴로 돌아갑니다.");
				afterchoice();
			}
			System.out.println(name+" 님 회원 등록되었습니다.");
			beforePay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pay() {
		System.out.println("\n🜚 결제 🜚");
		
	}
}
