package services.impl;

import dao.ICommentDao;
import entities.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import resources.exception.ResourceNotFoundException;
import services.ICommentService;
import services.IRateService;
import services.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {

    @Mock
    private Connection conn;

    @Mock
    private ICommentDao commentDao;

    @Mock
    private IRateService rateService;

    private ICommentService commentService;

    @Before
    public void initialize() {
        commentService = new CommentService(conn, commentDao, rateService);
    }

    @Test
    public void test_comment_Should_Return_Null_If_Rate_RateId_Is_Null() throws SQLException {
        int productId = 2;
        Integer topLevelCommentId = null;
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(null, 0, 0);

        Mockito.when(rateService.createRate()).thenReturn(rate1);

        Assert.assertNull(commentService.comment("text", user1, productId, topLevelCommentId));

    }

    @Test
    public void test_comment_Should_Return_Null_If_CommentDao_InsertComment_Returns_Null() throws SQLException {
        int productId = 2;
        Integer topLevelCommentId = null;
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(1, 0, 0);
        TopLevelComment comment = new TopLevelComment(null, "text", user1, rate1);

        Mockito.when(rateService.createRate()).thenReturn(rate1);
        Mockito.when(commentDao.insertComment(comment, productId)).thenReturn(null);

        Assert.assertNull(commentService.comment("text", user1, productId, null));
        Mockito.verify(conn, Mockito.times(1)).rollback();
    }

    @Test
    public void test_comment_Should_Return_Comment_Id_On_Successful_Insert() throws SQLException {
        int productId = 2;
        Integer topLevelCommentId = null;
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        Rate rate1 = new Rate(1, 0, 0);
        TopLevelComment comment = new TopLevelComment(null, "text", user1, rate1);

        Mockito.when(rateService.createRate()).thenReturn(rate1);
        Mockito.when(commentDao.insertComment(comment, productId)).thenReturn(1);

        Assert.assertEquals(Integer.valueOf(1),
                commentService.comment("text", user1, productId, null));
        Mockito.verify(conn, Mockito.times(1)).commit();

    }

    @Test
    public void test_editComment_Should_Throw_UnauthorizedException_For_Different_User_Comment() {
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
                "P@ssword1", LocalDate.now());
        User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
                "P@ssword2", LocalDate.now());

        Rate rate1 = new Rate(1, 2, 1);
        Comments comment = new TopLevelComment(1, "text", user1, rate1);

        Mockito.when(commentDao.getCommentById(1)).thenReturn(comment);
        UnauthorizedException thrown = Assert.assertThrows(UnauthorizedException.class ,
                () -> commentService.editComment(user2, "textUpdate", 1));
        Assert.assertEquals("Can't modify another user's comment." ,thrown.getMessage());
    }

   @Test
    public void test_editComment_Should_Throw_ResourceNotFoundException_For_Nonexistent_Comment_Id() {
       User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
               "P@ssword1", LocalDate.now());

       Mockito.when(commentDao.getCommentById(1)).thenReturn(null);

       ResourceNotFoundException thronw = Assert.assertThrows(ResourceNotFoundException.class, () ->
               commentService.editComment(user1, "text", 1));
       Assert.assertEquals("Comment id 1 not found", thronw.getMessage());
   }

   @Test
    public void test_editComment_Should_Return_True_If_Comment_Id_Exists_And_Have_Same_User() {
       User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
               "P@ssword1", LocalDate.now());
       Rate rate1 = new Rate(1, 2, 1);
       Comments comment = new TopLevelComment(1, "text", user1, rate1);

       Mockito.when(commentDao.getCommentById(1)).thenReturn(comment);
       Mockito.when(commentDao.updateComment(comment)).thenReturn(true);

       Assert.assertTrue(commentService.editComment(user1, "updatedText", 1));
       Assert.assertEquals("updatedText", comment.getText());
   }

   @Test
    public void test_deleteComment_Should_Throw_ResourceNotFoundException_For_NonExistent_Comment_Id() throws SQLException {
        int nonexistentCommentId = 90;
        User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
               "P@ssword1", LocalDate.now());

        Mockito.when(commentDao.getCommentById(nonexistentCommentId)).thenReturn(null);

       ResourceNotFoundException thrown = Assert.assertThrows(ResourceNotFoundException.class, () ->
               commentService.deleteComment(user1, nonexistentCommentId));
       Assert.assertEquals("Comment id " + nonexistentCommentId + " not found", thrown.getMessage());
   }

   @Test
    public void test_deleteComment_Should_Throw_UnauthorizedException_For_Different_Comment_User() throws SQLException {
       User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
               "P@ssword1", LocalDate.now());
       User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
               "P@ssword2", LocalDate.now());
       Rate rate1 = new Rate(1, 2, 1);
       Comments comment = new TopLevelComment(1, "text", user2, rate1);

       Mockito.when(commentDao.getCommentById(1)).thenReturn(comment);

       UnauthorizedException thrown = Assert.assertThrows(UnauthorizedException.class,
               () -> commentService.deleteComment(user1, 1));
       Assert.assertEquals("Can't delete another user's comment.", thrown.getMessage());
   }

   @Test
    public void test_deleteComment_Should_Return_False_If_Comment_Is_Instance_Of_TopLevelComment_And_DeleteRateById_Returns_False() throws SQLException {
       User user1 = new User(1, "fullName", "111111111-11", "email1@gmail.com",
               "P@ssword1", LocalDate.now());
       User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
               "P@ssword2", LocalDate.now());
       Rate rate1 = new Rate(1, 2, 0);
       Rate rate2 = new Rate(2, 1, 0);

       Comments topLevelComment = new TopLevelComment(1, "text1", user1, rate1);
       Comments subLevelComment = new SubLevelComment(2, "test2", user2, rate2, 1);
       ((TopLevelComment) topLevelComment).addSubLevelComment((SubLevelComment) subLevelComment);

       Mockito.when(commentDao.getCommentById(1)).thenReturn(topLevelComment);

       Assert.assertFalse(commentService.deleteComment(user1, 1));
       Mockito.verify(conn, Mockito.times(1)).rollback();
   }

   @Test
    public void test_deleteComment_Should_Return_False_If_RateService_DeleteById_Returns_False() throws SQLException {
       User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
               "P@ssword2", LocalDate.now());
       Rate rate2 = new Rate(2, 1, 0);
       Comments subLevelComment = new SubLevelComment(2, "test2", user2, rate2, 1);

       Mockito.when(commentDao.getCommentById(2)).thenReturn(subLevelComment);


       Assert.assertFalse(commentService.deleteComment(user2, 2));
       Mockito.verify(conn, Mockito.times(1)).rollback();
   }

   @Test
    public void test_deleteComment_Should_Return_False_If_CommentDao_DeleteCommentById_Returns_False() throws SQLException {
       User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
               "P@ssword2", LocalDate.now());
       Rate rate2 = new Rate(2, 1, 0);
       Comments subLevelComment = new SubLevelComment(2, "test2", user2, rate2, 1);

       Mockito.when(commentDao.getCommentById(2)).thenReturn(subLevelComment);
       Mockito.when(commentDao.deleteCommentById(2)).thenReturn(false);

       Assert.assertFalse(commentService.deleteComment(user2, 2));
       Mockito.verify(conn, Mockito.times(1)).rollback();
   }

   @Test
    public void test_deleteComment_Should_Return_True_For_Successful_Delete() throws SQLException {
       User user2 = new User(2, "fullName", "222222222-22", "email2@gmail.com",
               "P@ssword2", LocalDate.now());
       Rate rate2 = new Rate(2, 1, 0);
       Comments subLevelComment = new SubLevelComment(2, "test2", user2, rate2, 1);

       Mockito.when(commentDao.getCommentById(2)).thenReturn(subLevelComment);
       Mockito.when(rateService.deleteById(2)).thenReturn(true);
       Mockito.when(commentDao.deleteCommentById(2)).thenReturn(true);

       Assert.assertTrue(commentService.deleteComment(user2, 2));
       Mockito.verify(conn, Mockito.times(1)).commit();
       Mockito.verify(conn, Mockito.times(0)).rollback();
   }


}
