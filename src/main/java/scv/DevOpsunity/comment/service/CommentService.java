package scv.DevOpsunity.comment.service;

import jakarta.servlet.http.HttpServletRequest;
import scv.DevOpsunity.comment.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    //댓글조회
    public List<CommentDTO> readReply(int bno) throws Exception;

    //댓글작성
    public void writeReply (CommentDTO dto) throws Exception;

}
