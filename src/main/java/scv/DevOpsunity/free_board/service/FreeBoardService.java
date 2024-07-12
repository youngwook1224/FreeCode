package scv.DevOpsunity.free_board.service;

import org.springframework.dao.DataAccessException;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;

import java.util.Map;

public interface FreeBoardService {
	
	public Map listArticles(Map<String, Integer> pagingMap) throws DataAccessException;
	
	//한개의 이미지 추가
	 public int addArticle(FreeArticleDTO freeArticleDTO) throws DataAccessException;
	
	public FreeArticleDTO viewArticle(int feeArticleNo) throws DataAccessException;
	
	//한개의 이미지 글 수정
	public void modArticle(FreeArticleDTO freeArticleDTO) throws DataAccessException;
	
	public void removeArticle(int articleNo) throws DataAccessException;
	
}