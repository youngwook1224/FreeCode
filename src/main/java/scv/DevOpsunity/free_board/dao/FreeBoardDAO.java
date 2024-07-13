package scv.DevOpsunity.free_board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import scv.DevOpsunity.free_board.dto.FreeArticleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Mapper
@Repository("freeBoardDAO")
public interface FreeBoardDAO {

	public List selectAllArticles(@Param("count") int count) throws DataAccessException;

	public int selectToArticles() throws DataAccessException;

	public int getNewArticleNo() throws DataAccessException;

	public void insertNewArticle(FreeArticleDTO freeArticleDTO) throws DataAccessException;

	public FreeArticleDTO selectArticle(int freeArticleNo) throws DataAccessException;

	public void updateArticle(FreeArticleDTO freeArticleDTO) throws DataAccessException;

	public void deleteArticle(int freeArticleNo) throws DataAccessException;

	public ArrayList<FreeArticleDTO> selectSearch(@Param("type")String type, @Param("keyword")String keyword, @Param("s") int start, @Param("e") int end)throws Exception;

	public int selectSearchCount(@Param("type")String type,@Param("keyword")String keyword);
	
}
