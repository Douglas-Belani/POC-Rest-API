package dao;

public interface IAuxRateProductDao {

    public abstract boolean isAlreadyVoted(int userId, int productId);

    public abstract boolean insertUpvotedProduct(int userId, int productId);

    public abstract boolean insertDownvotedProduct(int userId, int productId);

    public abstract boolean deleteVotedProduct(int userId, int productId);

    public abstract boolean deleteVotedProductById(int productId);

}
