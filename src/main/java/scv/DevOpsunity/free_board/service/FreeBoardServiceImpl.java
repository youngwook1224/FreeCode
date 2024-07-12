package scv.DevOpsunity.free_board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
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

}
