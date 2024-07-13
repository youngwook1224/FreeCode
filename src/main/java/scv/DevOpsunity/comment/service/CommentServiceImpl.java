package scv.DevOpsunity.comment.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scv.DevOpsunity.comment.dao.CommentDAO;
import scv.DevOpsunity.comment.dto.CommentDTO;

import java.util.List;

@Service("")
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentDAO commentDAO;

    @Override
    public List<CommentDTO> readReply(int bno) throws Exception{
        return commentDAO.readReply(bno);
    }

    @Override
    public void writeReply(CommentDTO dto) throws Exception{
        commentDAO.writeReply(dto); //commentMapper.writeReply(dto);
    }


}
