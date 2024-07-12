package scv.DevOpsunity.free_board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import scv.DevOpsunity.comment.dto.CommentDTO;
import scv.DevOpsunity.comment.service.CommentService;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;
import scv.DevOpsunity.free_board.dto.ImageDTO;
import scv.DevOpsunity.free_board.service.FreeBoardService;
import scv.DevOpsunity.member.dto.MemberDTO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@Controller("freeBoardController")
public class FreeBoardControllerImpl implements FreeBoardController {
	private static String ARTICLE_IMG_REPO="C:\\kimchangmin\\fileUpload";

	@Autowired
	private FreeBoardService boardService;

	@Autowired
	private FreeArticleDTO articleDTO;

	//댓글전용 자동주입
	@Autowired
	private CommentService commentService;

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
		return mav;
	}

	@Override
	@GetMapping("/board/freeArticleForm.do")
	public ModelAndView articleForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav=new ModelAndView();
		/* 한 개의 이미지 추가
		mav.setViewName("/free_board/freeArticleForm");
		*/
		// 여러 개의 이미지 추가
		mav.setViewName("/free_board/freeArticleForm_multi");
		return mav;
	}

	//글쓰기에 여러 개의 이미지 추가
	@Override
	@PostMapping("/board/freeAddArticle.do")
	public ModelAndView addArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		String imageFileName=null;
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap=new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()) {
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}
		List<String> fileList=multiFileUpload(multipartRequest);
		List<ImageDTO> imageFileList=new ArrayList<ImageDTO>();
		if(fileList != null && fileList.size() != 0) {
			for(String fileName : fileList) {
				ImageDTO imageDTO=new ImageDTO();
				imageDTO.setImageFileName(fileName);
				imageFileList.add(imageDTO);
			}
			articleMap.put("imageFileList", imageFileList);
		}
		HttpSession session = multipartRequest.getSession();
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String id = memberDTO.getId();
		articleMap.put("id",id);
		try {
			int articleNo=boardService.addArticle(articleMap);
			if(imageFileList != null && imageFileList.size() != 0) {
				for(ImageDTO imageDTO : imageFileList) {
					imageFileName=imageDTO.getImageFileName();

					// FileUtils.moveFileToDirectory(srcFile, destDir, true);
					Path srcFile = Paths.get(ARTICLE_IMG_REPO, "temp", imageFileName);
					Path destDir = Paths.get(ARTICLE_IMG_REPO, String.valueOf(articleNo));
					Path targetFile = destDir.resolve(imageFileName);

					Files.createDirectories(destDir);  // 대상 디렉토리가 없을 경우 생성
					Files.move(srcFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}catch (Exception e) {
			//글쓰기 수행 중 오류
			if(imageFileList != null && imageFileList.size() != 0) {
				for(ImageDTO imageDTO : imageFileList) {
					imageFileName=imageDTO.getImageFileName();
					File srcFile=new File(ARTICLE_IMG_REPO + "\\temp\\" + imageFileName);
					//오류 발생 시 temp폴더의 이미지를 모두 삭제
					srcFile.delete();
				}
			}
		}
		ModelAndView mav= new ModelAndView("redirect:/board/freeListArticles.do");
		return mav;
	}

	//여러 개의 이미지 상세 글보기
	@Override
	@GetMapping("/board/freeViewArticle.do")
	public ModelAndView viewArticle(@RequestParam("articleNo") int articleNo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map articleMap=boardService.viewArticle(articleNo);
		ModelAndView mav=new ModelAndView();
		mav.setViewName("/free_board/freeViewArticle_multi");
		mav.addObject("articleMap",articleMap);

		//댓글 보기
		List<CommentDTO> commentList = commentService.readReply(articleDTO.getArticleNo());
		model.addAttribute("commentList",commentList);	// 임플리먼츠 매개변수에 Model model 추가해야함


		return mav;
	}

	//여러 개의 이미지 글 수정하기
	@Override
	@GetMapping("/board/freeModArticle.do")
	public ModelAndView modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		String imageFileName=null;
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap=new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()) {
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			//System.out.println(name + ":" + value);
			articleMap.put(name, value);
		}
		List<String> fileList=multiFileUpload(multipartRequest);
		String articleNo=(String)articleMap.get("articleNo");
		List<ImageDTO> imageFileList=new ArrayList<ImageDTO>();
		int modityNumber=0;
		if(fileList != null && fileList.size() != 0) {
			for(String fileName : fileList) {
				modityNumber++;
				ImageDTO imageDTO=new ImageDTO();
				System.out.println("이미지 이름 : " + fileName);
				imageDTO.setImageFileName(fileName);
				imageDTO.setImageFileNo(Integer.parseInt((String)articleMap.get("imageFileNo"+modityNumber)));
				imageFileList.add(imageDTO);
			}
			articleMap.put("imageFileList", imageFileList);
		}
		HttpSession session = multipartRequest.getSession();
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		String id = memberDTO.getId();
		articleMap.put("id",id);
		try {
			boardService.modArticle(articleMap);
			if(imageFileList != null && imageFileList.size() != 0) {
				//int cnt=imageFileList.size()-1;
				int cnt=0;
				for(ImageDTO imageDTO : imageFileList) {
					cnt++;
					imageFileName=imageDTO.getImageFileName();
					if(imageFileName != null && imageFileName != "") {
						File srcFile=new File(ARTICLE_IMG_REPO + "\\temp\\" + imageFileName);
						File destDir=new File(ARTICLE_IMG_REPO + "\\" + articleNo);
						// FileUtils.moveFileToDirectory(srcFile, destDir, true);
						Path source = srcFile.toPath();
						Path target = destDir.toPath().resolve(source.getFileName());
						Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
						String originalFileName=(String)articleMap.get("originalFileName" + cnt);
						System.out.println("이전 이미지 : " + originalFileName);
						File oldFile=new File(ARTICLE_IMG_REPO + "\\" + articleNo + "\\" + originalFileName);
						oldFile.delete();
					}

				}
			}
		}catch (Exception e) {
			//글쓰기 수행 중 오류
			/*if(imageFileList != null && imageFileList.size() != 0) {
				for(ImageDTO imageDTO : imageFileList) {
					imageFileName=imageDTO.getImageFileName();
					File srcFile=new File(ARTICLE_IMG_REPO + "\\temp\\" + imageFileName);
					//오류 발생 시 temp폴더의 이미지를 모두 삭제
					srcFile.delete();
				}
			}*/
			e.printStackTrace();
		}
		ModelAndView mav= new ModelAndView("redirect:/board/freeListArticles.do");
		return mav;
	}

	@Override
	@PostMapping("/board/freeRemoveArticle.do")
	public ModelAndView removeArticle(@RequestParam("articleNo") int articleNo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		boardService.removeArticle(articleNo);
		File imgDir=new File(ARTICLE_IMG_REPO + "\\" + articleNo);
		if(imgDir.exists()) {
			FileUtils.deleteDirectory(imgDir);
		}
		ModelAndView mav= new ModelAndView("redirect:/board/freeListArticles.do");
		return mav;
	}

	//한 개 이미지 파일 업로드
	private String fileUpload(MultipartHttpServletRequest multipartRequest) throws Exception {
		String imageFileName=null;
		Iterator<String> fileNames=multipartRequest.getFileNames();
		while(fileNames.hasNext()) {
			String fileName=fileNames.next();
			MultipartFile mFile=multipartRequest.getFile(fileName);
			imageFileName=mFile.getOriginalFilename();
			File file=new File(ARTICLE_IMG_REPO+"\\"+ fileName);
			if(mFile.getSize() != 0) {
				if(! file.exists()) {
					if(file.getParentFile().mkdir()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(ARTICLE_IMG_REPO + "\\temp\\" + imageFileName));
			}
		}
		return imageFileName;
	}

	//여러개의 이미지 파일 업로드
	private List<String> multiFileUpload(MultipartHttpServletRequest multipartRequest) throws Exception {
		List<String> fileList=new ArrayList<String>();
		Iterator<String> fileNames=multipartRequest.getFileNames();
		while(fileNames.hasNext()) {
			String fileName=fileNames.next();
			MultipartFile mFile=multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			fileList.add(originalFileName);
			File file=new File(ARTICLE_IMG_REPO+"\\"+ fileName);
			if(mFile.getSize() != 0) {
				if(! file.exists()) {
					if(file.getParentFile().mkdir()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(ARTICLE_IMG_REPO + "\\temp\\" + originalFileName));
			}
		}
		return fileList;
	}

















}


