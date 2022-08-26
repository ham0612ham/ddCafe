package ddCafe_Customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class KioskUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private KioskDAO dao = new KioskDAOImpl();
	
	public void menu() {
		System.out.println("\n🜚 어서요세요 🜚");
	}
	
}
