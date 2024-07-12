package scv.DevOpsunity.free_board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;

import java.util.List;
import java.util.Map;


@Mapper
@Repository("freeBoardDAO")
public interface FreeBoardDAO {
	
	public List selectAllArticles(@Param("count") int count) throws DataAccessException;
	
	public int selectToArticles() throws DataAccessException;
	
	public int getNewArticleNo() throws DataAccessException;
	
	/* 하나의 이미 추가
	public void insertNewArticle(ArticleDTO articleDTO) throws DataAccessException;
	*/
	
	//여러개 이미지 추가
	public void insertNewArticle(Map articleMap) throws DataAccessException;
	
	public void insertNewImages(Map articleMap) throws DataAccessException; 
	
	public FreeArticleDTO selectArticle(int articleNo) throws DataAccessException;
	
	public List selectImageFileList(int articleNo) throws DataAccessException;
	
	//public void updateArticle(ArticleDTO articleDTO) throws DataAccessException;
	
	public void updateArticle(Map articleMap) throws DataAccessException;
	
	public void updateImage(Map articleMap) throws DataAccessException;
	
	public void deleteArticle(int articleNo) throws DataAccessException;
	
}
