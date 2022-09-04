package ddCafe_Manage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import oracle.jdbc.OracleTypes;

public class MenuDAOmpl implements MenuDAO{
	private Connection conn = DBConn.getConnection();
	
	
	public boolean equals(Object obj) {
		return(this==obj);
	}

	@Override
	public int addMenu(MenuDTO dto) throws SQLException {
		int result = 0;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			conn.setAutoCommit(false);
			
			sql = "select menu_code from menu where menu_name = ?"; // 메뉴이름이 같은 메뉴코드 찾음
			int no3=0;
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMenuName());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				no3 = rs.getInt("menu_code");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
			
			sql = "select menu_name from menu"; // 메뉴들의 이름을 모음
			String ss = null;
			List<String> sss = new ArrayList<>();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ss = rs.getString("menu_name");
				sss.add(ss);
			}
			rs.close();
			pstmt.close();
			pstmt = null;
			
			boolean b = true;
			for(String menu_name : sss) { // 메뉴 이름중에 같은 메뉴가 있는지 확인해봄
				if(menu_name.equals(dto.getMenuName())) {
					b = false; // 이전에 있던 메뉴일 경우
					break;
				} else{
					b = true; // 이전에 있던 메뉴가 아닐 경우
				}
			}
			
			if(b==true) { // 이전에 있던 메뉴가 아닐 경우 
				sql = "Insert into menu(menu_code,menu_name,category_num,status) values(menu_seq.NEXTVAL,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dto.getMenuName());
				pstmt.setInt(2, dto.getCategoryNum());
				pstmt.setString(3, dto.getStatus());
				result = pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
				
				sql = "Insert into menu_detail(menu_detail_code,menu_price,menu_code,menu_size) values(menu_detail_seq.NEXTVAL,?,menu_seq.currval,?)";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,dto.getMenuPrice());
				pstmt.setString(2,dto.getMenuSize());
				pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
			} else { // 이전에 있던 메뉴일 경우 
				sql = "Insert into menu_detail(menu_detail_code,menu_price,menu_code,menu_size) values(menu_detail_seq.NEXTVAL,?,?,?)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1,dto.getMenuPrice());
				pstmt.setInt(2,no3);
				pstmt.setString(3,dto.getMenuSize());
				pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
			}
			
			for(Integer n : dto.getIngredients()) { // 모든 재료들을 추가함
				sql = "Insert into menu_ingredient(ingredient_code,menu_detail_code) values(?,menu_detail_seq.CURRVAL)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, n);
				pstmt.executeUpdate();
				pstmt.close();
			}
			
			conn.commit();
		} catch (SQLIntegrityConstraintViolationException e) {
			
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			// 기본키 제약 위반, NOT NULL 등의 제약 위반 - 무결성 제약 위반시 발생
			if(e.getErrorCode() == 1) { // 기본키 중복
				System.out.println("상품코드 중복으로 등록이 불가능합니다.");
			} else if(e.getErrorCode() == 1400) { // NOT NULL
				System.out.println("필수 입력 사항을 입력 하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
			
			throw e;
		} catch (SQLException e) {
			
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
			
			throw e;
		} finally {
			if(cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	@Override
	public int updateMenuprice(MenuDTO dto) throws SQLException {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = "{CALL updateMenu(?,?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, dto.getMenuNum());
			cstmt.setInt(2, dto.getPrice());

			cstmt.executeUpdate();
			result = 1;


			
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode() == 1400) {
				System.out.println("필수 입력 사항을 입력하지 않았습니다.");
			}else {
				System.out.println(e.toString());
			}
			
			throw e;
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 20100) {
				System.out.println("등록된 상품이 아닙니다.");
			}
			e.printStackTrace();
			throw e;
			
		} finally {
			if(cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	@Override
	public int deleteMenu(int code) throws SQLException {
		int result = 0;
		MenuDTO dto = new MenuDTO();
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
//			sql = "{CALL deleteMenu(?)}";
//			cstmt = conn.prepareCall(sql);
//			cstmt.setInt(1, code);
//			cstmt.executeUpdate();
//			result = 1;
			
			conn.setAutoCommit(false);
			sql = "select menu_detail_code from menu_detail where menu_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, code);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				dto.setMenuDetailNum(rs.getInt("menu_detail_code"));
			}
			pstmt.close();
			pstmt = null;
			
			sql = "delete from menu_ingredient where menu_detail_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getMenuDetailNum());
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "delete from menu_detail where menu_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, code);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			sql = "delete from menu where menu_code = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, code);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (Exception e2) {
			}
			if(e.getErrorCode() == 20100) {
				System.out.println("등록된 상품이 아닙니다.");
			}
			throw e;
			
		} finally {
			if(cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}

	@Override
	public List<MenuDTO> listMenu() {
		List<MenuDTO> list = new ArrayList<>();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "{CALL listMenu(?)}";
			
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.executeQuery();
			
			
			rs = (ResultSet)cstmt.getObject(1);
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setMenuNum(Integer.parseInt(rs.getString("menu_code")));
				dto.setMenuName(rs.getString("menu_name"));
				dto.setCategoryNum(Integer.parseInt(rs.getString("category_num")));
				dto.setStatus(rs.getString("status"));
				dto.setMenuPrice(Integer.parseInt(rs.getString("menu_price")));
				dto.setMenuSize(rs.getString("menu_size"));
				
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
			if(cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
		
	}

	@Override
	public List<MenuDTO> listMenu(String menuName) {
		List<MenuDTO> list = new ArrayList<>();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "{CALL searchNameMenu(?,?)}";
			
			cstmt = conn.prepareCall(sql);
			
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.setString(2, menuName);
			
			cstmt.executeQuery();
			
			rs = (ResultSet)cstmt.getObject(1);
	
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setMenuNum(Integer.parseInt(rs.getString("menu_code")));
				dto.setMenuName(rs.getString("menu_name"));
				dto.setCategoryNum(Integer.parseInt(rs.getString("category_num")));
				dto.setStatus(rs.getString("status"));
				dto.setMenuPrice(Integer.parseInt(rs.getString("menu_price")));
				dto.setMenuSize(rs.getString("menu_size"));
				
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
			if(cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		
		return list;
	}

	@Override
	public List<MenuDTO> selectCategory() throws SQLException {
		List<MenuDTO> list  = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select * from category";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setCategoryNum(Integer.parseInt(rs.getString("category_num")));
				dto.setCategoryName(rs.getString("category_name"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	@Override
	public int updateSoldOut(int code) throws SQLException {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = "{CALL updateSoldout(?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, code);
			

			cstmt.executeUpdate();
			result = 1;


			
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode() == 1400) {
				System.out.println("필수 입력 사항을 입력하지 않았습니다.");
			}else {
				System.out.println(e.toString());
			}
			
			throw e;
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 20100) {
				System.out.println("등록된 상품이 아닙니다.");
			}
			e.printStackTrace();
			throw e;
			
		} finally {
			if(cstmt != null) {
				try {
					cstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}


	@Override
	public List<MenuDTO> listSoldout() {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select unique menu_name"
					+ " from menu m1"
					+ " Join menu_detail m2 on m2.menu_code = m1.menu_code"
					+ " Join menu_ingredient m3 on m3.menu_detail_code = m2.menu_detail_code"
					+ " Join ingredient i1 on i1.ingredient_code = m3.ingredient_code"
					+ " WHERE ingredient_qty = 0 or status = '품절'";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				dto.setSoldMenu(rs.getString("menu_name"));
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
	public List<MenuDTO> showTwoMenu() {
		List<MenuDTO> list  = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select menu_code,menu_name from menu order by menu_code";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				
				dto.setMenuNum(Integer.parseInt(rs.getString("menu_code")));
				dto.setMenuName(rs.getString("menu_name"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}

	@Override
	public int insertIngredient(IngredientDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql;
		
		try {
			sql = "insert into ingredient(ingredient_code,ingredient_name,ingredient_qty)values(ingredient_seq.nextval,?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getIngredient_name());
			pstmt.setInt(2, dto.getIngredient_qty());
			result = pstmt.executeUpdate();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 제약 위반, NOT NULL 등의 제약 위반 - 무결성 제약 위반시 발생
			if(e.getErrorCode() == 1) { // 기본키 중복
				System.out.println("재료코드 중복으로 등록이 불가능합니다.");
			} else if(e.getErrorCode() == 1400) { // NOT NULL
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
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}


}
