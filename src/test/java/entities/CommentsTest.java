package entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class CommentsTest {

    private Comments comments;

    @Before
    public void initialize() {
        this.comments = new TopLevelComment(1, "text", null,
                null);
    }

    @Test
    public void test_setText_Should_Throw_IllegalArgumentException_When_Null_Text_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                comments.setText(null));
        Assert.assertEquals("Text must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setText_Should_Throw_IllegalArgumentException_When_Blank_Text_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                comments.setText(""));
        Assert.assertEquals("Text must not be blank.", thrown.getMessage());
    }

    @Test
    public void test_setText_Should_Throw_IllegalArgumentException_When_Text_Is_Larger_Than_120_Characters() {
        StringBuilder sb = new StringBuilder();
        while (sb.length() <= 120) {
            sb.append("a");
        }

        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                comments.setText(sb.toString()));
        Assert.assertEquals("Text must have less than 120 characters.", thrown.getMessage());
    }

    @Test
    public void test_setText_Should_Successfully_Change_Text_For_Non_Null_And_Non_Blank_Text_With_Less_Than_120_Characters() {
        String newText = "newText";
        comments.setText(newText);
        Assert.assertEquals(newText, comments.getText());
    }

    @Test
    public void test_setUser_Should_Throw_IllegalArgumentException_When_Null_User_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                comments.setUser(null));
        Assert.assertEquals("User must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_setUser_Should_Successfully_Change_User_When_Non_Null_User_Is_Passed() {
        User user = new User(1, "fullName", "111111111-11",
                "email@gmail.com", "password", LocalDate.now());
        comments.setUser(user);
        Assert.assertEquals(user, comments.getUser());
    }

    @Test
    public void test_setRate_Should_Throw_IllegalArgumentException_When_Null_Rate_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                comments.setRate(null));
        Assert.assertEquals("Rate must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_setRate_Should_Successfully_Change_Rate_When_Non_Null_Rate_Is_Passed() {
        Rate rate = new Rate(1, 1, 4);
        comments.setRate(rate);
        assertThat(rate, samePropertyValuesAs(comments.getRate()));
    }

    @Test
    public void test_addCommentToAnswers_Should_Throw_IllegalArgumentException_When_Null_SubLevelComment_Is_Passed() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                ((TopLevelComment)comments).addSubLevelComment(null));
        Assert.assertEquals("Comment must not be empty.", thrown.getMessage());
    }

    @Test
    public void test_addCommentToAnswers_Should_Successfully_Add_Comment_To_Answers_When_Non_Null_Comment_Is_Passed() {
        Comments comments2 = new SubLevelComment(2, "text",
                comments.getUser(), new Rate(2, 0, 0), comments.getCommentId());
        Assert.assertEquals(0, ((TopLevelComment) comments).getSubLevelComments().size());
        ((TopLevelComment) comments).addSubLevelComment((SubLevelComment) comments2);
        Assert.assertEquals(1, ((TopLevelComment) comments).getSubLevelComments().size());
        assertThat(comments2, samePropertyValuesAs(((TopLevelComment) comments).getSubLevelComments().get(0)));

    }

}
