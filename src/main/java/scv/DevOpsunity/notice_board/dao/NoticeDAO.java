package scv.DevOpsunity.notice_board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository("/NoticeBoardDAO")
public interface NoticeDAO {
	/*
	public List selectAllArticles(@Param("count") int count) throws DataAccessException;
	
	public int selectToArticles() throws DataAccessException;

	public int getNewArticleNo() throws DataAccessException;

	public void insertNewArticle(CodingArticleDTO articleDTO) throws DataAccessException;

	public CodingArticleDTO selectArticle(int articleNo) throws DataAccessException;

	public void updateArticle(CodingArticleDTO articleDTO) throws DataAccessException;
	
	public void deleteArticle(int articleNo) throws DataAccessException;

	*/
}
