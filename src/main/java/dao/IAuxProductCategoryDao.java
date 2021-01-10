package dao;

public interface IAuxProductCategoryDao {

    public abstract boolean insert(int productId, int categoryId);

    public abstract boolean update(int productId, int oldCategoryId, int newCategoryId);

    public abstract boolean deleteByProductIdAndCategoryId(int productId, int categoryId);

    public abstract boolean deleteByProductId(int productId);

}
