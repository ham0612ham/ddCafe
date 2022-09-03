 package ddCafe_Customer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ManagerLog {
	private String pwd = "dd1234";
	private String id = "ddmanager";
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public int try_log(List<String> log) {
		
		if(log.get(0).equals(id)&&log.get(1).equals(pwd)) {
			return 1;
		} else return 0;
	}
	
	public int log() {
		int logn=0;
		System.out.println("\nミ★ 관리자 로그인 ミ★");
		for(int i=1; i<=3; i++) {
			try {
				System.out.println("\n로그인 ["+i+"/3회]");
				String id, pwd;
				List<String> login = new ArrayList<>();
				System.out.print("아이디를 입력해주세요 => ");
				id = br.readLine();
				login.add(id);
				
				System.out.print("비밀번호를 입력해주세요 => ");
				pwd = br.readLine();
				login.add(pwd);
				
				logn = try_log(login);
				
				if(logn==1) {
					return logn;
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return logn;
	}
}
