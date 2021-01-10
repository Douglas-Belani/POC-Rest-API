package entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;

public class UserTest {

   private User user;

   @Before
   public void initialize() {
      user = new User(1, "fullName", "111111111-11","email@gmail.com",
              "password", LocalDate.now());
   }

   @Test
   public void test_setFullName_Should_Throw_IllegalArgumentException_When_Null_Full_Name_Is_Passed() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setFullName(null));
      Assert.assertEquals("Full name must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setFullName_Should_Throw_IllegalArgumentException_When_Blank_Full_Name_Is_Passed() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setFullName(""));
      Assert.assertEquals("Full name must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setFullName_Should_Throw_IllegalArgumentException_When_Full_Name_Larger_Than_120_Characters_Is_Passed() {
      StringBuilder sb = new StringBuilder();
      while (sb.length() <= 120) {
         sb.append("a");
      }

      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setFullName(sb.toString()));
      Assert.assertEquals("Full name must have less than 120 characters.", thrown.getMessage());
   }

   @Test
   public void test_setFullName_Should_Successfully_Change_Full_Name_For_Non_Empty_Non_Null_FullName_With_Less_Than_120_Characters() {
      String newFullName = "newFullName";
      user.setFullName(newFullName);
      Assert.assertEquals(newFullName, user.getFullName());
   }

   @Test
   public void test_setCpf_Should_Throw_IllegalArgumentException_For_Null_Cpf() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setCpf(null));
      Assert.assertEquals("CPF must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setCpf_Should_Throw_IllegalArgumentException_For_Blank_Cpf() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setCpf(null));
      Assert.assertEquals("CPF must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setCpf_Should_Throw_IllegalArgumentException_If_CPF_Length_Is_Not_11() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setCpf("11"));
      Assert.assertEquals("CPF must have 9 digits followed by - followed by 2 digits.",
              thrown.getMessage());
   }

   @Test
   public void test_setCpf_Should_Throw_IllegalArgumentException_If_CPF_Contains_Letter() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setCpf("11aaaa231"));
      Assert.assertEquals("CPF must have 9 digits followed by - followed by 2 digits.",
              thrown.getMessage());
   }

   @Test
   public void test_setCpf_Should_Successfully_Change_Cpf_If_Cpf_Contains_10_Digits() {
      String cpf = "11111111111";
      user.setCpf(cpf);
      Assert.assertEquals("111111111-11", user.getCpf());
   }

   @Test
   public void test_setCpf_Should_Successfully_Change_Cpf_If_Cpf_Contains_9_Digits_Followed_By_Dash_Folllowed_By_2D_igits() {
      String cpf = "111111111-11";
      user.setCpf(cpf);
      Assert.assertEquals(cpf, user.getCpf());
   }

   @Test
   public void test_setEmail_Should_Throw_IllegalArgumentException_For_Null_Email() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setEmail(null));
      Assert.assertEquals("Email must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setEmail_Should_Throw_IllegalArgumentException_For_Blank_Email() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setEmail(""));
      Assert.assertEquals("Email must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setEmail_Should_Throw_IllegalArgumentException_For_Email_Larger_Than_160_Characters() {
      StringBuilder sb = new StringBuilder();
      while (sb.length() <= 160) {
         sb.append("a");
      }
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setEmail(sb.toString()));
      Assert.assertEquals("Email must have less than 120 characters.", thrown.getMessage());
   }

   @Test
   public void test_setEmail_Should_Successfully_Change_Email_For_Non_Null_Non_Blank_Email_With_Less_Than_160_Characters() {
      String newEmail = "newEmail@gmail.com";
      user.setEmail(newEmail);
      Assert.assertEquals(newEmail, user.getEmail());
   }

   @Test
   public void test_setPassword_Should_Throw_IllegalArgumentException_For_Null_Password() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setPassword(null));
      Assert.assertEquals("Password must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setPassword_Should_Throw_IllegalArgumentException_For_Blank_Password() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setPassword(""));
      Assert.assertEquals("Password must not be blank.", thrown.getMessage());
   }

   @Test
   public void test_setPassword_Should_Throw_IllegalArgumentException_For_Password_Without_Capital_Letter_Digit_Special_Character() {
      String invalidPassword = "invalidPassword";
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setPassword(invalidPassword));
      Assert.assertEquals("Wrong password format. Password must contain at least one upper case letter, " +
              "one lower case letter, one number, !@#$%^&*()-+ and have 6-20 characters", thrown.getMessage());
   }

   @Test
   public void test_setBirthDate_Should_Throw_IllegalArgumentException_For_Null_BirthDate() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.setBirthDate(null));
      Assert.assertEquals("Birth date must not be empty.", thrown.getMessage());
   }

   @Test
   public void test_setBirthDate_Should_Successfully_Change_BirthDate_For_Non_Null_Birhtdate() {
      LocalDate newBirthDate = LocalDate.now().plusDays(1);
      user.setBirthDate(newBirthDate);
      Assert.assertEquals(newBirthDate, user.getBirthDate());
   }

   @Test
   public void test_addProductToList_Should_Throw_IllegalArgumentException_For_Non_Product() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.addListedProduct(null));
      Assert.assertEquals("Product must not be null.", thrown.getMessage());
   }

   @Test
   public void test_addProductToList_Should_Successfully_Add_Product_To_List_For_Non_Null_Product() {
      Product product = new Product(1, "product", 100.00,
              "description", 5, user, new Rate(1, 1, 1));
      Assert.assertEquals(0, user.getListedProducts().size());
      user.addListedProduct(product);
      Assert.assertEquals(1, user.getListedProducts().size());
      Assert.assertEquals(product, user.getListedProducts().get(0));
   }

   @Test
   public void test_addUpvotedProduct_Should_Throw_IllegalArgumentException_For_Null_Product() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.addUpvotedProduct(null));
      Assert.assertEquals("Product must not be null.", thrown.getMessage());
   }

   @Test
   public void test_addUpvotedProduct_Should_Successfully_Add_Product_To_Upvoted_Product_List_For_Non_Null_Product() {
      Product product = new Product(2, "product2", 300.00,
              "description", 5, user, new Rate(5, 1, 1));
      Assert.assertEquals(0, user.getUpvotedProducts().size());
      user.addListedProduct(product);
      Assert.assertEquals(1, user.getUpvotedProducts().size());
      Assert.assertEquals(product, user.getUpvotedProducts().get(0));
   }

   @Test
   public void test_addOrder_Should_Throw_IllegalArgumentException_For_Null_Order() {
      IllegalArgumentException thrown = Assert.assertThrows(IllegalArgumentException.class, () ->
              user.addOrder(null));
      Assert.assertEquals("Order must not be null.", thrown.getMessage());
   }

   @Test
   public void test_addOrder_Should_Successfully_Add_Order_To_Orders_For_Non_Null_Order() {
      Order order = new Order(1, LocalDate.now(), OrderStatus.Paid, null, user);
      Assert.assertEquals(0, user.getOrders().size());
      user.addOrder(order);
      Assert.assertEquals(1, user.getOrders().size());
      Assert.assertEquals(order, user.getOrders().get(0));
   }

}
