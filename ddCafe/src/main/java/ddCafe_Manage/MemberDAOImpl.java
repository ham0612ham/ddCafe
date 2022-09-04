package ddCafe_Manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;

public class MemberDAOImpl implements MemberDAO{
	Connection conn = DBConn.getConnection();

	@Override
	public List<MemberDTO> readMemberByName(String name) {
		List<MemberDTO> list = new ArrayList<>();
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql; 
		
		try {
			sql = " SELECT member_code, member_name, member_tel, member_date FROM member "
					+" WHERE INSTR(member_name, ?) > 0 "
					+" ORDER BY member_code";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new MemberDTO();
				
				dto.setMemberNum(rs.getInt("member_code"));
				dto.setName(rs.getString("member_name"));
				dto.setTel(rs.getString("member_tel"));
				dto.setDate(rs.getDate("member_date").toString());
				
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
	public MemberDTO readMemberByTel(String tel) throws SQLException{
		MemberDTO dto = new MemberDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql; 
		
		try {
			sql = " SELECT member_code, member_name, member_tel, member_date FROM member "
					+" WHERE member_tel = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, tel);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setMemberNum(rs.getInt("member_code"));
				dto.setName(rs.getString("member_name"));
				dto.setTel(rs.getString("member_tel"));
				dto.setDate(rs.getDate("member_date").toString());
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode()==1400) {
				System.out.println("필수 입력사항을 입력하지 않았습니다.");
			} else { 
				System.out.println(e.toString());
			}
			throw e;
		
			
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
		return dto;
	}

	@Override
	public int updateMember(String name, String tel, int memberNum) throws SQLException {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql;
		
		try {
			sql = " UPDATE member SET member_name = ?, member_tel = ? "
					+" WHERE member_code = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, tel);
			pstmt.setInt(3, memberNum);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode()==1400) {
				System.out.println("필수 입력사항을 입력하지 않았습니다.");
			} else { 
				System.out.println(e.toString());
			}
			throw e;
			
		} catch (SQLDataException e) {
			if(e.getErrorCode()==1840 || e.getErrorCode()==1861) {
				System.out.println("날짜 형식 입력 오류 입니다.");
			} else {
				System.out.println(e.toString());
			}
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
	public List<MemberDTO> listMember() {
		List<MemberDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT member_code, member_name, member_tel, TO_CHAR(member_date,'YYYY-MM-DD') member_date FROM member "
					+ " WHERE member_tel IS NOT NULL "
					+ " ORDER BY member_code";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setMemberNum(rs.getInt("member_code"));
				dto.setName(rs.getString("member_name"));
				dto.setTel(rs.getString("member_tel"));
				dto.setDate(rs.getString("member_date"));
				
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
	public int deleteMember(String tel) throws SQLException {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql;
		
		try {
			sql = " DELETE FROM member WHERE member_tel = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, tel);
			
			result = pstmt.executeUpdate();
			
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
