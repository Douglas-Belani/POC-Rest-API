package entities;

import java.util.ArrayList;
import java.util.List;

public class TopLevelComment extends Comments {

    private List<SubLevelComment> subLevelComments = new ArrayList<>();

    public TopLevelComment() {

    }

    public TopLevelComment(Integer commentId, String text, User user, Rate rate) {
        super(commentId, text, user, rate);
    }

    public List<SubLevelComment> getSubLevelComments() {
        return subLevelComments;
    }

    public void addSubLevelComment(SubLevelComment subLevelComment) {
        if (subLevelComment == null) {
            throw new IllegalArgumentException("Comment must not be empty.");
        }
        subLevelComments.add(subLevelComment);
    }

    public void setSubLevelComments(List<SubLevelComment> subLevelComment) {
        if (subLevelComment == null) {
            throw new IllegalArgumentException("Comment must not be empty.");
        }
        subLevelComments = subLevelComment;
    }



}
