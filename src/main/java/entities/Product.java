package entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {

    private Integer productId;
    private String name;
    private double price;
    private String description;
    private int stock;
    private User user;
    private Rate rate;

    private List<Category> categories = new ArrayList<>();
    private List<Comments> comments = new ArrayList<>();

    public Product() {

    }

    public Product(Integer productId, String name, Double price, String description, Integer stock,
                   User user, Rate rate) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.user = user;
        this.rate = rate;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }

        if (name.length() > 120) {
            throw new IllegalArgumentException("Name must have less than 120 characters.");
        }

        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        if (price <= 0.0) {
            throw new IllegalArgumentException("Price must be greater than 0.0.");
        }

        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be blank.");
        }

        if (description.length() > 140) {
            throw new IllegalArgumentException("Description must have less than 140 characters.");
        }

        this.description = description;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        this.stock = stock;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null.");
        }

        this.user = user;
    }

    public Rate getRate() {
        return this.rate;
    }

    public void setRate(Rate rate) {
        if (rate == null) {
            throw new IllegalArgumentException("Rate must not be null.");
        }

        this.rate = rate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        if (categories == null) {
            throw new IllegalArgumentException("Categories must not be null.");
        }
        this.categories = categories;
    }

    public void addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null.");
        }

        categories.add(category);
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void addComment(Comments comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment must not be empty.");
        }

        if (comment instanceof TopLevelComment) {
            comments.add(comment);

        } else {
            for (Comments topLevelComment : comments) {
                if (topLevelComment.getCommentId().equals(((SubLevelComment) comment).getTopLevelCommentId())) {
                    ((TopLevelComment) topLevelComment).addSubLevelComment((SubLevelComment) comment);
                }
            }
        }
    }

    public void setComment(List<Comments> comments) {
        this.comments = comments;
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
        Product product = (Product) o;
        return Objects.equals(getProductId(), product.getProductId()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getStock(), product.getStock()) &&
                Objects.equals(getUser(), product.getUser()) &&
                Objects.equals(getRate(), product.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getName(), getPrice(), getDescription(), getStock()
                , getUser(), getRate());
    }
}
