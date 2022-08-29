package ddCafe_Manage;

import java.sql.SQLException;
import java.util.List;

public interface MemberDAO {
	public List<MemberDTO> readMemberByName(String name); // 이름으로 회원 정보 읽기
	public MemberDTO readMemberByTel(String tel) throws SQLException; // 전화번호로 회원 정보 읽기
	public int updateMember(String name, String tel, int memberNum) throws SQLException; // 회원 정보 수정
	public List<MemberDTO> listMember(); // 회원 목록 확인
	public int deleteMember(String tel) throws SQLException; // 회원 탈퇴
}