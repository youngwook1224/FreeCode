package scv.DevOpsunity.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scv.DevOpsunity.comment.dto.CommentDTO;
import scv.DevOpsunity.comment.service.CommentService;

@Controller
@RequestMapping("/comment")
public class CommentControllerImpl implements CommentController{

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/commentWrite")
    public String replyWrite(@RequestParam("bno") int bno, @RequestParam("content") String content, @RequestParam("writer") String writer,
            RedirectAttributes rttr) throws Exception {

        CommentDTO dto = new CommentDTO();
        dto.setBno(bno);
        dto.setContent(content);
        dto.setWriter(writer);

        commentService.writeReply(dto);

        rttr.addAttribute("bno", bno);
        return "redirect:/board/readView";
    }
}
