package ddCafe_Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class KioskDAOImpl implements KioskDAO{

	@Override
	public void choiceTogo(boolean a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> selectCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuDTO> showMenues(int category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void orderMenues(Map<String, Integer> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bestMenues() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int addMember(String name, String tel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int findMember(String tel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean useStamp(boolean a) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> showPaymentMethod(int choice) {
		// TODO Auto-generated method stub
		return null;
	}

}
