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
	
	
	
	
	@Override // 현재 재고 확인
	public List<IngredientDTO> left_ingredient() { // 완료 1.재고확인
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
	

	
	@Override // 2.재료추가주문    
	public int add_ingredient(IngredientDTO dto) throws SQLException { // 2.재료추가주문
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
			
			if(e.getErrorCode() == 1400) { // NOT NULL
				System.out.println("필수 입력 사항을 입력 하지 않았습니다.");
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
	

	
	
	@Override // 입고된 재료 수량 확인
	public List<IngredientDTO> receiving_history() { //완료 3. 입고된 재료 수량 확인
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
			pstmt.setInt(1, dto.getReceiving_price()); // 입고금액 3 이거 입력
			pstmt.setString(2, dto.getIngredient_name()); // 재료명 2 이거 입력하고
			pstmt.setInt(3, dto.getVendor_code()); // 납품업체코드    1 이거먼저 받고
			//pstmt.setInt(4, dto.getIngredient_code());
		
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
				System.out.println("이미 등록된 전화번호 입니다.");
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
	
	@Override // 재고 삭제     
	public int sub_ingredient(IngredientDTO dto) throws SQLException{ // 5.재고삭제
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
		
			if(e.getErrorCode() == 1400) { // NOT NULL
				System.out.println("필수 입력 사항을 입력 하지 않았습니다.");
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
	
	
	@Override // 납품업체 리스트확인
	public List<IngredientDTO> vendorList() { // 완료 6.납품업체확인
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
