package entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Objects;

public abstract class Comments {

    private Integer commentId;
    private String text;
    private User user;
    private Rate rate;

    public Comments() {

    }

    public Comments(Integer commentId, String text, User user, Rate rate) {
        this.commentId = commentId;
        this.text = text;
        this.user = user;
        this.rate = rate;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not be blank.");
        }

        if (text.length() > 120) {
            throw new IllegalArgumentException("Text must have less than 120 characters.");
        }

        this.text = text;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be empty.");
        }

        this.user = user;
    }

    public Rate getRate() {
        return this.rate;
    }

    public void setRate(Rate rate) {
        if (rate == null) {
            throw new IllegalArgumentException("Rate must not be empty.");
        }

        this.rate = rate;
    }

    @Override
    public String toString() {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            return writer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return Objects.equals(getCommentId(), comments.getCommentId()) &&
                Objects.equals(getText(), comments.getText()) &&
                Objects.equals(getUser(), comments.getUser()) &&
                Objects.equals(getRate(), comments.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getText(), getUser(), getRate());
    }
}
