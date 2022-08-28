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
	
	
	@Override // 납품업체 리스트확인
	public List<IngredientDTO> vendorList() {
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

	/*
	@Override // 현재 재고 확인
	public Map<String, Integer> left_ingredient() {
		//Map<String, Integer> map = new HashMap<>();
		
		
		
		return null;
	}
	 */
	
	
	@Override // 현재 재고 확인
	public List<IngredientDTO> leftingredient() {
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
	
	
	
	
	
	@Override // 입고된 재료 수량 확인
	public Map<String, Integer> in_ingredient(String ingredient, String date) {
		/*
		PreparedStatement pstmt = null;
		String sql;
		
		Set<String> set = map.keySet(); // 키에 대한 Set 객체
		Iterator<String> it = set.iterator(); // 키에 대한 반복자
		while(it.hasNext()) {
			String key = it.next(); // 키
			Integer a = map.get(key); // 키에 대한 값
			System.out.println(key + " => " + a);
		}
		System.out.println();
		
		pstmt = conn.prepareStatement(sql);
		
		// SELECT 문을 제외한 DML, DDL 실행
		result = pstmt.executeUpdate(sql);
		*/
		
		return null;
	}
	
	

	@Override // 입고된 재료 수량 확인
	public Map<String, Integer> in_ingredient(String date) {
		return null;
	}

	
	@Override // 재료추가주문 // 재료 이름, 숫자             
	// public int add_ingredient(String ingredient, int qty, String vendor) throws SQLException {
	
	public int add_ingredient(IngredientDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null; // 쿼리를 실행한다.
		String sql;

		try {
			// INSERT INTO 테이블명 (컬럼, 컬럼) VALUES (값, 값)
			sql = "INSERT INTO ingredient(receiving_num, receiving_date, receiving_qty, receiving_price, ingredient_name, vendor_code, ingredient_code) "
					+ "VALUES (RECEIVING_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
			
			
			pstmt = conn.prepareStatement(sql);
			
			// SELECT 문을 제외한 DML, DDL 실행
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
	
	
// 오라클에 트리거 만들어서 폐기에 자료 업데이트 되면 알아서 재료테이블의 재료 수당이 감소함
	
	@Override // 재고 삭제     
	// public int sub_ingredient(String ingredient, int qty, String vendor) throws SQLException {
	
	public int sub_ingredient(IngredientDTO dto) throws SQLException{ 
		int result = 0;
		PreparedStatement pstmt = null; // 쿼리를 실행한다.
		String sql;

		try {
			sql = "INSERT INTO ingredient(trash_code, ingredient_code, trash_date, trash_qty, remark) VALUES (TRASH_SEQ.NEXTVAL, ?, ?, ?, ?)";
			
			
			pstmt = conn.prepareStatement(sql);
			
			// SELECT 문을 제외한 DML, DDL 실행
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

}
