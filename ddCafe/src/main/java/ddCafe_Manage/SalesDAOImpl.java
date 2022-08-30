package ddCafe_Manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.util.DBConn;

public class SalesDAOImpl<ScoreDTO> implements SalesDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	public Map<String, Integer> countTakeOut() {
		Map<String, Integer> map = new HashMap<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "select SUM(DECODE(takeout_togo, '매장', order_qty, 0)) store,\n"
					+ "       SUM(DECODE(takeout_togo, '포장', order_qty, 0)) takeout\n" + "FROM menu_order mo\n"
					+ "join order_detail od on mo.order_num = od.order_num";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				int store = rs.getInt("store");
				int takeout = rs.getInt("takeout");

				map.put("store", store);
				map.put("takeout", takeout);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return map;
	}

	@Override
	public List<MenuDTO> listPanmai() {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT select SUM(DECODE(menu_detail_code, '메뉴', menu_price, 0)) menu,\n"
					+ "       SUM(DECODE(menu_code, '가격', menu_price, 0)) price\n" + "FROM menu_detail mo\n"
					+ "join menu od on mo.menu = od.menu_num ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));

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
	public List<MenuDTO> listBestMenu() {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT menu,name,qty TO_CHAR(list,'YYYY-MM-DD')Best,"
					+ "  menu,name,qty,( menu,name,qty) tot, ( menu,name,qty)/3 ave" + " FROM menu"
					+ " WHERE INSTR(name,?) > 0" + "join menu od on mo.menu = od.menu_num ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));

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
	public List<MenuDTO> bestMenues() {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT menu,name,qty TO_CHAR(best,'YYYY-MM-DD')menu,"
					+ "  menu,name,qty,( menu,name,qty) tot, ( menu,name,qty)/3 ave" + " FROM menu"
					+ " WHERE INSTR(name,?) > 0" + "join menu od on mo.menu = od.menu_num ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));

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
	public List<MenuDTO> listLately() {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "INSERT INTO listLately(saledate, name,menu,qty) VALUES (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));
				dto.setSaledate("saledate");
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
	public List<SalesDTO> listToday() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "INSERT INTO listToday(saledate, name,menu,qty) VALUES (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));
				dto.setSaledate("saledate");
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
	public List<SalesDTO> listWeek() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "INSERT INTO listWeek(saledate, name,menu,qty) VALUES (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));
				dto.setSaledate("saledate");
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
	public List<SalesDTO> listMonth() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "INSERT INTO listMonth(saledate,name,menu,qty) VALUES (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));
				dto.setSaledate("saledate");
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
	public List<SalesDTO> listYear() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "INSERT INTO listYear(saledate,name,menu,qty) VALUES (?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setmenu(rs.getString("menu"));
				dto.setName(rs.getString("name"));
				dto.setQty(rs.getString("qty"));
				dto.setSaledate("saledate");
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
}
