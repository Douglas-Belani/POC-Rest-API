package dao.impl;

import connection.H2Connection;
import dao.ICommentDao;
import entities.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

public class CommentDaoImplTest {

   private ICommentDao commentsDao;

    @Before
    public void initialize() {
        this.commentsDao = new CommentDaoImpl(H2Connection.getH2DatabaseConnection());
    }

    @Test
    public void test_getCommentById_Should_Return_Comment_Id_1() {
        User user1 = new User(1, "user1", "111111111-11", "user1@gmail.com",
                "user3Password", LocalDate.of(2005, 8, 10));
        User user3 = new User(3, "user3", "333333333-33",
                "user3@gmail.com", "user3Password",
                LocalDate.of(2004, 7, 23));
        Rate rate4 = new Rate(4, 1, 0);
        Rate rate5 = new Rate(5, 0, 0);
        Comments expectedComment = new TopLevelComment(1, "comment1",
                user3, rate4);
        SubLevelComment subLevelComment = new SubLevelComment(2, "comment 1 reply", user1, rate5,
                1);
        ((TopLevelComment) expectedComment).addSubLevelComment(subLevelComment);

        Comments comments = commentsDao.getCommentById(1);
        Assert.assertEquals(expectedComment, comments);
    }

    @Test
    public void test_getCommentById_Should_Return_Null_For_Nonexistent_Comment_Id() {
        int nonExistentCommentId = 20;
        Comments comments = commentsDao.getCommentById(nonExistentCommentId);

        Assert.assertNull(comments);
    }

    @Test
    public void test_InsertComment_Should_Return_Comment_Id() {
        User user = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));
        Rate rate10 = new Rate(10, 0, 0);
        Comments newComments = new TopLevelComment(null, "test", user, rate10);
        int productId = 1;

        Integer commentId = commentsDao.insertComment(newComments, productId);
        newComments.setCommentId(commentId);

        Comments comments = commentsDao.getCommentById(commentId);

        assertThat(newComments, samePropertyValuesAs(comments));
    }

    @Test
    public void test_InsertComment_Should_Return_Comment_Id_For_A_Reply_To_Comment_Id_5() {
        Integer commentId = 5;

        User user2 = new User(2, "user2", "222222222-22",
                "user2@gmail.com", "user2Password",
                LocalDate.of(2002, 10, 10));
        Rate rate10 = new Rate(10, 0, 0);
        Comments replyToComment5 = new SubLevelComment(null, "answer comment3",
                user2, rate10, commentId);

        int productId = 3;
        Integer replyToComment3Id = commentsDao.insertComment(replyToComment5, productId);
        replyToComment5.setCommentId(replyToComment3Id);

        Comments reply = commentsDao.getCommentById(replyToComment3Id);

        assertThat(replyToComment5, samePropertyValuesAs(reply));

    }

    @Test
    public void testUpdateComment() {
        Comments comments = commentsDao.getCommentById(1);

        Assert.assertEquals("comment1", comments.getText());

        comments.setText("comment1 test update");

        boolean updated = commentsDao.updateComment(comments);
        Assert.assertTrue(updated);

        comments = commentsDao.getCommentById(1);
        Assert.assertEquals("comment1 test update", comments.getText());
    }

    @Test
    public void teste_deleteCommentById_Should_Return_True_On_Successful_Delete() {
        User expectedUser = new User(1, "user1", "111111111-11",
                "user1@gmail.com", "user1Password",
                LocalDate.of(2005, 8, 10));
        Rate rate = new Rate(10, 0, 0);
        Comments comments = new TopLevelComment(null, "test", expectedUser, rate);

        int productId = 1;

        Integer commentId = commentsDao.insertComment(comments, productId);

        boolean deleted = commentsDao.deleteCommentById(commentId);
        Assert.assertTrue(deleted);

        comments = commentsDao.getCommentById(commentId);
        Assert.assertNull(comments);
    }

    @Test
    public void test_deleteCommentById_Should_Return_False_False_For_Nonexistent_Comment_Id() {
        int nonExistentId = 50;

        boolean deleted = commentsDao.deleteCommentById(nonExistentId);

        Assert.assertFalse(deleted);
    }
}
