package scv.DevOpsunity.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("common.do")
public class Common {
    private static String ARTICLE_IMG_REPO = "D:\\kim_dohyeon\\fileupload"; //각자 레포지토리에 맞게 바꾸기

    public Common() {

    }



    //이미지 업로드
    @GetMapping({"/download.do"})
    public void fileDown(@RequestParam("articleNo") String articleNO, @RequestParam("imageFileName") String imageFileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        OutputStream outs = response.getOutputStream();
        String path = ARTICLE_IMG_REPO + "\\" + articleNO + "\\" + imageFileName;
        File imageFile = new File(path);
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("content-disposition", "attachmnet;fileName=" + imageFileName);
        FileInputStream fis = new FileInputStream(imageFile);
        byte[] buffer = new byte[8192];

        while(true) {
            int count = fis.read(buffer);
            if (count == -1) {
                fis.close();
                outs.close();
                return;
            }

            outs.write(buffer, 0, count);
        }
    }



    // 댓글
    @GetMapping({"/comment.do"})
    public void comment(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }
}