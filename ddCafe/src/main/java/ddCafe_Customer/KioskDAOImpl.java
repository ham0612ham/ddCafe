package ddCafe_Customer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import ddCafe_Manage.SalesDTO;

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
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<MenuDTO> list = new ArrayList<>();
		
		try {
			sql = "{ CALL menuSoldout }";
			cstmt = conn.prepareCall(sql);
			cstmt.executeUpdate();
			cstmt.close();
			
			sql = "{ CALL menuEnable }";
			cstmt = conn.prepareCall(sql);
			cstmt.executeUpdate();
			cstmt.close();
			
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
	public int orderMenues(List<MenuDTO> list, String takeout_togo,int member_code, String payment_method, int stampUse_price) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;
		int result=0, final_price;
		List<Integer> plasticList = selectPlastic(list);
		
		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO menu_order "
					+ "(order_num, order_date, total_price, takeout_togo, member_code) "
					+ "VALUES (order_seq.NEXTVAL, SYSDATE, ?, ?, DECODE(?, 0, null, ?))";
				
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, totalPrice(list));
			pstmt.setString(2, takeout_togo);
			pstmt.setInt(3, member_code);
			pstmt.setInt(4, member_code);
				
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			// 메뉴에 있는 모든 리스들의 재료를 주문 개수만큼 뺌
			for(MenuDTO dto : list) {
				sql = "INSERT INTO order_detail (order_detail_num, order_qty, order_num, menu_detail_code) "
						+ "VALUES (order_detail_seq.NEXTVAL, ?, order_seq.CURRVAL, ?) ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getQty());
				pstmt.setInt(2, dto.getMenu_detail_code());
				
				result += pstmt.executeUpdate();
				pstmt.close();
				pstmt = null;
			}
			
			// 플라스틱 컵을 사이즈별로 개수만큼 뺌
			if(takeout_togo.equals("포장")) {
				
				// R사이즈 플라스틱 컵 수량 줄어들게
				sql = " UPDATE ingredient SET ingredient_qty = ? WHERE ingredient_code = 29 ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, plasticList.get(0));
				result = pstmt.executeUpdate();
				
				pstmt.close();
				pstmt = null;
				
				// L사이즈 플라스틱 컵 수량 줄어들게
				sql = " UPDATE ingredient SET ingredient_qty = ? WHERE ingredient_code = 30 ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, plasticList.get(1));
				result = pstmt.executeUpdate();
				
				pstmt.close();
				pstmt = null;
			}
			
			if(stampUse_price > 0) {
				final_price = totalPrice(list)-stampUse_price < 0 ? 0 : totalPrice(list)-3000;
			} else final_price = totalPrice(list);
			
			sql = "INSERT INTO payment "
					+ "(payment_num, order_num, payment_method, payment_price, stampUse_price) "
					+ "VALUES (payment_seq.NEXTVAL, order_seq.CURRVAL, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, payment_method);
			pstmt.setInt(2, final_price);
			pstmt.setInt(3, stampUse_price);
			result = pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if(stampUse_price!=0) {
				sql = "INSERT INTO stamp (stamp_num, order_num, stampUse_date) "
						+ " VALUES (stamp_seq.NEXTVAL, order_seq.CURRVAL, SYSDATE) ";
				pstmt = conn.prepareStatement(sql);
				result = pstmt.executeUpdate();
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
	public List<MenuDTO> bestMenues() {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT menu_code, rank FROM( "
					+ " SELECT menu_name,SUM(order_qty) qty ,RANK() OVER(ORDER BY SUM(order_qty) DESC) rank, m2.menu_code "
					+ " FROM order_detail o1 " + " JOIN menu_detail m1 ON o1.menu_detail_code = m1.menu_detail_code "
					+ " JOIN menu m2 ON m1.menu_code = m2.menu_code " 
					+ " GROUP BY menu_name, m2.menu_code "
					+ " )WHERE rank <=3 ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				MenuDTO dto = new MenuDTO();

				dto.setMenu_detail_code(rs.getInt("menu_code"));
				dto.setRank(rs.getInt("rank"));

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
	public int addMember(String name, String tel)  throws MyDuplicationException, SQLException  {
		if(findMember(tel).getMember_tel() != null) {
			throw new MyDuplicationException("이미 등록된 번호입니다.");
		};
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
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, tel);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto.setMember_code(rs.getInt("member_code"));
				dto.setMember_name(rs.getString("member_name"));
				dto.setMember_date(rs.getDate("member_date").toString());
				dto.setMemeber_tel(tel);
			}
			pstmt.close();
			pstmt = null;
			
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
	public int usableStamp(int member_code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		int usable_stamp = 0, used_stamp = 0;
		
		try {
			sql = "SELECT SUM(order_qty) sum "
					+ "FROM order_detail d "
					+ "JOIN menu_order mo ON d.order_num = mo.order_num "
					+ "JOIN member m ON m.member_code = mo.member_code "
					+ "WHERE m.member_code = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, member_code);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				usable_stamp += rs.getInt("sum");
			};
			pstmt.close();
			pstmt = null;
			rs.close();
			rs = null;
			
			sql = "SELECT SUM(stampUse_price) used_price "
					+ "FROM payment p "
					+ "JOIN menu_order mo ON p.order_num = mo.order_num "
					+ "JOIN member m ON mo.member_code = m.member_code "
					+ "WHERE m.member_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, member_code);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				used_stamp += rs.getInt("used_price");
			}
			
			usable_stamp = usable_stamp - (used_stamp/3000)*20;
			
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
		return usable_stamp;
	}

	@Override
	public List<String> showPaymentMethod() {
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		
		try {
			sql = "SELECT payment_method FROM payment_method";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				list.add(rs.getString("payment_method"));
			}
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

	@Override
	public List<Integer> selectPlastic(List<MenuDTO> list) {
		List<Integer> plasticList = new ArrayList<>();
		int countR=0, countL=0;
		String sql;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			sql = "SELECT ingredient_qty FROM ingredient WHERE ingredient_code = 29";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				countR = rs.getInt("ingredient_qty");
			}
			pstmt.close();
			pstmt = null;
			
			sql = "SELECT ingredient_qty FROM ingredient WHERE ingredient_code = 30";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				countL = rs.getInt("ingredient_qty");
			}
			pstmt.close();
			pstmt = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(MenuDTO dto :list) {
			if(dto.getSize().equals("R")) {
				countR -= dto.getQty();
			} else if(dto.getSize().equals("L")){
				countL -= dto.getQty();
			}
		}
		plasticList.add(countR);
		plasticList.add(countL);
		
		return plasticList;
	}

	@Override
	public boolean calculateMenu(List<MenuDTO> list) {
		String sql;
		List<CalculateDTO> clist = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean b = true;
		int ingredient_code, ingredient_qty;
		int calculate, plus = 0;
		
		try {
			for(MenuDTO dto : list) {
				sql = "SELECT i.ingredient_code "
						+ "FROM menu_detail md "
						+ "JOIN menu_ingredient mi ON mi.menu_detail_code = md.menu_detail_code "
						+ "JOIN ingredient i ON mi.ingredient_code = i.ingredient_code "
						+ "WHERE md.menu_detail_code = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getMenu_detail_code());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					CalculateDTO cdto = new CalculateDTO();
					cdto.setIngredient_code(rs.getInt("ingredient_code"));
					cdto.setIngredient_qty(dto.getQty());
					
					clist.add(cdto);
				}
				pstmt.close();
				pstmt = null;
			}
			for(int i = 0; i<clist.size(); i++) {
				for(int j = 0; j<i; j++) {
					CalculateDTO cdto2 = clist.get(i);
					CalculateDTO cdto3 = clist.get(j);
					if(cdto2.getIngredient_code()==cdto3.getIngredient_code()) {
						cdto3.setIngredient_qty(cdto3.getIngredient_qty() + cdto2.getIngredient_qty());
					}
				}
			}
			
			for(CalculateDTO cdto : clist) {
				sql = "SELECT i.ingredient_code, ingredient_qty "
						+ "FROM menu_detail md "
						+ "JOIN menu_ingredient mi ON mi.menu_detail_code = md.menu_detail_code "
						+ "JOIN ingredient i ON mi.ingredient_code = i.ingredient_code "
						+ "WHERE md.menu_detail_code = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, cdto.getIngredient_code());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					ingredient_code = rs.getInt("ingredient_code");
					ingredient_qty = rs.getInt("ingredient_qty");
					
					for(CalculateDTO cdto2 : clist) {
						if(cdto2.getIngredient_code()==ingredient_code) {
							calculate = cdto2.getIngredient_qty()>ingredient_qty? 1 : 0 ;
							plus += calculate;
						}
					}
				}
			}
			b = plus > 0 ? true : false ;
			
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
		return b;
	}
	
}
