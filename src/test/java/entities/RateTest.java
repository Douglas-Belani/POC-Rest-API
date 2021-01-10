package entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RateTest {

    private Rate rate;

    @Before
    public void initialize() {
        this.rate = new Rate(1, 0, 0);
    }

    @Test
    public void test_setUpvote_Should_Throw_IllegalArgumentException_For_Negative_Upvotes() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                rate.setUpvotes(-1));
        Assert.assertEquals("Upvotes must be greater than or equal to 0.", thrown.getMessage());
    }

    @Test
    public void test_setUpvote_Should_Successfully_Change_Upvotes_For_Positive_Upvotes() {
        int upvotes = 15;
        rate.setUpvotes(upvotes);
        Assert.assertEquals(upvotes, rate.getUpvotes());
    }

    @Test
    public void test_addUpvote_Should_Increase_Upvotes_By_1() {
        int currentUpvotes = rate.getUpvotes();
        rate.addUpvote();
        Assert.assertEquals(currentUpvotes + 1, rate.getUpvotes());
    }

    @Test
    public void test_decreaseDownvote_Should_Decrease_Upvotes_By_1() {
        int currentUpvotes = 5;
        rate.setUpvotes(currentUpvotes);
        rate.decreaseUpvote();
        Assert.assertEquals(currentUpvotes - 1, rate.getUpvotes());
    }

    @Test
    public void test_decreaseDownvote_Should_Return_0_If_Current_Downvotes_Is_Zero() {
        int currentUpvotes = 0;
        rate.setUpvotes(currentUpvotes);
        rate.decreaseUpvote();
        Assert.assertEquals(currentUpvotes, rate.getUpvotes());
    }

    @Test
    public void test_setDownvotes_Should_Throw_IllegalArgumentException_For_Negative_Downvotes() {
        IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
                rate.setDownvotes(-1));
        Assert.assertEquals("Donwvotes must be greater than or equal to 0.", thrown.getMessage());
    }

    @Test
    public void test_setDownvotes_Should_Successfully_Change_Downvotes_For_Positive_Downvotes() {
        int downvotes = 5;
        rate.setDownvotes(downvotes);
        Assert.assertEquals(downvotes, rate.getDownvotes());
    }

    @Test
    public void test_addDownvotes_Should_Increase_Downvotes_By_1() {
        int currentDownvotes = rate.getDownvotes();
        rate.addDownvote();
        Assert.assertEquals(currentDownvotes + 1, rate.getDownvotes());
    }

    @Test
    public void test_decreaseDownvotes_Should_Decrease_Current_Downvotes_By_1() {
        int currentDownvotes = 5;
        rate.setDownvotes(currentDownvotes);
        rate.decreaseDownvote();
        Assert.assertEquals(currentDownvotes - 1, rate.getDownvotes());
    }

    @Test
    public void test_decreaseDownvotes_Should_Return_0_If_Current_Downvotes_Is_0() {
        int currentDownvotes = 0;
        rate.setDownvotes(currentDownvotes);
        rate.decreaseDownvote();
        Assert.assertEquals(currentDownvotes, rate.getDownvotes());
    }

//    @Test
//    public void test_getTotalVotes_Should_Return_12_For_Upvotes_7_And_Downvotes_5() {
//        rate.setUpvotes(7);
//        rate.setDownvotes(5);
//        Assert.assertEquals(12, rate.getUpvotes());
//    }

    @Test
    public void test_getAvarage_Should_Return_3_57_For_Upvotes_25_And_Total_Votes_35() {
        rate.setUpvotes(25);
        rate.setDownvotes(10);
        Assert.assertEquals(3.57, rate.getAvarage(), 0.01);
    }
}
