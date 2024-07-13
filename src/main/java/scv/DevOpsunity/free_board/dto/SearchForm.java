package scv.DevOpsunity.free_board.dto;

public class SearchForm {
    private String type;
    private String keyword;

    // getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}