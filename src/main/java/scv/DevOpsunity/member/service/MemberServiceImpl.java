package scv.DevOpsunity.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import scv.DevOpsunity.member.dao.MemberDAO;
import scv.DevOpsunity.member.dto.MemberDTO;

import java.util.List;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
   
   @Autowired
   private MemberDAO memberDAO;

    @Override
   public List listMembers() throws DataAccessException {
      List membersList = memberDAO.selectAllMembersList();
      return membersList;
   }

@Override
public void addMember(MemberDTO memberDTO) throws DataAccessException {
		
		memberDAO.insertMember(memberDTO);
	
}

@Override
public MemberDTO findMember(String id) throws DataAccessException {
	MemberDTO memberDTO = memberDAO.selectMemberById(id);
	return memberDTO;
}

@Override
public void updateMember(MemberDTO memberDTO) throws DataAccessException {
	memberDAO.updateMember(memberDTO);
	
}

@Override
public void delMember(String id) throws DataAccessException {
	memberDAO.delMember(id);
	
}

@Override
public MemberDTO login(MemberDTO memberDTO) throws DataAccessException {
	return memberDAO.loginCheck(memberDTO);
}

@Override
public int idCheck(String id) {
    int cnt = memberDAO.idCheck(id);
    return cnt;


    }
}




