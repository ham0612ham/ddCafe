package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import db.util.DBConn;

public class IngredientUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private IngredientDAO dao = new IngredientImpl();

	public void menu() {
		System.out.println("\n✦ 재료 메뉴 ✦");

		int ch;

		while (true) {
			try {
				do {
					System.out.print("\n1.재고확인 2.재료추가주문 3.입고내역 4.새로운재료추가 5.재고삭제 6.납품업체확인 7.종료 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 7);

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

	public void check_ingredient() { // 완료, 재료를 5개씩 보이게 출력싶다
		System.out.println("\n✦ 재료 확인 ︎✦");

		List<IngredientDTO> list = dao.leftingredient();

		for (IngredientDTO dto : list) {

			System.out.print(dto.getIngredient_code()+ ". ");
			System.out.print(dto.getIngredient_name() + "\t");
			System.out.println(dto.getIngredient_qty());

		}

		System.out.println();

	}

	public void add_ingredietn() {
		System.out.println("\n✦ 재료 추가 주문 ︎✦");

		try {
			IngredientDTO dto = new IngredientDTO();
			
			System.out.print("주문 날짜 ( 오늘 날짜? ) ? ");
			dto.setReceiving_date(br.readLine());
			
			System.out.print("주문 수량 ? ");
			dto.setReceiving_qty(Integer.parseInt(br.readLine()));

			System.out.print("주문 단가 ? ");
			dto.setReceiving_price(Integer.parseInt(br.readLine()));

			System.out.print("재료 이름 ? ");
			dto.setIngredient_name(br.readLine());

			System.out.print("납품업체코드 ? ");
			dto.setVendor_code(Integer.parseInt(br.readLine()));
			
			System.out.print("재료코드");
			dto.setIngredient_code(Integer.parseInt(br.readLine()));
			
			//dao.add_ingredient(dto);
			
			System.out.println("재료가 주문 돼었습니다.");
			
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
		System.out.println("입고날짜\t\t재료이름\t재료수량");
		System.out.println("-----------------------------------------------");

		List<IngredientDTO> list = dao.receiving_history();
		
		for(IngredientDTO dto : list) {
			System.out.print(dto.getReceiving_date()+"\t");
			System.out.print(dto.getIngredient_name()+"\t");
			System.out.println(dto.getReceiving_qty()+"\t");
		}
		System.out.println();
	}

	public void new_ingredient() { // 완료
		System.out.println("\n✦ 새로운 재료 추가 ︎✦");
		String ingredient;
		int result;
		try {
			System.out.println("재료 이름 => ");
			ingredient = br.readLine();
			result = dao.new_ingredient(ingredient, 0);
			if(result==0) {
				System.out.println("재료 등록이 실패됐습니다.");
				System.out.println("메뉴로 돌아갑니다.");
				menu();
			}
			System.out.println(ingredient+"가 등록 되었습니다.");
			menu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete_ingredient() {
		System.out.println("\n✦ 재고 삭제 ︎✦");
		
		try {
			IngredientDTO dto = new IngredientDTO();
			
			System.out.print("재료코드 ? ");
			dto.setIngredient_code(Integer.parseInt(br.readLine()));
			
			System.out.print("폐기날짜 ? ");
			dto.setTrash_date(br.readLine());
			
			System.out.print("재료수량 ? ");
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
			System.out.print(dto.getVendor_name() + "\t");
			System.out.print(dto.getManager_name() + "\t");
			System.out.println(dto.getManager_tel());
		}

		System.out.println();

	}
}
