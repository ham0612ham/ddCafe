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
					System.out.print("\n1.재고확인 2.재료추가주문 3.입고내역 4.새로운재료추가 5.재고삭제 6.납품업체확인 7.뒤로가기 => ");
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
			int ch, chc ;
			//////////////////////////////////String name = null;
			
			do {
				System.out.print("\n 추가할 재료를 입력하세요. [뒤로가기 : 0]  ");
				ch = Integer.parseInt(br.readLine());
				if(ch==0) {return;}
			} while(ch<1||ch>list.size());
			
			System.out.print("\n 개수를 입력하세요. [뒤로가기 : 0]  ");
			qty = Integer.parseInt(br.readLine());
			
			if(qty==0) {return;}
			
			IngredientDTO dto = new IngredientDTO();

			dto = list.get(ch - 1);  //납품업체코드, 재료코드랑 주문단가는 정해짐,,,
			
			dto.setIngredient_code(ch -1); 
			dto.setReceiving_qty(qty);
			///////////////////////////////dto.setIngredient_name(name);
			
			do {
				System.out.println();
				System.out.print("\n "+ dto.getIngredient_name() + "(을)를 " + qty + "개 추가주문하시겠습니까? [1.예/2.아니오] =>  ");
				chc = Integer.parseInt(br.readLine());
			} while (chc<0 || chc>2);
				
			if(chc==1) {
				int result = dao.add_ingredient(dto);
				System.out.println("\n 재료가 추가 되었습니다.");
				
			} else if(chc==2) {
				System.out.println("\n 추가 주문을 취소했습니다.");
				return;
			}
			
			
			return; 
			
		} catch (NumberFormatException e) {
			System.out.println("숫자만 입력 가능합니다.");
		} catch (Exception e) {
			System.out.println("데이터 등록이 실패했습니다.");
		}
		
		
		System.out.println();
		
	}

	public void receiving_history() { 
		System.out.println("\n✦ 입고 내역 ︎✦");
		
		System.out.println();
		System.out.println("\n  입고날짜    / 재료이름 / 재료수량");
		System.out.println("-----------------------------------------------");

		List<IngredientDTO> list = dao.receiving_history();
		
		for(IngredientDTO dto : list) {
			System.out.print(dto.getReceiving_date()+" / ");
			System.out.print(dto.getIngredient_name()+" / ");
			System.out.println(dto.getReceiving_qty());
		}
		System.out.println();
	}

	public void new_ingredient() { 
		System.out.println("\n✦ 새로운 재료 추가 ︎✦");
		String newingredient;
		int ans;
		
		try {
			
			// IngredientDTO dto = dao.leftingredient();
			
		
			
			do {
			System.out.println("추가 할 재료 이름 => ");
			newingredient = br.readLine();
			if (newingredient == dto.getIngredient_name() ) {
				System.out.println(" 이미 있는 재료입니다. 메뉴로 돌아갑니다.");
				return;
			}
			
			
			System.out.println(dto.getIngredient_name());
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
				return; //menu()
			}
			System.out.println(newingredient+"(이)가 등록 되었습니다.");
			return; //menu();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete_ingredient() {
		System.out.println("\n✦ 재고 삭제 ︎✦");
		List<IngredientDTO> list = dao.trash_ingredientcode();
		
		
		for (IngredientDTO dto : list) {
			
			System.out.print(dto.getIngredient_code()+ ".");
			System.out.print(dto.getIngredient_name()+ "/");
			System.out.println(dto.getIngredient_qty());
			
			
		}
		
		try {
			int ic, icq, ch;
			IngredientDTO dto = new IngredientDTO();
			
			System.out.print("\n 폐기할 자료를 입력하세요. => ");
			ic =Integer.parseInt(br.readLine());
			
			dto = list.get(ic -1);
			
		//	dto.setIngredient_code(ic -1);     -오류의 주범
			
			do {
				System.out.print("\n " + dto.getIngredient_name() + " 개수를 입력하세요. => ");
				icq = Integer.parseInt(br.readLine());
				if (icq >= 1 && icq <= dto.getIngredient_qty()) {
					dto.setTrash_qty(icq);
				} else { 
					System.out.println("\n 재료 수량보다 많습니다. 다시 입력하세요. => ");
				}
				
			} while (icq > dto.getIngredient_qty());
			
				

			
			System.out.print("\n 폐기 이유 (선택) => " );
			dto.setRemark(br.readLine());

			
			do {
				System.out.println();
				System.out.print("\n "+ dto.getIngredient_name() + "(을)를 " + dto.getTrash_qty()+ "개 폐기하겠습니까? [1.예/2.아니오] =>  ");
				ch = Integer.parseInt(br.readLine());
			} while (ch<0 || ch>2);
				
			if(ch==1) {
				dao.sub_ingredient(dto);
				System.out.println("\n 재료가 폐기되었습니다.");
				
			} else if(ch==2) {
				System.out.println("\n 폐기를 취소했습니다.");
				return;
			}
			
		} catch (NumberFormatException e) {
			System.out.println("숫자만 입력 가능합니다.");
		} catch (Exception e) {
			System.out.println("데이터 등록이 실패했습니다.");
		}
		System.out.println();
		
	}
		
	public void check_vendor() { 
		System.out.println("\n✦ 납품업체 확인 ︎✦");

		List<IngredientDTO> list = dao.vendorList();

		for (IngredientDTO dto : list) {
			System.out.print(dto.getVendor_code() + ".");
			System.out.print(dto.getVendor_name() + " / ");
			System.out.print(dto.getManager_name() + " / ");
			System.out.println(dto.getManager_tel());
		}

		System.out.println();

	}
}
