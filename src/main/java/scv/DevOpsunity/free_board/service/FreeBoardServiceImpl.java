package scv.DevOpsunity.free_board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import scv.DevOpsunity.free_board.dao.FreeBoardDAO;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;
import scv.DevOpsunity.free_board.dto.ImageDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
		articleMap.put("totArticles", 324);
		return articleMap;
	}
	
	//여러개의 이미지 추가
	@Override
	public int addArticle(Map articleMap) throws DataAccessException {
		int articleNo = freeBoardDAO.getNewArticleNo();
		articleMap.put("articleNo",articleNo);
		freeBoardDAO.insertNewArticle(articleMap);
		if(articleMap.get("imageFileList") != null) {
			freeBoardDAO.insertNewImages(articleMap);
		}
		return articleNo;
	}
	
		//여러개 이미지 상세 글보기
	@Override
	public Map viewArticle(int articleNo) throws DataAccessException {
		Map articleMap = new HashMap();
		FreeArticleDTO articleDTO = freeBoardDAO.selectArticle(articleNo);
		List<ImageDTO> imageFileList = freeBoardDAO.selectImageFileList(articleNo);
		articleMap.put("article", articleDTO);
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}

	@Override
	public void modArticle(Map articleMap) throws DataAccessException {
		freeBoardDAO.updateArticle(articleMap);
		freeBoardDAO.updateImage(articleMap);
		
	}

	@Override
	public void removeArticle(int articleNo) throws DataAccessException {
		freeBoardDAO.deleteArticle(articleNo);
		
	}

}
