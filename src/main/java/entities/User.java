package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import util.UserUtil;

import java.time.LocalDate;
import java.util.*;
import static util.PasswordUtil.passwordHash;
import static util.UserUtil.checkEmail;

public class User {

    private Integer userId;
    private String fullName;
    private String cpf;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Product> listedProducts = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Product> upvotedProducts = new ArrayList<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Order> orders = new ArrayList<>();

    public User() {

    }

    public User(Integer userId, String fullName, String cpf,String email, String password,
                LocalDate birthDate) {
        this.userId = userId;
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name must not be blank.");
        }

        if (fullName.length() > 120) {
            throw new IllegalArgumentException("Full name must have less than 120 characters.");
        }
        this.fullName = fullName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF must not be blank.");
        }

        UserUtil.checkCpf(cpf);
        if (cpf.length() == 11) {
            cpf = cpf.substring(0, 9) + "-" + cpf.substring(9);
        }

        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank.");
        }

        if (email.length() > 160) {
            throw new IllegalArgumentException("Email must have less than 120 characters.");
        }
        checkEmail(email);
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank.");
        }

        password = passwordHash(password);
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date must not be empty.");
        }

        this.birthDate = birthDate;
    }

    public List<Product> getListedProducts() {
        return listedProducts;
    }

    public void addListedProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }

        listedProducts.add(product);
    }

    public void setListedProducts(List<Product> listedProducts) {
        this.listedProducts = listedProducts;
    }

    public List<Product> getUpvotedProducts() {
        return listedProducts;
    }

    public void addUpvotedProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null.");
        }

        listedProducts.add(product);
    }

    public void setUpvotedProducts(List<Product> upvotedProducts) {
        this.upvotedProducts = upvotedProducts;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null.");
        }

        orders.add(order);
    }

    public void setOrder(List<Order> orders) {
        this.orders = orders;
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
        User user = (User) o;
        return Objects.equals(getUserId(), user.getUserId()) &&
                Objects.equals(getFullName(), user.getFullName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getBirthDate(), user.getBirthDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFullName(), getEmail(), getPassword(), getBirthDate());
    }
}
