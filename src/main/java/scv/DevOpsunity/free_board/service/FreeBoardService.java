package scv.DevOpsunity.free_board.service;

import org.springframework.dao.DataAccessException;

import java.util.Map;

public interface FreeBoardService {
	
	public Map listArticles(Map<String, Integer> pagingMap) throws DataAccessException;
	
	/* 한개의 이미지 추가
	 public int addArticle(ArticleDTO articleDTO) throws DataAccessException;
	 */
	
	//여러개 이미지 추가
	public int addArticle(Map articleMap) throws DataAccessException;
	
	//public ArticleDTO viewArticle(int articleNo) throws DataAccessException;
	
	public Map viewArticle(int articleNo) throws DataAccessException;
	
	/*한개의 이미지 글 수정
	public void modArticle(ArticleDTO articleDTO) throws DataAccessException;*/
	
	//여러개의 이미지 글 수정
	public void modArticle(Map articleMap) throws DataAccessException;
	
	public void removeArticle(int articleNo) throws DataAccessException;
	
}