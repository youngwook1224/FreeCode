package scv.DevOpsunity.comment.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface CommentController {

    public String replyWrite(@RequestParam("bno") int bno, @RequestParam("content") String content, @RequestParam("writer") String writer,
            RedirectAttributes rttr) throws Exception;
}
