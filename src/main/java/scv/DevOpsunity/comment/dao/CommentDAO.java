package scv.DevOpsunity.comment.dao;

import org.springframework.stereotype.Repository;
import scv.DevOpsunity.comment.dto.CommentDTO;

import java.util.List;

@Repository("")
public interface CommentDAO {
    
    //댓글조회
    public List<CommentDTO> readReply(int bno) throws Exception;

    //댓글작성
    public void writeReply (CommentDTO vo) throws Exception;


}
