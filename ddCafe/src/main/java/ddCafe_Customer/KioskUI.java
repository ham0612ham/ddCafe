package ddCafe_Customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class KioskUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	List<MenuDTO> shoppingList = null;
	NumberFormat nf = NumberFormat.getCurrencyInstance();
	private KioskDAO dao = new KioskDAOImpl();
	MenuDTO dto = null;
	MemberDTO mdto = null;
	int choice, category_num, menu_num, qty, size_num, price, stampUse_price, v;
	String takeoutTogo, category, menu, size, payment_method;
	
	public int menu() {
		int result=0;
		System.out.println("\nππππππΉπΊππππππ");
		System.out.println("\nπ μ΄μμ€μΈμ π");
		result = choiceTogo();
		return result;
	}
	
	public int choiceTogo() {
		while(true) {
			System.out.println("\nπ λ§€μ₯ or ν¬μ₯ π");
			try {
				do {
					System.out.print("1.λ§€μ₯ 2.ν¬μ₯ => ");
					choice = Integer.parseInt(br.readLine());
					if(choice == 9999) {
						return choice;
					}
					if(choice == 0000) {
						return choice;
					}
				} while(choice<0||choice>2);
				if(choice==1) {takeoutTogo = "λ§€μ₯";}
				else { takeoutTogo = "ν¬μ₯"; };
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			shoppingList = new ArrayList<>();
			dto = new MenuDTO();
			mdto = new MemberDTO();
			int result = selectMenu();
			if(result == 987) {return 987;}
		}
	}

	public int selectMenu() {
		while(true) {
			System.out.println("\nπ μΉ΄νκ³ λ¦¬ π");
			List<String> list5 = dao.showCategory();
			int ch;
			try {
				int n = 1;
				for(String s : list5) {
					System.out.print(n + "." + s + " ");
					n++;
				}
				do {
					System.out.print("[μ΄μ  : 0] => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<0||ch>list5.size());
				if(ch==0) {
					// menu();
					return 567;
				} else {
					System.out.println();
					category_num = ch;
					category = list5.get(ch-1);
				}
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<MenuDTO> list = dao.showMenues(category_num);
			List<String> list2 = dao.showMenues2(category_num);
			List<MenuDTO> bestList = dao.bestMenues();
			MenuDTO dto3 = new MenuDTO();
			MenuDTO dto2 = new MenuDTO();

			System.out.println("\nπ "+category+" π");
			
			try {
				int n = 1;
				if(category_num==6||category_num==7) {
					System.out.println("\nλ©λ΄ / κ°κ²© / μ£Όλ¬Έ");
				} else {
					System.out.println("\nλ©λ΄ / μ¬μ΄μ¦ / κ°κ²© / μ£Όλ¬Έ");
				}
				System.out.println("--------------------------------");
				for(MenuDTO dto : list) {
					System.out.print(n+"."+dto.getMenu()+" / ");
					if(dto.getSize()==null||dto.getSize().equals("null")) {
						System.out.print("");
					} else {
						System.out.print(dto.getSize()+" / ");
					}
					System.out.print(nf.format(dto.getPrice())+" / ");
					if(dto.getStatus().equals("νμ ")) {
						System.out.print("νμ ");
					} else {
						System.out.print("μ£Όλ¬Έκ°λ₯");
					}
					for(MenuDTO dto4 :bestList) {
						if(dto.getMenu().equals(dto4.getMenu())) {
							System.out.print(" / λ² μ€νΈ ");
							System.out.print(dto4.getRank()+"μ");
						} else {
						}
					}
					System.out.println();
					n++;
				}
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
			}
			
			MenuDTO dto = null;
			try {
				int ch2;
				do {
					System.out.print("\nλ©λ΄λ₯Ό κ³¨λΌμ£ΌμΈμ [μ΄μ  : 0] => ");
					ch2 = Integer.parseInt(br.readLine());
					if(ch2==0) { return 567; }
					dto = new MenuDTO();
					dto = list.get(ch2-1);
					if(dto.getStatus().equals("νμ ")) {
						System.out.println("ν΄λΉ λ©λ΄λ νμ μλλ€.");
						ch2 = 0;
					}
				} while(ch2<1||ch2>list.size());
				System.out.print("κ°μλ₯Ό μλ ₯ν΄μ£ΌμΈμ [μ΄μ  : 0] => ");
				qty = Integer.parseInt(br.readLine());
				if(qty==0) { return 567; }
				// μ₯λ°κ΅¬λμ μλ κ²μΈμ§ νμΈ, μμΌλ©΄ κ°μ μΆκ°
				boolean b=true;
				int n = 0;
				for(MenuDTO mdto : shoppingList) {
					if(mdto.getMenu_detail_code()==dto.getMenu_detail_code()) {
						shoppingList.subList(n, n+1).clear();
						qty += mdto.getQty();
						MenuDTO pdto = new MenuDTO();
						pdto.setMenu(mdto.getMenu());
						pdto.setCategory(mdto.getCategory());
						pdto.setQty(qty);
						pdto.setSize(mdto.getSize());
						pdto.setTakeout_togo(mdto.getTakeout_togo());
						pdto.setMenu_detail_code(mdto.getMenu_detail_code());
						pdto.setPrice(mdto.getPrice());
						shoppingList.add(pdto);
						b = false;
						break;
					} else {
						b = true;
					}
					n++;
				}
				
				if(b==true) {
					dto2 = list.get(ch2 - 1);
					menu_num = dto2.getMenu_detail_code();
					menu = list2.get(ch2 - 1);
					size = dto2.getSize();
					price = dto2.getPrice();
					
					dto3.setMenu(menu);
					dto3.setQty(qty);
					dto3.setCategory(category);
					dto3.setMenu_detail_code(menu_num);
					dto3.setTakeout_togo(takeoutTogo);
					dto3.setSize(size);
					dto3.setPrice(price);
					shoppingList.add(dto3);
				}
				int n1 = 1;
				System.out.println("λ©λ΄ μΆκ°κ° μλ£λμμ΅λλ€.");
				System.out.println("\nπ μ₯λ°κ΅¬λ π");
				System.out.println("------------------------");
				for (MenuDTO mdto : shoppingList) {
					System.out.print(n1 + "." + mdto.getMenu() + " / ");
					if(mdto.getSize().equals("null")) {
						System.out.print("μ¬μ΄μ¦ μμ");
					} else {
						System.out.print(mdto.getSize());
					}
					System.out.println(" / " + mdto.getQty() + "κ°");
					n1++;
				}
				System.out.println("μ΄κ°κ²© : " + dao.totalPrice(shoppingList));
				int result = afterchoice();
				if (result == 987) { return 987;}
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
			}
		}
	}

	public int afterchoice() {
		while(true) {
			int ch, result = 0;
			try {
				do {
					System.out.print("\n1.μΆκ°μ£Όλ¬Έ 2.λ©λ΄μ­μ  3.κ°μλ³κ²½ 4.κ²°μ  5.μ₯λ°κ΅¬λ [μ΄μ  : 0] => ");
					ch = Integer.parseInt(br.readLine());
					if(ch==0) {return 987;}
					switch(ch) {
					case 1 : 
						result = selectMenu();
						if(result == 987) {return 987;} break;
					case 2 : 
						result = deleteMenu();
						if(result == 987) {return 987;} break;
					case 3 : 
						result = changeQty(); 
						if(result == 987) {return 987;} break;
					case 4 : 
						result = beforePay(); 
						if(result == 987) {return 987;} break;
					case 5 :
						result = showShoppingList(); 
						if(result == 987) {return 987;} break;
					}
						
				} while(ch < 1||ch > 5);
				
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int showShoppingList() {
		System.out.println("\nπ μ₯λ°κ΅¬λ π");
		System.out.println("------------------------");
		int n1 = 1;
		for (MenuDTO mdto : shoppingList) {
			System.out.print(n1 + "." + mdto.getMenu() + " / ");
			if(mdto.getSize().equals("null")) {
				System.out.print("μ¬μ΄μ¦ μμ");
			} else {
				System.out.print(mdto.getSize());
			}
			System.out.println(" / " + mdto.getQty() + "κ°");
			n1++;
		}
		System.out.println("μ΄κ°κ²© : " + dao.totalPrice(shoppingList));
		return 567;
	}
	
	public int deleteMenu() { // κ³ λ₯Έ λ©λ΄λ₯Ό μ­μ 
		while(true) {
			System.out.println("\nπ λ©λ΄ μ­μ  π");
			System.out.println("------------------------");
			int ch;
			int n1 = 1;
			for (MenuDTO mdto : shoppingList) {
				System.out.print(n1 + "." + mdto.getMenu() + " / ");
				if(mdto.getSize().equals("null")) {
					System.out.print("μ¬μ΄μ¦ μμ");
				} else {
					System.out.print(mdto.getSize());
				}
				System.out.println(" / " + mdto.getQty() + "κ°");
				n1++;
			}
			System.out.println("μ΄κ°κ²© : " + dao.totalPrice(shoppingList));
			try {
				do {
					System.out.print("μ­μ ν  λ©λ΄λ₯Ό μλ ₯ν΄μ£ΌμΈμ [μ΄μ  : 0] => ");
					ch = Integer.parseInt(br.readLine());
					if(ch==0) { return 567; }
				} while (ch<0||ch>shoppingList.size());
				shoppingList.remove(ch-1);
				System.out.println("μ­μ κ° μλ£λμμ΅λλ€");
				int result = afterchoice();
				if(result == 987) {return 987;}
				
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int changeQty() { // κ³ λ₯Έ λ©λ΄μ μ£Όλ¬Έ κ°μλ₯Ό λ°κΏ
		while(true) {
			System.out.println("\nπ κ°μ λ³κ²½ π");
			System.out.println("------------------------");
			int ch, qty, n1 = 1;
			for (MenuDTO mdto : shoppingList) {
				System.out.print(n1 + "." + mdto.getMenu() + " / ");
				if(mdto.getSize().equals("null")) {
					System.out.print("μ¬μ΄μ¦ μμ");
				} else {
					System.out.print(mdto.getSize());
				}
				System.out.println(" / " + mdto.getQty() + "κ°");
				n1++;
			}
			System.out.println("μ΄κ°κ²© : " + dao.totalPrice(shoppingList));
			try {
				do {
					System.out.print("μμ ν  λ©λ΄λ₯Ό μλ ₯ν΄μ£ΌμΈμ [μ΄μ  : 0] => ");
					ch = Integer.parseInt(br.readLine());
					if(ch==0) { return 567; }
				} while (ch<0||ch>shoppingList.size());
				System.out.print("κ° μλ₯Ό μλ ₯ν΄μ£ΌμΈμ [μ΄μ  : 0] => ");
				qty = Integer.parseInt(br.readLine()); // ## λ°κΎΌ κ°μλ‘ μ¬λ£ νλ³΄ κ°λ₯νμ§ μ¬λΆ <- μΆκ°ν΄μΌν¨
				if(qty==0) { return 567; }
				MenuDTO dto = shoppingList.get(ch-1);
				dto.setQty(qty);
				shoppingList.add(dto);
				shoppingList.remove(ch-1);
				System.out.println("\nμμ μ΄ μλ£λμμ΅λλ€");
				for(MenuDTO dto2 : shoppingList) {
					System.out.println(dto2.getMenu()+" / "+dto2.getSize()+" / "+dto2.getQty()+"κ°");
				}
				int result = afterchoice();
				if(result == 987) {return 987;}
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int beforePay() {
		while(true) {
			int ch, result;
			try {
				do {
					System.out.print("ν¬μΈνΈ μ λ¦½ νμκ² μ΅λκΉ? [1.μ/2.μλμ€/3.μ κ·κ°μ/4.μ΄μ ] => ");
					ch = Integer.parseInt(br.readLine());
				} while(ch<1||ch>4);
				if(ch==1) {
					result = point();
					if(result == 987) {return 987;}
				} else if(ch==2){
					result = pay();
					if(result == 987) {return 987;}
				} else if(ch==3) {
					result = add_member();
					if(result == 987) {return 987;}
				} else if(ch==4) {
					return 567;
				}
				
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int point() {
		while(true) {
			String tel;
			String p = "010-\\d{4}-\\d{4}";
			MemberDTO dto2 = new MemberDTO();
			int ch2;
			try {
				do {
					System.out.print("ν΄λμ ν λ²νΈλ₯Ό μλ ₯ν΄μ£ΌμΈμ [μ΄μ  : 0] => ");
					tel = br.readLine();
					if(tel.equals("0")) {
						return 567;
					} else if(!tel.matches(p)) {
						System.out.println("μλ ₯ νμμ΄ μΌμΉνμ§ μμ΅λλ€[010-0000-0000]");
					} 
				} while(!tel.matches(p));
				
				dto2 = dao.findMember(tel);
				if(dto2.getMember_name()!=null) {
					System.out.println("\nνμν©λλ€ " + dto2.getMember_name() + " λ!");
					System.out.println("μ λ¦½λ μ€ν¬ν : " + dao.usableStamp(dto2.getMember_code())+ " κ°");
					System.out.println("μ€ν¬ν 20κ° μ¬μ© μ 3000μμ΄ ν μΈ");
					if(dao.usableStamp(dto2.getMember_code())>=20) {
						do {
							System.out.print("ν¬μΈνΈλ₯Ό μ¬μ©νμκ² μ΅λκΉ? [1.μ/2.μλμ€] => ");
							ch2 = Integer.parseInt(br.readLine());
						} while(ch2<1||ch2>2);
						stampUse_price = ch2==1 ? 3000 : 0 ;
					}
					mdto = dto2;
					int result = pay();
					if(result == 987) {return 987;}
				} else {
					System.out.println("μ‘΄μ¬νμ§ μλ νμμλλ€.");
					return 567;
				}
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int add_member() {
		while(true) {
			System.out.println("\nπ νμκ°μ π");
			String name, tel, ch;
			int result;
			boolean b = true;
			String p = "010-\\d{4}-\\d{4}";
			try {
				do {
					do {
					System.out.print("μ΄λ¦ or λ³λͺμ μλ ₯ν΄μ£ΌμΈμ => ");
					name = br.readLine();
					if(name.length()>=8) {
						System.out.println("8μ μ΄νλ‘ μλ ₯ν΄μ£ΌμΈμ.");
					}
					} while(!(name.length()>0&&name.length()<9));
					System.out.print("ν΄λν°λ²νΈλ₯Ό μλ ₯ν΄μ£ΌμΈμ [010-0000-0000] => ");
					tel = br.readLine();
					if(!tel.matches(p)) {
						System.out.println("μλ ₯ νμμ΄ μΌμΉνμ§ μμ΅λλ€[010-0000-0000]");
						System.out.print("λ€μ μλνμκ² μ΅λκΉ? [1.μ/2.μλμ€] => ");
						ch = br.readLine();
						if(ch.equals("2")) {
							return 567;
						} else {
							b = true;
						}
					} else {
						b = false;
					}
				} while(b);
				result = dao.addMember(name, tel);
				if(result==0) {
					System.out.println("νμ λ±λ‘μ΄ μ€ν¨λμ΅λλ€.");
					System.out.println("λ©λ΄λ‘ λμκ°λλ€.");
					return 567;
				}
				System.out.println(name+" λ νμ λ±λ‘λμμ΅λλ€.");
				return 567;
			} catch (MyDuplicationException e) {
				System.out.println("μ΄λ―Έ λ±λ‘λ λ²νΈμλλ€.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int pay() {
		while (true) {
			System.out.println("\nπ κ²°μ  π");
			List<String> list = new ArrayList<>();
			int ch;
			
			try {
				list = dao.showPaymentMethod();
				int n = 1;
				for (String s : list) {
					System.out.print(n + "." + s + " ");
					n++;
				}
				System.out.println();
				do {
					System.out.print("κ²°μ  μλ¨μ μλ ₯ν΄μ£ΌμΈμ => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > list.size());
				payment_method = list.get(ch - 1);
				for (MenuDTO mdto : shoppingList) {
					System.out.print(mdto.getMenu() + " / ");
					if(mdto.getSize().equals("null")) {
						System.out.print("μ¬μ΄μ¦ μμ");
					} else {
						System.out.print(mdto.getSize());
					}
					System.out.println(" / " + mdto.getQty() + "κ°");
				}
				System.out.println("μ΄κ°κ²© : " + dao.totalPrice(shoppingList));
				if (stampUse_price > 0) {
					System.out.println("ν μΈκΈμ‘ : " + stampUse_price);
					if(dao.totalPrice(shoppingList) - stampUse_price<0) {
						System.out.println("κ²°μ  κΈμ‘ : " + 0);
					} else {
						System.out.println("κ²°μ  κΈμ‘ : " + (dao.totalPrice(shoppingList) - stampUse_price));
					}
				}
				do {
					System.out.print("κ²°μ νμκ² μ΅λκΉ? [1.μ/2.μλμ€] => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 2);
				if (ch == 2) {
					return 987;
				}
				boolean b = dao.calculateMenu(shoppingList);
				if(b == true) {
					System.out.println("μ£μ‘ν©λλ€. μ£Όλ¬Έμ΄ μ¬λ£μλλ³΄λ€ λ§μ μ£Όλ¬Έμ΄ λΆκ°ν©λλ€.");
					return 567;
				}
				int result = dao.orderMenues(shoppingList, takeoutTogo, mdto.getMember_code(), payment_method,
						stampUse_price);
				if (result == 0) {
					System.out.println("κ²°μ κ° μ€ν¨λμμ΅λλ€.");
					return 567;
				}
				System.out.println("κ²°μ κ° μλ£λμμ΅λλ€.");
				return 987;
				
			} catch (NumberFormatException e) {
				System.out.println("μ«μλ₯Ό μλ ₯ν΄μ£ΌμΈμ");
			} catch (Exception e) {
			}
		}
	}
}
