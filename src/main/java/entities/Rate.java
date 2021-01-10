package entities;

import java.util.Objects;

public class Rate {

    private Integer rateId;
    private int upvotes;
    private int downvotes;

    public Rate() {

    }

    public Rate(Integer rateId, Integer upvotes, Integer downvotes) {
        this.rateId = rateId;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        if (upvotes < 0) {
            throw new IllegalArgumentException("Upvotes must be greater than or equal to 0.");
        }
        this.upvotes = upvotes;
    }

    public void addUpvote() {
        upvotes++;
    }

    public void decreaseUpvote() {
        upvotes--;
        if (upvotes == -1) {
            upvotes = 0;
        }
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        if (downvotes < 0) {
            throw new IllegalArgumentException("Donwvotes must be greater than or equal to 0.");
        }

        this.downvotes = downvotes;
    }

    public void addDownvote() {
        downvotes++;
    }

    public void decreaseDownvote() {
        downvotes--;
        if (downvotes == -1) {
            downvotes = 0;
        }
    }

    public int getTotalVotes() {
        return upvotes + downvotes;
    }

    public double getAvarage() {
        if (getTotalVotes() == 0) {
            return 0;

        } else {
            return (double) (upvotes * 5) / getTotalVotes();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(getRateId(), rate.getRateId()) &&
                Objects.equals(getUpvotes(), rate.getUpvotes()) &&
                Objects.equals(getDownvotes(), rate.getDownvotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRateId(), getUpvotes(), getDownvotes());
    }
}
