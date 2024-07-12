package scv.DevOpsunity.free_board.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component("freeArticleDTO")
public class FreeArticleDTO {
	// mySql 컬럼 순서대로 해야 자동으로 편하게 쓰는 기능이 많다.
	private int articleNo;			// 글 번호
	private String title;			// 글 제목
	private String content;		// 작성 일자
	private String reply;
	private String id;				// 회원 아이디
	
	// 생성자
	public FreeArticleDTO() { //빈 생성자
		
	}
	public FreeArticleDTO(int articleNo, String title, String content, String imageFileName, Date writeDate, String id) {
		this.articleNo = articleNo;
		this.title = title;
		this.content = content;
		this.id = id;
	}

	// getter, setter (위에서 변수가 private 접근제한자니까 getter, setter 이용)
	public int getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(int articleNo) {
		this.articleNo = articleNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
}

