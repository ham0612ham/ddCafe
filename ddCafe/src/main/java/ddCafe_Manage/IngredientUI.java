package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import db.util.DBConn;

public class IngredientUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private IngredientDAO dao = new IngredientImpl();
	int ingredient_code, qty, menu;

	
	public void menu() {
		System.out.println("\n✦ 재료 메뉴 ✦");

		int ch;

		while (true) {
			try {
				do {
					System.out.print("\n1.재고확인 2.재료추가주문 3.입고내역 4.새로운재료추가 5.재고삭제 6.납품업체확인 7.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1||ch > 7);

				if (ch == 7) {
					System.out.println();
					DBConn.close();
					return;
				}

				switch (ch) {
				case 1:	check_ingredient();	break;
				case 2:	add_ingredietn();	break;
				case 3:	receiving_history();break;
				case 4:	new_ingredient();	break;
				case 5:	delete_ingredient();break;
				case 6:	check_vendor();		break;
				}
			} catch (Exception e) {
			}
		}
	}

	public void check_ingredient() {
		System.out.println("\n✦ 재료 확인 ︎✦");

		List<IngredientDTO> list = dao.leftingredient();

		for (IngredientDTO dto : list) {

			System.out.print(dto.getIngredient_code()+ ".");
			System.out.print(dto.getIngredient_name() + " / ");
			System.out.println(dto.getIngredient_qty());

		}

		System.out.println();

	}

	public void add_ingredietn() {
		
		System.out.println("\n✦ 재료 추가 주문 ︎✦");
		List<IngredientDTO> list = dao.show_orderlist();
		
		for (IngredientDTO dto : list) {
			
			System.out.print(dto.getIngredient_code()+ ".");
			System.out.print(dto.getIngredient_name()+ " / ");
			System.out.print(dto.getReceiving_price()+ " / ");
			System.out.print(dto.getVendor_code()+ ".");
			System.out.print(dto.getVendor_name()+ " / ");
			System.out.print(dto.getManager_name()+ " / ");
			System.out.println(dto.getManager_tel());
			
			
		}
		
		try {
			int ch;
			do {
				System.out.print("\n 추가할 재료 코드를 골라주세요. [뒤로가기 : 0]  ");
				ch = Integer.parseInt(br.readLine());
				if(ch==0) {menu();}
			} while(ch<1||ch>list.size());
			System.out.print("\n 개수를 입력해주세요. [뒤로가기 : 0]  ");
			qty = Integer.parseInt(br.readLine());
			if(qty==0) {menu();}
			
			IngredientDTO dto = new IngredientDTO();

			dto = list.get(ch - 1);  //납품업체코드, 재료코드랑 주문단가는 정해짐,,,
			
			dto.setIngredient_code(ch -1); 
			dto.setReceiving_qty(qty);
			
			// 이 제품을 ㅇ여기에서 이마만큼 주문하시겠습니까? y예 주문하도록 하면 102번 실행 주문ㅇ안하면 빠져나오게,,,
		
			int result = dao.add_ingredient(dto);
			
			System.out.println("\n 재료가 주문 되었습니다.");
			
			menu();
			
		} catch (NumberFormatException e) {
			System.out.println("숫자만 입력 가능합니다.");
		} catch (Exception e) {
			System.out.println("데이터 등록이 실패했습니다.");
		}
		
		
		System.out.println();
		
	}

	public void receiving_history() { // 완료
		System.out.println("\n✦ 입고 내역 ︎✦");
		
		System.out.println();
		System.out.println("\n입고날짜\t / 재료이름 / 재료수량");
		System.out.println("-----------------------------------------------");

		List<IngredientDTO> list = dao.receiving_history();
		
		for(IngredientDTO dto : list) {
			System.out.print(dto.getReceiving_date()+" / ");
			System.out.print(dto.getIngredient_name()+" / ");
			System.out.println(dto.getReceiving_qty());
		}
		System.out.println();
	}

	public void new_ingredient() { // 완료
		System.out.println("\n✦ 새로운 재료 추가 ︎✦");
		String newingredient;
		int ans;
		
		try {
			
			do {
			System.out.println("추가 할 재료 이름 => ");
			newingredient = br.readLine();
			System.out.print(newingredient + "(을)를 추가하시겠습니까? [1.예/2.아니오] => ");
			ans = Integer.parseInt(br.readLine());
			} while(ans<1||ans>2);

			if(ans==2) {
				System.out.println("재료 등록 취소. 메뉴로 돌아갑니다.");
				return;
			} 
			
			int result = dao.new_ingredient(newingredient, 0);
		
			if(result==0) {
				System.out.println("재료 등록 실패. 메뉴로 돌아갑니다.");
				menu();
			}
			System.out.println(newingredient+"(이)가 등록 되었습니다.");
			menu();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete_ingredient() {
		System.out.println("\n✦ 재고 삭제 ︎✦");
		List<IngredientDTO> list = dao.trash_ingredientcode();
		
		for (IngredientDTO dto : list) {
			
			System.out.print(dto.getIngredient_code()+ ".");
			System.out.println(dto.getIngredient_name());
			
			
		}
		
		try {
			IngredientDTO dto = new IngredientDTO();
			
			System.out.print("재료코드 ? ");
			dto.setIngredient_code(Integer.parseInt(br.readLine()));
			
			System.out.print("버릴수량 ? ");
			dto.setTrash_qty(Integer.parseInt(br.readLine()));
			
			System.out.print("비고 " );
			dto.setRemark(br.readLine());
			
			dao.sub_ingredient(dto);
			
			System.out.println("재료가 폐기돼었습니다.");
			
		} catch (NumberFormatException e) {
			System.out.println("숫자만 입력 가능합니다.");
		} catch (Exception e) {
			System.out.println("데이터 등록이 실패했습니다.");
		}
		System.out.println();
		
	}
		
	public void check_vendor() { // 완료
		System.out.println("\n✦ 납품업체 확인 ︎✦");

		List<IngredientDTO> list = dao.vendorList();

		for (IngredientDTO dto : list) {
			System.out.print(dto.getVendor_name() + " / ");
			System.out.print(dto.getManager_name() + " / ");
			System.out.println(dto.getManager_tel());
		}

		System.out.println();

	}
}
