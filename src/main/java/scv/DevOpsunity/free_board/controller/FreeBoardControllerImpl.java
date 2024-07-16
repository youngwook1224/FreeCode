package scv.DevOpsunity.free_board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import scv.DevOpsunity.comment.dto.CommentDTO;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;
import scv.DevOpsunity.free_board.dto.SearchForm;
import scv.DevOpsunity.member.dto.MemberDTO;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;
import scv.DevOpsunity.free_board.dto.SearchForm;
import scv.DevOpsunity.free_board.service.FreeBoardService;
import scv.DevOpsunity.member.dto.MemberDTO;

import java.io.File;
import java.util.*;


@Controller("freeBoardController")
public class FreeBoardControllerImpl implements FreeBoardController {
    private static String ARTICLE_IMG_REPO="C:\\kimchangmin\\fileUpload";

    @Autowired
    private FreeBoardService boardService;

    @Autowired
    private FreeArticleDTO articleDTO;


    @Override
    @GetMapping("/board/freeListArticles.do")
    public ModelAndView listArticles(
            @RequestParam(value = "section", required = false) String _section,
            @RequestParam(value ="pageNum", required = false) String _pageNum,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        int section=Integer.parseInt((_section == null)?"1":_section);
        int pageNum=Integer.parseInt((_pageNum == null)?"1":_pageNum);
        Map<String, Integer> pagingMap=new HashMap<String, Integer>();
        pagingMap.put("section", section);
        pagingMap.put("pageNum", pageNum);
        Map articleMap=boardService.listArticles(pagingMap);
        articleMap.put("section", section);
        articleMap.put("pageNum", pageNum);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("/free_board/freeListArticles");
        mav.addObject("articleMap",articleMap);
        mav.addObject("searchForm", new SearchForm());

        return mav;
    }

    @Override
    @GetMapping("/board/freeArticleForm.do")
    public ModelAndView articleForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav=new ModelAndView();

        mav.setViewName("/free_board/freeArticleForm");
        return mav;
    }

    // 글쓰기에 한 개의 이미지 추가
    @Override
    @PostMapping("/board/freeAddArticle.do")
    public ModelAndView addArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
            throws Exception {
        try {
            multipartRequest.setCharacterEncoding("utf-8");
            Map<String, Object> articleMap = new HashMap<String, Object>();
            Enumeration enu = multipartRequest.getParameterNames();
            while (enu.hasMoreElements()) {
                String name = (String) enu.nextElement();
                String value = multipartRequest.getParameter(name);
                articleMap.put(name, value);
            }
            String freeImageFileName = fileUpload(multipartRequest);
            String freeTitle = (String) articleMap.get("freeTitle");
            String freeContent = (String) articleMap.get("freeContent");

            if (freeTitle == null || freeTitle.trim().isEmpty()) {
                ModelAndView mav = new ModelAndView("/free_board/freeArticleForm");
                mav.addObject("errorMessage", "제목은 비워둘 수 없습니다.");
                return mav;
            }

            articleDTO.setFreeTitle(freeTitle);
            articleDTO.setFreeContent(freeContent);
            articleDTO.setFreeImageFileName(freeImageFileName);
            HttpSession session = multipartRequest.getSession();
            MemberDTO memberDTO = (MemberDTO) session.getAttribute("member");
            String id = memberDTO.getId();
            articleDTO.setId(id);
            int freeArticleNo = boardService.addArticle(articleDTO);
            if (freeImageFileName != null && freeImageFileName.length() != 0) {
                File srcFile = new File(ARTICLE_IMG_REPO + "\\temp\\" + freeImageFileName);
                File destDir = new File(ARTICLE_IMG_REPO + "\\" + freeArticleNo);
                System.out.println("Source file exists: " + srcFile.exists());
                System.out.println("Destination directory created: " + destDir.mkdirs());
                FileUtils.moveFileToDirectory(srcFile, destDir, true);
                System.out.println("File moved to: " + new File(destDir, freeImageFileName).getAbsolutePath());
                System.out.println("File exists after move: " + new File(destDir, freeImageFileName).exists());

            }
            ModelAndView mav = new ModelAndView("redirect:/board/freeListArticles.do");
            return mav;
        } catch (Exception e) {
            ModelAndView mav = new ModelAndView("/free_board/freeArticleForm");
            mav.addObject("errorMessage", e.getMessage());
            return mav;
        }
    }

    //한 개의 이미지 상세 글보기
    @Override
    @GetMapping("/board/freeViewArticle.do")
    public ModelAndView viewArticle(@RequestParam("freeArticleNo") int freeArticleNo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        articleDTO=boardService.viewArticle(freeArticleNo);
        ModelAndView mav=new ModelAndView();
        mav.setViewName("/free_board/freeViewArticle");
        mav.addObject("article",articleDTO);
        return mav;
    }

    // 한 개의 이미지 글 수정하기
    @Override
    @PostMapping("/board/freeArticleModArticle.do")
    public ModelAndView modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
            throws Exception {
        multipartRequest.setCharacterEncoding("utf-8");
        Map<String, Object> articleMap=new HashMap<String, Object>();
        Enumeration enu=multipartRequest.getParameterNames();
        while(enu.hasMoreElements()) {
            String name=(String)enu.nextElement();
            String value=multipartRequest.getParameter(name);
            articleMap.put(name, value);
        }
        String freeImageFileName=fileUpload(multipartRequest);
        String freeTitle=(String)articleMap.get("freeTitle");
        String freeContent=(String)articleMap.get("freeContent");
        String freeArticleNo=(String)articleMap.get("freeArticleNo");
        articleDTO.setFreeTitle(freeTitle);
        articleDTO.setFreeContent(freeContent);
        articleDTO.setFreeImageFileName(freeImageFileName);
        HttpSession session = multipartRequest.getSession();
        MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
        String id = memberDTO.getId();
        articleDTO.setId(id);
        articleDTO.setFreeArticleNo(Integer.parseInt(freeArticleNo));
        boardService.modArticle(articleDTO);

        if(freeImageFileName != null && freeImageFileName.length() != 0) {
            File srcFile=new File(ARTICLE_IMG_REPO + "\\temp\\" + freeImageFileName);
            File destDir=new File(ARTICLE_IMG_REPO + "\\" + freeArticleNo);
            FileUtils.moveFileToDirectory(srcFile, destDir, true);
            String originalFileName=(String)articleMap.get("originalFileName");
            File oldFile=new File(ARTICLE_IMG_REPO + "\\" + freeArticleNo + "\\" + originalFileName);
            oldFile.delete();
        }
        ModelAndView mav= new ModelAndView("redirect:/board/freeViewArticle.do?freeArticleNo=" + freeArticleNo);
        return mav;
    }

    @Override
    @PostMapping("/board/freeRemoveArticle.do")
    public ModelAndView removeArticle(@RequestParam("freeArticleNo") int freeArticleNo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        boardService.removeArticle(freeArticleNo);
        File imgDir=new File(ARTICLE_IMG_REPO + "\\" + freeArticleNo);
        if(imgDir.exists()) {
            //FileUtils.deleteDirectory(imgDir);
        }
        ModelAndView mav= new ModelAndView("redirect:/board/freeListArticles.do");
        return mav;
    }

    //한 개 이미지 파일 업로드
    private String fileUpload(MultipartHttpServletRequest multipartRequest) throws Exception {
        String freeImageFileName=null;
        Iterator<String> fileNames=multipartRequest.getFileNames();
        while(fileNames.hasNext()) {
            String fileName=fileNames.next();
            MultipartFile mFile=multipartRequest.getFile(fileName);
            freeImageFileName=mFile.getOriginalFilename();
            File file=new File(ARTICLE_IMG_REPO+"\\"+ fileName);
            if(mFile.getSize() != 0) {
                if(! file.exists()) {
                    if(file.getParentFile().mkdir()) {
                        file.createNewFile();
                    }
                }
                mFile.transferTo(new File(ARTICLE_IMG_REPO + "\\temp\\" + freeImageFileName));
            }
        }
        return freeImageFileName;
    }

    @GetMapping("/board/freeReview.do")
    public String review(Model model,
                         @RequestParam(value="type", required=false) String type,
                         @RequestParam(value="keyword", required=false) String keyword,
                         @RequestParam(required = false, defaultValue = "1") int num) throws Exception {
        Map<String, Object> articleMap = new HashMap<>();

        if(type != null && keyword != null && !keyword.trim().isEmpty()) {
            boardService.selectSearch(model, type, keyword, num);
            List<FreeArticleDTO> articlesList = (List<FreeArticleDTO>) model.getAttribute("boardList");
            articleMap.put("articlesList", articlesList);
        } else {
            boardService.boardList(model, num);
            List<FreeArticleDTO> articlesList = (List<FreeArticleDTO>) model.getAttribute("boardList");
            articleMap.put("articlesList", articlesList);
        }

        // 페이징 정보 추가
        articleMap.put("section", 1); // 섹션 정보가 필요하다면 적절히 계산해서 넣어주세요
        articleMap.put("pageNum", num);

        int totArticles = (int) model.getAttribute("repeat") * 10; // 한 페이지당 10개 게시글로 가정
        articleMap.put("totArticles", totArticles);

        // articleMap을 모델에 추가
        model.addAttribute("articleMap", articleMap);

        // Add searchForm to the model
        model.addAttribute("searchForm", new SearchForm());

        return "/free_board/freeListArticles";
    }//method 종료


}