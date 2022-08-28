package ddCafe_Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


import db.util.DBConn;

public class KioskDAOImpl implements KioskDAO{
	private Connection conn = DBConn.getConnection();
	
	@Override
	public List<String> showCategory() {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		
		try {
			sql = " SELECT category_name FROM category ";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getString("category_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}

	@Override
	public List<MenuDTO> showMenues(int category_num) {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<MenuDTO> list = new ArrayList<>();
		
		try {
			sql = "SELECT menu_detail_code, m.menu_name, md.menu_size, md.menu_price, status "
					+ "FROM menu m "
					+ "FULL OUTER JOIN menu_detail md ON md.menu_code = m.menu_code "
					+ "WHERE category_num = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, category_num);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setMenu_detail_code(rs.getInt("menu_detail_code"));
				dto.setMenu(rs.getString("menu_name"));
				dto.setSize(rs.getString("menu_size"));
				dto.setPrice(rs.getInt("menu_price"));
				dto.setStatus(rs.getString("status"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<String> showMenues2(int category_num) {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		
		try {
			sql = "SELECT m.menu_name "
					+ "FROM menu m "
					+ "FULL OUTER JOIN menu_detail md ON md.menu_code = m.menu_code "
					+ "WHERE category_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, category_num);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("menu_name"));
			}
		} catch (SQLException e) {
		}
		
		return list;
	}
	
	@Override
	public int orderMenues(List<MenuDTO> list, String takeout_togo,int member_code) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		int result=0;
		
		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO menu_order "
					+ "(order_num, order_date, total_price, takeout_togo, member_code) "
					+ "VALUES (order_seq.NEXTVAL, SYSDATE, ?, ?, ?)";
				
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, totalPrice(list));
			pstmt.setString(2, takeout_togo);
			pstmt.setInt(3, member_code);
				
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			for(MenuDTO dto : list) {
				sql = "INSERT INTO order_detail(order_detail_num, order_qty, order_num, menu_detail_code) "
						+ "(order_detail_seq, ?, order_seq.CURRVAL, ?) ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getQty());
				pstmt.setInt(2, dto.getMenu_detail_code());
				
				result += pstmt.executeUpdate();
			}
			
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
			
		}
		return result;
	}

	@Override
	public void bestMenues() {
		
	}

	@Override
	public int addMember(String name, String tel) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		int result = 0;
		
		try {
			sql = "INSERT INTO member(member_code, member_name, member_date, member_tel )"
					+ "VALUES (member_seq.NEXTVAL,?,SYSDATE,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, tel);
			
			result = pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
			} catch (Exception e2) {
			}
			
			if(e.getErrorCode()==1) {
				System.out.println("이미 등록된 전화번호 입니다.");
			} else if(e.getErrorCode() == 1400) {
				System.out.println("필수 입력 사항을 입력 하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
			
			throw e;
			
		} catch (SQLDataException e) {
			try {
			} catch (Exception e2) {
			}
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("날짜 입력 형식 오류 입니다.");
			} else {
				System.out.println(e.toString());
			}
			
			throw e;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			throw e;
			
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			
			try {
			} catch (Exception e2) {
			}
			
		}
		return result;
	}

	@Override
	public MemberDTO findMember(String tel){
		MemberDTO dto = new MemberDTO();
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT member_code, member_name, member_date FROM member WHERE member_tel = ?";
			pstmt = conn.prepareCall(sql);
			
			pstmt.setString(1, tel);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto.setMember_code(rs.getInt("member_code"));
				dto.setMember_name(rs.getString("member_name"));
				dto.setMember_date(rs.getDate("member_date").toString());
				dto.setMemeber_tel(tel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}

	@Override
	public boolean useStamp(boolean a) throws SQLException {
		return false;
	}

	@Override
	public List<String> showPaymentMethod(int choice) {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		
		try {
			sql = "SELECT ";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
		} catch (Exception e) {
		}
		return list;
	}

	@Override
	public int totalPrice(List<MenuDTO> list) {
		int price = 0;
		for(MenuDTO dto : list) {
			price += dto.getPrice()*dto.getQty();
		}
		
		return price;
	}


}
