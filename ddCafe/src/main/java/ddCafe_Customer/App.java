package ddCafe_Customer;

import db.util.DBConn;

public class App {
	public static void main(String[] args) {
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
		System.out.println("ミ★ ᗪOᑌᗷᒪE ᗪᖇᗩGOᑎ ᑕOᖴᖴEE ミ★");
		
		new KioskUI().menu();
		
		DBConn.close();
	}
}