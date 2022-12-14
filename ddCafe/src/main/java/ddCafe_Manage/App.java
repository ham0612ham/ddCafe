package ddCafe_Manage;

import java.util.InputMismatchException;
import java.util.Scanner;

import db.util.DBConn;

public class App {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		IngredientUI in = new IngredientUI();
		MemberUI member = new MemberUI();
		MenuUI menu = new MenuUI();
		SalesUI sale = new SalesUI();
		
		int ch;
		while(true) {
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
			System.out.println("            ( ( '))          |__&gt;--&lt;__|    |                 /' _---_~\\");
			System.out.println("         `-)) )) (           |__&gt;--&lt;__|    |               /'  /     ~\\`\\");
			System.out.println("        ,/,'//( (             \\__&gt;--&lt;__\\    \\            /'  //        ||");
			System.out.println("      ,( ( ((, ))              ~-__&gt;--&lt;_~-_  ~--____---~' _/'/        /'");
			System.out.println("    `~/  )` ) ,/|                 ~-_~&gt;--&lt;_/-__       __-~ _/");
			System.out.println("  ._-~//( )/ )) `                    ~~-'_/_/ /~~~~~~~__--~");
			System.out.println("   ;'( ')/ ,)(                              ~~~~~~~~~~");
			System.out.println("  ' ') '( (/");
			System.out.println("    '   '  `");
			System.out.println("?????? Double Dragon Coffee ??????");
			System.out.println("?????? ????????? ???????????? ??????");
			
			try {
				do {
					System.out.print("\n1.???????????? 2.???????????? 3.???????????? 4.???????????? 5.?????? => ");
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
		sc.close();
		DBConn.close();
	}
}
