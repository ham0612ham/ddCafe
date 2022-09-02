package ddCafe_Manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class MemberUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private MemberDAO dao = new MemberDAOImpl();

	public void menu() {

		int ch;

		while (true) {
			System.out.println("\n✦ 회원 메뉴 ✦");
			try {
				do {
					System.out.print("\n1.회원정보확인[이름] 2.회원정보확인[전화번호] 3.회원리스트 4.회원정보수정 5.회원탈퇴 6.이전 => ");
					ch = Integer.parseInt(br.readLine());
				} while (ch < 1 || ch > 6);

				if (ch == 6) {
					System.out.println();
					return;
				}

				switch (ch) {
				case 1:
					findByName();
					break;
				case 2:
					findByTel();
					break;
				case 3:
					listMember();
					break;
				case 4:
					updateMember();
					break;
				case 5:
					deleteMember();
					break;
				}
			} catch (Exception e) {
			}
		}
	}

	public void findByName() {
		System.out.println("\n✦ 회원 정보 확인 ︎[이름] ✦");
		String name;

		try {

			List<MemberDTO> list = null;

			System.out.print("이름을 입력해주세요 [종료 : 0] => ");
			name = br.readLine();

			if (name.equals("0")) {
				return;
			}

			list = dao.readMemberByName(name);

			if (list.size() == 0) {
				System.out.println("등록된 회원 정보가 없습니다.");
				return;
			}

			System.out.println();
			System.out.println("회원번호 / 회원이름 / 전화번호 / 회원등록일");

			for (MemberDTO dto : list) {
				System.out.print(dto.getMemberNum() + " / ");
				System.out.print(dto.getName() + " / ");
				System.out.print(dto.getTel() + " / ");
				System.out.println(dto.getDate());
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
			MemberDTO dto = new MemberDTO();

			do {
				System.out.print("전화번호를 입력해주세요 [종료 : 0] => ");
				tel = br.readLine();

				if (tel.equals("0")) {
					return;
				}

				dto = dao.readMemberByTel(tel);

				if (!tel.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
				}

			} while (!tel.matches(p));

			if (dto.getName() == null) {
				System.out.println("등록된 회원 정보가 없습니다.");
				return;
			}

			System.out.println();
			System.out.println("회원번호 / 회원이름 / 전화번호 / 회원등록일");

			System.out.print(dto.getMemberNum() + " / ");
			System.out.print(dto.getName() + " / ");
			System.out.print(dto.getTel() + " / ");
			System.out.println(dto.getDate());

			System.out.println();

		} catch (Exception e) {
			System.out.println("전화번호 검색에 실패했습니다.");
		}
		System.out.println();
	}

	public void listMember() {
		System.out.println("\n✦ 회원 리스트 ︎✦");

		System.out.println();
		System.out.println("회원번호 / 회원이름 / 전화번호 / 회원등록일");

		List<MemberDTO> list = dao.listMember();

		for (MemberDTO dto : list) {
			System.out.print(dto.getMemberNum() + " / ");
			System.out.print(dto.getName() + " / ");
			System.out.print(dto.getTel() + " / ");
			System.out.println(dto.getDate());
		}
		System.out.println();
	}

	public void updateMember() {
		System.out.println("\n✦ 회원 정보 수정 ︎✦");
		String old_tel;
		String new_name, new_tel;
		String p = "010-\\d{4}-\\d{4}";
		int choice = 0;
		int result = 0;

		try {
			MemberDTO dto = new MemberDTO();

			do {
				System.out.print("기존 전화번호를 입력해주세요 [종료 : 0] => ");
				old_tel = br.readLine();

				if (old_tel.equals("0")) {
					return;
				}

				dto = dao.readMemberByTel(old_tel);

				if (!old_tel.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
				}

			} while (!old_tel.matches(p));

			if (dto.getName() == null) {
				System.out.println("등록된 회원 정보가 없습니다.");
				return;
			}

			do {
				System.out.print(dto.getName() + "님(이/가) 맞습니까? [1.예/2.아니오] => ");
				choice = Integer.parseInt(br.readLine());
			} while (choice < 0 || choice > 2);

			System.out.println();

			if (choice == 1) {
				System.out.print("수정할 이름을 입력해주세요 => ");
				new_name = br.readLine();

				do {
					System.out.print("수정할 전화번호를 입력해주세요 => ");
					new_tel = br.readLine();

					if (!new_tel.matches(p)) {
						System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
					}
				} while (!new_tel.matches(p));

				do {
					System.out.print("정말로 수정하시겠습니까? [1.예/2.아니오] => ");
					choice = Integer.parseInt(br.readLine());
				} while (choice < 0 || choice > 2);

				if (choice == 1) {
					result = dao.updateMember(new_name, new_tel, dto.getMemberNum());

					if (result != 0) {
						System.out.println("회원 정보가 수정 되었습니다.");
					}

				} else if (choice == 2) {
					return;
				}

			} else if (choice == 2) {
				return;
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

		String tel;
		String p = "010-\\d{4}-\\d{4}";
		int choice, result;

		try {

			do {
				System.out.println("삭제할 전화번를 입력해주세요 [종료 : 0] => ");
				tel = br.readLine();

				if (tel.equals("0")) {
					return;
				}

				if (!tel.matches(p)) {
					System.out.println("입력 형식이 일치하지 않습니다[010-0000-0000]");
				}

			} while (!tel.matches(p));

			MemberDTO dto = dao.readMemberByTel(tel);

			if (dto == null) {
				System.out.println("등록된 회원정보가 없습니다.");
				return;
			}

			do {
				System.out.print(dto.getName() + "님(이/가) 맞습니까? [1.예/2.아니오] => ");
				choice = Integer.parseInt(br.readLine());
			} while (choice < 0 || choice > 2);

			do {
				System.out.println();
				System.out.print("정말로 삭제하시겠습니까? [1.예/2.아니오] =>  ");
				choice = Integer.parseInt(br.readLine());
			} while (choice < 0 || choice > 2);

			if (choice == 1) {
				result = dao.deleteMember(tel);

				if (result != 0) {
					System.out.println("회원 탈퇴가 완료되었습니다");
				}

			} else if (choice == 2) {
				return;
			}

		} catch (Exception e) {
			System.out.println("회원 탈퇴에 실패했습니다.");
		}
		System.out.println();
	}
}
