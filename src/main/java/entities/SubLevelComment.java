package entities;

public class SubLevelComment extends Comments {

    private Integer topLevelCommentId;

    public SubLevelComment() {

    }

    public SubLevelComment(Integer topLevelCommentId) {
        this.topLevelCommentId = topLevelCommentId;
    }

    public SubLevelComment(Integer commentId, String text, User user, Rate rate, Integer topLevelCommentId) {
        super(commentId, text, user, rate);
        this.topLevelCommentId = topLevelCommentId;
    }

    public Integer getTopLevelCommentId() {
        return topLevelCommentId;
    }

    public void setTopLevelCommentId(Integer topLevelCommentId) {
        this.topLevelCommentId = topLevelCommentId;
    }
}
