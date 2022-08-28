package ddCafe_Manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import db.util.DBConn;

public class IngredientImpl implements IngredientDAO{
	private Connection conn = DBConn.getConnection();
	
	
	
	
	@Override // 현재 재고 확인
	public List<IngredientDTO> leftingredient() { // 완료 1.재고확인
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql  = "SELECT ingredient_code, ingredient_name, ingredient_qty "
					+ " FROM ingredient";
			
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
  	public int add_ingredient(String ingredient, int qty) throws SQLException { // 2.재료추가주문
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		int result = 0;
		List<IngredientDTO> list = new ArrayList<>();
		
	
		
		try {
			sql = "INSERT INTO receiving_ingredient("
					+ "receiving_num, receiving_date, receiving_qty, receiving_price, ingredient_name, vendor_code, ingredient_code) "
					+ "SELECT (RECEIVING_SEQ.NEXTVAL, SYSDATE, ?, ?, ?, vendor_seq.CURRVAL, ingredient_seq.CURRVAL)";
			// INSERT문에... 재고이름과 재고량만 넣으면 RECEVING_INGREDIENT에 인서트 되는,,,그런,,, 
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, qty);
			//pstmt.setString(2,  );
			
			
			result = pstmt.executeUpdate();
			
			
			
		} catch (SQLIntegrityConstraintViolationException e) {
		
			if(e.getErrorCode() == 1400) { // NOT NULL
				System.out.println("필수 입력 사항을 입력 하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
			
			throw e;
			
		} catch (SQLDataException e) {

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
		}
		
		return result;
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
					+ "ORDER BY receiving_date DESC";
			
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

	
	public int new_ingredient(String ingredient, int qty) throws SQLException { // 완료 4.새로운재료추가
		PreparedStatement pstmt = null;
		String sql;
		int result = 0;
		
		try {
			sql = "INSERT INTO ingredient(ingredient_code, ingredient_name, ingredient_qty )"
					+ " VALUES (ingredient_seq.NEXTVAL,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ingredient);
			pstmt.setInt(2, 0);
			
			result = pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			try {
			} catch (Exception e2) {
			}
			
			if(e.getErrorCode()==1) {
				System.out.println("이미 등록된 재료 입니다.");
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
	
	
	@Override // 재고 삭제     
	// public int sub_ingredient(String ingredient, int qty, String vendor) throws SQLException {
	
	public int sub_ingredient(IngredientDTO dto) throws SQLException{ // 5.재고삭제
		int result = 0;
		PreparedStatement pstmt = null; // 쿼리를 실행한다.
		String sql;

		try {
			sql = "INSERT INTO ingredient(trash_code, ingredient_code, trash_date, trash_qty, remark) "
					+ "VALUES (TRASH_SEQ.NEXTVAL, INGREDIENT_SEQ.CURRVAL, SYSDATE, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getTrash_qty());
			pstmt.setString(2, dto.getRemark());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLIntegrityConstraintViolationException e) {
		
			if(e.getErrorCode() == 1400) { // NOT NULL
				System.out.println("필수 입력 사항을 입력 하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
			
			throw e;
			
		} catch (SQLDataException e) {

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
		}
		
		return result;
	}

	
	@Override // 납품업체 리스트확인
	public List<IngredientDTO> vendorList() { // 완료 6.납품업체확인
		List<IngredientDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql  = "SELECT vendor_name, manager_name, manager_tel "
					+ " FROM vendor";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);
			
			while(rs.next()) {
				IngredientDTO dto = new IngredientDTO();
				
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
