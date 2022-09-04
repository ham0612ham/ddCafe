package ddCafe_Customer;

import java.util.InputMismatchException;
import java.util.Scanner;

import db.util.DBConn;
import ddCafe_Manage.IngredientUI;
import ddCafe_Manage.MemberUI;
import ddCafe_Manage.MenuUI;
import ddCafe_Manage.SalesUI;

public class App {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		IngredientUI in = new IngredientUI();
		MemberUI member = new MemberUI();
		MenuUI menu = new MenuUI();
		SalesUI sale = new SalesUI();
		System.out.println("ミ★ ᗪOᑌᗷᒪE ᗪᖇᗩGOᑎ ᑕOᖴᖴEE ミ★");
		
		boolean b = true;
		while(b) {
			System.out.println("                                                 /===-_---~~~~~~~~~------____");
			System.out.println("                                                |===-~___                _,-'");
			System.out.println("                 -==\\\\                         `//~\\\\   ~~~~`---.___.-~~");
			System.out.println("             ______-==|                         | |  \\\\           _-~`");
			System.out.println("       __--~~~  ,-/-==\\\\                        | |   `\\        ,'");
			System.out.println("    _-~       /'    |  \\\\                      / /      \\      /");
			System.out.println("  .'        /       |   \\\\                   /' /        \\   /'");
			System.out.println(" /  ____  /         |    \\`\\.__/-~~ ~ \\ _ _/'  /          \\/'");
			System.out.println("/-'~    ~~~~~---__  |     ~-/~         ( )   /'        _--~`");
			System.out.println("                  \\_|      /        _)   ;  ),   __--~~");
			System.out.println("                    '~~--_/      _-~/-  / \\   '-~ \\");
			System.out.println("                   {\\__--_/}    / \\\\_&gt;- )&lt;__\\      \\");
			System.out.println("                   /'   (_/  _-~  | |__&gt;--&lt;__|      |");
			System.out.println("                  |0  0 _/) )-~     | |__&gt;--&lt;__|     |");
			System.out.println("                  / /~ ,_/       / /__&gt;---&lt;__/      |");
			System.out.println("                 o o _//        /-~_&gt;---&lt;__-~      /");
			System.out.println("                 (^(~          /~_&gt;---&lt;__-      _-~");
			System.out.println("                ,/|           /__&gt;--&lt;__/     _-~");
			System.out.println("             ,//('(          |__&gt;--&lt;__|     /                  .----_");
			System.out.println("            (   '))          |__&gt;--&lt;__|    |                 /' _---_~\\");
			System.out.println("         `-))  ) (           |__&gt;--&lt;__|    |               /'  /     ~\\`\\");
			System.out.println("        ,[  어서  ]((             \\__&gt;--&lt;__\\    \\            /'  //        ||");
			System.out.println("      ,( '    )( )            ~-__&gt;--&lt;_~-_  ~--____---~' _/'/        /'");
			System.out.println("    `~[  오세용  ]/|                 ~-_~&gt;--&lt;_/-__       __-~ _/");
			System.out.println("  ._-~//      )) `                    ~~-'_/_/ /~~~~~~~__--~");
			System.out.println("   ;'( ')/ ,)(                              ~~~~~~~~~~");
			System.out.println("  ' ') '( (/");
			System.out.println("    '   '  `");

			int result = new KioskUI().menu();
			if(result==9999) {
				DBConn.close();
				break;
			} else if(result==0000) {
				int log;
				ManagerLog mlog = new ManagerLog();
				log = mlog.log();
				if(log==1) {
					System.out.println("관리자메뉴로 로그인되었습니다.");
					while(true) {
					try {
							int ch;
							do {
								System.out.print("\n1.메뉴관리 2.매출관리 3.재고관리 4.회원관리 5.돌아가기 => ");
								ch = sc.nextInt();
							} while(ch<1||ch>5);
							if(ch==5) break;
							switch(ch) {
							case 1: menu.menu(); break;
							case 2: sale.menu(); break;
							case 3: in.menu(); break;
							case 4: member.menu(); break;
							}
						} catch (InputMismatchException e) {
							sc.nextLine();
						}
					}
				}
				b = true;
			}
		}
		sc.close();
	}
}