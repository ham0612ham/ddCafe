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
	
	@Override
	public int addMenu(MenuDTO dto) throws SQLException {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = "{CALL insertMenu(menu_seq.nextval,?,?,?,?,?)}";
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, dto.getMenuName());
			cstmt.setInt(2, dto.getCategoryNum());
			cstmt.setString(3, dto.getStatus());
			cstmt.setInt(4, dto.getPrice());
			cstmt.setString(5, dto.getMenuSize());
			
			cstmt.executeUpdate();
			result = 1;
			
		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 제약 위반, NOT NULL 등의 제약 위반 - 무결성 제약 위반시 발생
			if(e.getErrorCode() == 1) { // 기본키 중복
				System.out.println("아이디 중복으로 등록이 불가능합니다.");
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
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = "{CALL deleteMenu(?)}";
			cstmt = conn.prepareCall(sql);
			cstmt.setInt(1, code);
			cstmt.executeUpdate();
			result = 1;

			
			
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 20100) {
				System.out.println("등록된 자료가 아닙니다.");
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
	public int addCategory() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectIngredient() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
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
					+ " WHERE ingredient_qty = 0";
			
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


}
