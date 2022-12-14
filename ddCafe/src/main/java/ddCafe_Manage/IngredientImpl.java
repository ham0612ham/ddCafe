package ddCafe_Manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;

public class IngredientImpl implements IngredientDAO{
	private Connection conn = DBConn.getConnection();
	
	
	@Override 
	public List<IngredientDTO> left_ingredient() { 
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql  = "SELECT ingredient_code, ingredient_name, ingredient_qty "
					+ " FROM ingredient ORDER BY ingredient_code";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			
			while(rs.next()) {
				IngredientDTO dto = new IngredientDTO();
				
				dto.setIngredient_code(rs.getInt("ingredient_code"));
				dto.setIngredient_name(rs.getString("ingredient_name"));
				dto.setIngredient_qty(rs.getInt("ingredient_qty"));
				
				list.add(dto);
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
	public int add_ingredient(IngredientDTO dto) throws SQLException { 
	PreparedStatement pstmt = null;
		String sql, sql1;
		int result = 0;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO receiving_ingredient("
					+ "receiving_num, receiving_date, receiving_qty, receiving_price, ingredient_name, vendor_code, ingredient_code) "
					+ " VALUES (RECEIVING_SEQ.NEXTVAL, SYSDATE, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getReceiving_qty());
			pstmt.setInt(2, dto.getReceiving_price());
			pstmt.setString(3, dto.getIngredient_name());
			pstmt.setInt(4, dto.getVendor_code());
			pstmt.setInt(5,dto.getIngredient_code());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql1 = "UPDATE ingredient SET ingredient_qty = ingredient_qty + ? WHERE ingredient_code = ? ";
			
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, dto.getReceiving_qty());
			pstmt.setInt(2, dto.getIngredient_code());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			conn.commit();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
				conn.rollback();                    
			} catch (Exception e2) {
			}
			
			if(e.getErrorCode() == 1400) { 
				System.out.println("?????? ?????? ????????? ?????? ?????? ???????????????.");
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
		}
		return result;
	}
	
	
	@Override 
	public List<IngredientDTO> show_orderlist(){
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT DISTINCT i.ingredient_code, ri.ingredient_name, ri.receiving_price, "
					+ " v.vendor_code, vendor_name, manager_name, manager_tel "
					+ "FROM vendor v "
					+ "JOIN receiving_ingredient ri ON ri.vendor_code = v.vendor_code "
					+ "JOIN ingredient i ON ri.ingredient_code = i.ingredient_code "
					+ "ORDER BY ingredient_code ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				IngredientDTO dto = new IngredientDTO();
				dto.setIngredient_code(rs.getInt("ingredient_code"));
				dto.setIngredient_name(rs.getString("ingredient_name"));
				dto.setReceiving_price(rs.getInt("receiving_price"));
				dto.setVendor_code(rs.getInt("vendor_code"));
				dto.setVendor_name(rs.getString("vendor_name"));
				dto.setManager_name(rs.getString("manager_name"));
				dto.setManager_tel(rs.getString("manager_tel"));
				list.add(dto);
			}
			
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
		return list;
	}
	
	
	@Override 
	public List<IngredientDTO> receiving_history() { 
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT TO_CHAR(receiving_date,'YYYY-MM-DD') receiving_date, ingredient_name, receiving_qty "
					+ "FROM receiving_ingredient "
					+ "ORDER BY receiving_date DESC, receiving_num DESC";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				IngredientDTO dto = new IngredientDTO();
				dto.setReceiving_date(rs.getString("receiving_date"));
				dto.setIngredient_name(rs.getString("ingredient_name"));
				dto.setReceiving_qty(rs.getInt("receiving_qty"));
				list.add(dto);
			}
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
		return list;
	}
	
	@Override
	public List<IngredientDTO> selectMonth(String receiving_date) {
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT TO_CHAR(receiving_date, 'YYYY-MM-DD') receiving_date, ingredient_name, receiving_qty "
					+ "FROM receiving_ingredient "
					+ "WHERE TO_CHAR(receiving_date,'YYYYMM') = ? "
					+ "ORDER BY receiving_date DESC, receiving_num DESC";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, receiving_date);
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				IngredientDTO dto = new IngredientDTO();
				dto.setReceiving_date(rs.getString("receiving_date"));
				dto.setIngredient_name(rs.getString("ingredient_name"));
				dto.setReceiving_qty(rs.getInt("receiving_qty"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	
	@Override
	public int newIngredient(IngredientDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		int result = 0;
		
		try {
			conn.setAutoCommit(false);
		
			sql = "INSERT INTO ingredient(ingredient_code, ingredient_name, ingredient_qty )"
					+ " VALUES (ingredient_seq.NEXTVAL,?,0)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getIngredient_name());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO receiving_ingredient(receiving_num, receiving_date, receiving_qty, "
					+ " receiving_price, ingredient_name, vendor_code, ingredient_code )"
					+ " VALUES (receiving_seq.NEXTVAL, sysdate, 0, ?, ?, ?, ingredient_seq.CURRVAL )";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getReceiving_price()); 
			pstmt.setString(2, dto.getIngredient_name()); 
			pstmt.setInt(3, dto.getVendor_code()); 
		
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
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
	public int newVendor(String vendorName, String managerName, String managerTel, String compRegisNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		int result = 0;
		
		try {
			sql = "INSERT INTO vendor(vendor_code, vendor_name, manager_name, manager_tel, companyRegistration_num) "
					+ " VALUES (vendor_seq.NEXTVAL, ? , ? , ? , ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vendorName);
			pstmt.setString(2, managerName);
			pstmt.setString(3, managerTel);
			pstmt.setString(4, compRegisNum);
			result = pstmt.executeUpdate();
		
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
			} catch (Exception e2) {
			}
			if(e.getErrorCode()==1) {
				System.out.println("?????? ????????? ???????????? ?????????.");
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
	public int sub_ingredient(IngredientDTO dto) throws SQLException{
		PreparedStatement pstmt = null; 
		String sql, sql1;
		int result = 0;

		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO trash(trash_code, ingredient_code, trash_date, trash_qty, remark) "
					+ " VALUES (TRASH_SEQ.NEXTVAL, ?, SYSDATE, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getIngredient_code());
			pstmt.setInt(2, dto.getTrash_qty());
			pstmt.setString(3, dto.getRemark());
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql1 = "UPDATE ingredient SET ingredient_qty = ingredient_qty - ?  WHERE ingredient_code = ?";
			
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, dto.getTrash_qty());
			pstmt.setInt(2, dto.getIngredient_code());
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			conn.commit();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
				conn.rollback();                 
			} catch (Exception e2) {
			}
		
			if(e.getErrorCode() == 1400) { 
				System.out.println("?????? ?????? ????????? ?????? ?????? ???????????????.");
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
		}
		return result;
	}

	
	@Override
	public List<IngredientDTO> trash_ingredientcode() {
	List<IngredientDTO> list = new ArrayList<>();
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql;
	
	try {
		sql = "SELECT ingredient_code, ingredient_name, ingredient_qty FROM ingredient ORDER BY ingredient_code";
		pstmt = conn.prepareStatement(sql);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			IngredientDTO dto = new IngredientDTO();
			dto.setIngredient_code(rs.getInt("ingredient_code"));
			dto.setIngredient_name(rs.getString("ingredient_name"));
			dto.setIngredient_qty(rs.getInt("ingredient_qty"));
			list.add(dto);
		}
		
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
	return list;
}
	
	
	@Override
	public List<IngredientDTO> vendorList() {
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql  = "SELECT vendor_code, vendor_name, manager_name, manager_tel "
					+ " FROM vendor ORDER BY vendor_code";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			
			while(rs.next()) {
				IngredientDTO dto = new IngredientDTO();
				dto.setVendor_code(rs.getInt("vendor_code"));
				dto.setVendor_name(rs.getString("vendor_name"));
				dto.setManager_name(rs.getString("manager_name"));
				dto.setManager_tel(rs.getString("manager_tel"));
				list.add(dto);
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
}
