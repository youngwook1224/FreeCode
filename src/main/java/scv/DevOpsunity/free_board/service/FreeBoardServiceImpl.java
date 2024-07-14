package scv.DevOpsunity.free_board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import scv.DevOpsunity.free_board.dao.FreeBoardDAO;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("freeBoardService")
public class FreeBoardServiceImpl implements FreeBoardService {
	
	@Autowired
	private FreeBoardDAO freeBoardDAO;
	
	@Override
	public Map listArticles(Map<String , Integer> pagingMap) throws DataAccessException {
		Map articleMap = new HashMap();
		int section= pagingMap.get("section");
		int pageNum = pagingMap.get("pageNum");
		int count = (section-1)*100+(pageNum-1)*10;
		List<FreeArticleDTO> articlesList = freeBoardDAO.selectAllArticles(count);
		int totArticles = freeBoardDAO.selectToArticles();
		articleMap.put("articlesList", articlesList);
		//articleMap.put("totArticles", totArticles);
		articleMap.put("totArticles", totArticles);
		return articleMap;
	}

	//한개의 이미지 추가
	@Override
	public int addArticle(FreeArticleDTO freeArticleDTO) throws DataAccessException {
		int freeArticleNo = freeBoardDAO.getNewArticleNo();
		freeArticleDTO.setFreeArticleNo(freeArticleNo);
		freeBoardDAO.insertNewArticle(freeArticleDTO);
		return freeArticleNo;
	}

	@Override
	public FreeArticleDTO viewArticle(int freeArticleNo) throws DataAccessException {
		FreeArticleDTO freeArticleDTO = freeBoardDAO.selectArticle(freeArticleNo);
		return freeArticleDTO;
	}
	

	@Override
	public void modArticle(FreeArticleDTO freeArticleDTO) throws DataAccessException {
		freeBoardDAO.updateArticle(freeArticleDTO);
		
	}

	@Override
	public void removeArticle(int freeArticleNo) throws DataAccessException {
		freeBoardDAO.deleteArticle(freeArticleNo);
		
	}

	public void selectSearch(Model model, String type, String keyword, int num) throws Exception {
		// 페이지당 게시글 수를 10으로 설정
		int pageLetter = 10;

		// 검색된 전체 게시글 수를 가져온다
		int allCount;
		allCount = freeBoardDAO.selectSearchCount(type, keyword);

		// 전체 페이지 수를 계산한다
		int repeat = allCount / pageLetter;
		if (allCount % pageLetter != 0) {
			repeat += 1; // 나머지가 있으면 한 페이지를 추가한다
		}

		// 시작번호와 끝번호를 계산한다
		int end = num * pageLetter;
		int start = end + 1 - pageLetter;

		// 모델에 반복 횟수(repeat)를 추가한다
		model.addAttribute("repeat", repeat);

		// 검색된 게시글 리스트를 모델에 추가한다
		model.addAttribute("boardList", freeBoardDAO.selectSearch(type, keyword, start, end));
	}

	@Override
	public void boardList(Model model, int num) throws Exception {
		// 페이지당 게시글 수를 10으로 설정한다
		int pageLetter = 10; // 페이지당 게시글 수

		// 전체 게시글 수를 가져온다
		int allCount = freeBoardDAO.selectToArticles(); // 전체 게시글 수

		// 전체 페이지 수를 계산한다
		int repeat = allCount / pageLetter;
		if (allCount % pageLetter != 0) {
			repeat += 1; // 나머지가 있으면 한 페이지를 추가한다
		}

		// 시작번호와 끝번호를 계산한다
		int end = num * pageLetter;
		int start = end + 1 - pageLetter;

		// 전체 게시글 리스트를 가져온다. (MyBatis는 0 기반 인덱스를 사용하므로 start - 1)
		List<FreeArticleDTO> boardList = freeBoardDAO.selectAllArticles(start - 1);

		// 모델에 반복 횟수(repeat)와 게시글 리스트(boardList), 현재 페이지 번호(num)를 추가한다
		model.addAttribute("repeat", repeat);
		model.addAttribute("boardList", boardList);
		model.addAttribute("num", num);
	}


}
