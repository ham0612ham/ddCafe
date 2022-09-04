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
		System.out.println("\n𓂃𓂃𓂃𓂃𓊝𓄹𓄺𓂃𓂃𓆞𓂃𓂃𓂃");
		System.out.println("\n🜚 어서오세요 🜚");
		result = choiceTogo();
		return result;
	}
	
	public int choiceTogo() {
		while(true) {
			System.out.println("\n🜚 매장 or 포장 🜚");
			try {
				do {
					System.out.print("1.매장 2.포장 => ");
					choice = Integer.parseInt(br.readLine());
					if(choice == 9999) {
						return choice;
					}
					if(choice == 0000) {
						return choice;
					}
				} while(choice<0||choice>2);
				if(choice==1) {takeoutTogo = "매장";}
				else { takeoutTogo = "포장"; };
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
			System.out.println("\n🜚 카테고리 🜚");
			List<String> list5 = dao.showCategory();
			int ch;
			try {
				int n = 1;
				for(String s : list5) {
					System.out.print(n + "." + s + " ");
					n++;
				}
				do {
					System.out.print("[이전 : 0] => ");
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
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<MenuDTO> list = dao.showMenues(category_num);
			List<String> list2 = dao.showMenues2(category_num);
			List<MenuDTO> bestList = dao.bestMenues();
			MenuDTO dto3 = new MenuDTO();
			MenuDTO dto2 = new MenuDTO();

			System.out.println("\n🜚 "+category+" 🜚");
			List<MenuDTO> list3 = dao.showMenues(category_num);
			
			try {
				int n = 1;
				if(category_num==6||category_num==7) {
					System.out.println("\n메뉴 / 가격 / 주문");
				} else {
					System.out.println("\n메뉴 / 사이즈 / 가격 / 주문");
				}
				System.out.println("--------------------------------");
				for(MenuDTO dto : list3) {
					System.out.print(n+"."+dto.getMenu()+" / ");
					if(dto.getSize()==null||dto.getSize().equals("null")) {
						System.out.print("");
					} else {
						System.out.print(dto.getSize()+" / ");
					}
					System.out.print(nf.format(dto.getPrice())+" / ");
					if(dto.getStatus().equals("품절")) {
						System.out.print("품절");
					} else {
						System.out.print("주문가능");
					}
					for(MenuDTO dto4 :bestList) {
						if(dto.getMenu().equals(dto4.getMenu())) {
							System.out.print(" / 베스트 ");
							System.out.print(dto4.getRank()+"위");
						} else {
						}
					}
					System.out.println();
					n++;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
			}
			
			MenuDTO dto = null;
			try {
				int ch2;
				do {
					System.out.print("\n메뉴를 골라주세요 [이전 : 0] => ");
					ch2 = Integer.parseInt(br.readLine());
					if(ch2==0) { return 567; }
					dto = new MenuDTO();
					dto = list.get(ch2-1);
					if(dto.getStatus().equals("품절")) {
						System.out.println("해당 메뉴는 품절입니다.");
						ch2 = 0;
					}
				} while(ch2<1||ch2>list.size());
				System.out.print("개수를 입력해주세요 [이전 : 0] => ");
				qty = Integer.parseInt(br.readLine());
				if(qty==0) { return 567; }
				// 장바구니에 있는 것인지 확인, 있으면 개수 추가
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
					
				System.out.println("메뉴 추가가 완료되었습니다.");
				System.out.println("\n🜚 장바구니 🜚");
				System.out.println("------------------------");
				for (MenuDTO mdto : shoppingList) {
					System.out.println(mdto.getMenu() + " / " + mdto.getSize() + " / " + mdto.getQty() + "개");
				}
				System.out.println("총가격 : " + dao.totalPrice(shoppingList));
				int result = afterchoice();
				if (result == 987) { return 987;}
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
			}
		}
	}

	public int afterchoice() {
		while(true) {
			int ch, result = 0;
			try {
				do {
					System.out.print("\n1.추가주문 2.메뉴삭제 3.개수변경 4.결제 5.장바구니 [이전 : 0] => ");
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
				System.out.println("숫자를 입력해주세요.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int showShoppingList() {
		int n = 1;
		System.out.println("\n🜚 장바구니 🜚");
		System.out.println("------------------------");
		for(MenuDTO dto : shoppingList) {
			System.out.println(n +". "+ dto.getMenu()+" / "+dto.getSize()+" / "+dto.getQty()+"개");
			n++;
		}
		System.out.println("총가격 : " + dao.totalPrice(shoppingList));
		return 567;
	}
	
	public int deleteMenu() { // 고른 메뉴를 삭제
		while(true) {
			System.out.println("\n🜚 메뉴 삭제 🜚");
			System.out.println("------------------------");
			int ch;
			int n = 1;
			for(MenuDTO dto : shoppingList) {
				System.out.println(n +". "+ dto.getMenu()+" / "+dto.getSize()+" / "+dto.getQty()+"개");
				n++;
			}
			System.out.println("총가격 : " + dao.totalPrice(shoppingList));
			try {
				do {
					System.out.print("삭제할 메뉴를 입력해주세요 [이전 : 0] => ");
					ch = Integer.parseInt(br.readLine());
					if(ch==0) { return 567; }
				} while (ch<0||ch>shoppingList.size());
				shoppingList.remove(ch-1);
				System.out.println("삭제가 완료되었습니다");
				int result = afterchoice();
				if(result == 987) {return 987;}
				
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int changeQty() { // 고른 메뉴의 주문 개수를 바꿈
		while(true) {
			System.out.println("\n🜚 개수 변경 🜚");
			System.out.println("------------------------");
			int ch, qty;
			int n = 1;
			for(MenuDTO dto : shoppingList) {
				System.out.println(n +". "+ dto.getMenu()+" / "+dto.getSize()+" / "+dto.getQty()+"개");
				n++;
			}
			System.out.println("총가격 : " + dao.totalPrice(shoppingList));
			try {
				do {
					System.out.print("수정할 메뉴를 입력해주세요 [이전 : 0] => ");
					ch = Integer.parseInt(br.readLine());
					if(ch==0) { return 567; }
				} while (ch<0||ch>shoppingList.size());
				System.out.print("개 수를 입력해주세요 [이전 : 0] => ");
				qty = Integer.parseInt(br.readLine()); // ## 바꾼 개수로 재료 확보 가능한지 여부 <- 추가해야함
				if(qty==0) { return 567; }
				MenuDTO dto = shoppingList.get(ch-1);
				dto.setQty(qty);
				shoppingList.add(dto);
				shoppingList.remove(ch-1);
				System.out.println("\n수정이 완료되었습니다");
				for(MenuDTO dto2 : shoppingList) {
					System.out.println(dto2.getMenu()+" / "+dto2.getSize()+" / "+dto2.getQty()+"개");
				}
				int result = afterchoice();
				if(result == 987) {return 987;}
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int beforePay() {
		while(true) {
			int ch, result;
			try {
				boolean b = dao.calculateMenu(shoppingList);
				if(b == true) {
					System.out.println("죄송합니다. 주문이 재료수량보다 많아 주문이 불가합니다.");
					return 567;
				}
				do {
					System.out.print("포인트 적립 하시겠습니까? [1.예/2.아니오/3.신규가입/4.이전] => ");
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
				System.out.println("숫자를 입력해주세요");
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
					System.out.print("휴대전화 번호를 입력해주세요 [이전 : 0] => ");
					tel = br.readLine();
					if(tel.equals("0")) {
						return 567;
					} else if(!tel.matches(p)) {
						System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
					} 
				} while(!tel.matches(p));
				
				dto2 = dao.findMember(tel);
				if(dto2.getMember_name()!=null) {
					System.out.println("\n환영합니다 " + dto2.getMember_name() + " 님!");
					System.out.println("적립된 스탬프 : " + dao.usableStamp(dto2.getMember_code())+ " 개");
					System.out.println("스탬프 20개 사용 시 3000원이 할인");
					if(dao.usableStamp(dto2.getMember_code())>=20) {
						do {
							System.out.println("포인트를 사용하시겠습니까? [1.예/2.아니오] => ");
							ch2 = Integer.parseInt(br.readLine());
						} while(ch2<1||ch2>2);
						stampUse_price = ch2==1 ? 3000 : 0 ;
					}
					mdto = dto2;
					int result = pay();
					if(result == 987) {return 987;}
				} else {
					System.out.println("존재하지 않는 회원입니다.");
					return 567;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int add_member() {
		while(true) {
			System.out.println("\n🜚 회원가입 🜚");
			String name, tel, ch;
			int result;
			boolean b = true;
			String p = "010-\\d{4}-\\d{4}";
			try {
				do {
					do {
					System.out.print("이름 or 별명을 입력해주세요 => ");
					name = br.readLine();
					if(name.length()>=8) {
						System.out.println("8자 이하로 입력해주세요.");
					}
					} while(!(name.length()>0&&name.length()<9));
					System.out.print("휴대폰번호를 입력해주세요 [010-0000-0000] => ");
					tel = br.readLine();
					if(!tel.matches(p)) {
						System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
						System.out.print("다시 시도하시겠습니까? [1.예/2.아니오] => ");
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
					System.out.println("회원 등록이 실패됐습니다.");
					System.out.println("메뉴로 돌아갑니다.");
					return 567;
				}
				System.out.println(name+" 님 회원 등록되었습니다.");
				return 567;
			} catch (MyDuplicationException e) {
				System.out.println("이미 등록된 번호입니다.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int pay() {
		while (true) {
			System.out.println("\n🜚 결제 🜚");
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
					System.out.print("결제 수단을 입력해주세요 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > list.size());
				payment_method = list.get(ch - 1);
				for (MenuDTO dto : shoppingList) {
					System.out.println(dto.getMenu() + " / " + dto.getSize() + " / " + dto.getQty() + "개");
				}
				System.out.println("총가격 : " + dao.totalPrice(shoppingList));
				if (stampUse_price > 0) {
					System.out.println("할인금액 : " + stampUse_price);
					System.out.println("결제 금액 : " + (dao.totalPrice(shoppingList) - stampUse_price));
				}
				do {
					System.out.print("결제하시겠습니까? [1.예/2.아니오] => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 2);
				if (ch == 2) {
					return 987;
				}
				int result = dao.orderMenues(shoppingList, takeoutTogo, mdto.getMember_code(), payment_method,
						stampUse_price);
				if (result == 0) {
					System.out.println("결제가 실패되었습니다.");
					return 567;
				}
				System.out.println("결제가 완료되었습니다.");
				return 987;
				
			} catch (NumberFormatException e) {
				System.out.println("숫자를 입력해주세요");
			} catch (Exception e) {
			}
		}
	}
}
