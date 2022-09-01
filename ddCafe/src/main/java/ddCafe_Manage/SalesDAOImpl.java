package ddCafe_Manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.util.DBConn;

public class SalesDAOImpl implements SalesDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	public Map<String, Integer> countTakeOut() {
		Map<String, Integer> map = new HashMap<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT SUM(DECODE(takeout_togo, '매장', order_qty, 0)) store, "
					+ "       SUM(DECODE(takeout_togo, '포장', order_qty, 0)) takeout " + " FROM menu_order mo "
					+ " JOIN order_detail od ON mo.order_num = od.order_num ";

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
	public List<SalesDTO> listPanmai() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT m2.menu_name, m1.menu_size, SUM(order_qty) qty " + " FROM order_detail o1 "
					+ " JOIN menu_detail m1 ON o1.menu_detail_code = m1.menu_detail_code "
					+ " JOIN menu m2 ON m1.menu_code = m2.menu_code "
					+ " GROUP BY m1.menu_detail_code, m2.menu_name, m1.menu_size " + " ORDER BY qty DESC ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setMenu_name(rs.getString("menu_name"));
				dto.setMenu_size(rs.getString("menu_size"));
				dto.setQty(rs.getInt("qty"));

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
	public List<SalesDTO> totalPanmai() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT SUM(qty) total_qty FROM( " + " SELECT SUM(order_qty) qty " + " FROM order_detail o1 "
					+ " JOIN menu_detail m1 ON o1.menu_detail_code = m1.menu_detail_code "
					+ " JOIN menu m2 ON m1.menu_code = m2.menu_code "
					+ " GROUP BY m1.menu_detail_code, m2.menu_name, m1.menu_size " + " ORDER BY qty DESC) ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setQty(rs.getInt("total_qty"));

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
	public List<SalesDTO> bestMenues() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT menu_name FROM( "
					+ " SELECT menu_name,SUM(order_qty) qty ,RANK() OVER(ORDER BY SUM(order_qty) DESC) rank, m2.menu_code "
					+ " FROM order_detail o1 " + " JOIN menu_detail m1 ON o1.menu_detail_code = m1.menu_detail_code "
					+ " JOIN menu m2 ON m1.menu_code = m2.menu_code " + " GROUP BY menu_name, m2.menu_code "
					+ " )WHERE rank <=3 ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setMenu_name(rs.getString("menu_name"));

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
	public List<SalesDTO> listToday() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD') today, TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') today_price "
					+ " FROM payment p " + " JOIN menu_order mo ON p.order_num = mo.order_num "
					+ " WHERE TO_CHAR(order_date, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD') ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("today"));
				dto.setPrice(rs.getString("today_price"));

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
	public List<SalesDTO> listWeek() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD') week, TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') week_price, "
					+ " TO_CHAR(NEXT_DAY(SYSDATE , 1) -7, 'YYYY-MM-DD') day1, "
					+ " TO_CHAR(NEXT_DAY(SYSDATE-1, 7), 'YYYY-MM-DD') day2 " + " FROM payment p "
					+ " JOIN menu_order mo ON p.order_num = mo.order_num "
					+ " WHERE (order_date >= NEXT_DAY(SYSDATE, 1) -7) "
					+ " AND (order_date <= NEXT_DAY(SYSDATE-1, 7)) ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("week"));
				dto.setPrice(rs.getString("week_price"));
				dto.setWeek1(rs.getString("day1"));
				dto.setWeek2(rs.getString("day2"));

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
	public List<SalesDTO> listMonth() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(SYSDATE, 'MM') month , TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') month_price "
					+ "	FROM payment p " + "	JOIN menu_order mo ON p.order_num = mo.order_num "
					+ "	WHERE TO_CHAR(order_date, 'MM') = TO_CHAR(SYSDATE, 'MM') ";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("month"));
				dto.setPrice(rs.getString("month_price"));

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
	public List<SalesDTO> listYear() {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(SYSDATE, 'YYYY') year , TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') year_price "
					+ " FROM payment p " + " JOIN menu_order mo ON p.order_num = mo.order_num "
					+ " WHERE TO_CHAR(order_date, 'YYYY') = TO_CHAR(SYSDATE, 'YYYY')";

			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("year"));
				dto.setPrice(rs.getString("year_price"));

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
	public List<SalesDTO> selectToday(String saledate) {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS') day, "
					+ " TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') day_price " + " FROM payment p "
					+ " JOIN menu_order mo ON p.order_num = mo.order_num  "
					+ " WHERE TO_CHAR(order_date,'YYYYMMDD') = ? "
					+ " GROUP BY ROLLUP(TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS')) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, saledate);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("day"));
				dto.setPrice(rs.getString("day_price"));

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
	public List<SalesDTO> selectWeek(String saledate) {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS') week, "
					+ " TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') week_price" + " FROM payment p "
					+ " JOIN menu_order mo ON p.order_num = mo.order_num "
					+ " WHERE (order_date >= NEXT_DAY(TO_DATE(?,'YYYYMMDD'), 1) -7) "
					+ " AND (order_date <= NEXT_DAY(TO_DATE(?,'YYYYMMDD')-1, 7)) "
					+ " GROUP BY ROLLUP(TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS')) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, saledate);
			pstmt.setString(2, saledate);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("week"));
				dto.setPrice(rs.getString("week_price"));

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
	public List<SalesDTO> selectMonth(String saledate) {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS') month, "
					+ " TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') month_price " + " FROM payment p "
					+ " JOIN menu_order mo ON p.order_num = mo.order_num " + " WHERE TO_CHAR(order_date,'YYYYMM') = ? "
					+ " GROUP BY ROLLUP(TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS')) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, saledate);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("month"));
				dto.setPrice(rs.getString("month_price"));

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
	public List<SalesDTO> selectYear(String saledate) {
		List<SalesDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS') year, "
					+ " TO_CHAR(NVL(SUM(payment_price),0),'999,999,999') year_price " + " FROM payment p "
					+ " JOIN menu_order mo ON p.order_num = mo.order_num " + " WHERE TO_CHAR(order_date,'YYYY') = ? "
					+ " GROUP BY ROLLUP(TO_CHAR(order_date, 'YYYY-MM-DD HH24:MM:SS')) ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, saledate);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				SalesDTO dto = new SalesDTO();

				dto.setSaledate(rs.getString("year"));
				dto.setPrice(rs.getString("year_price"));

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
}
