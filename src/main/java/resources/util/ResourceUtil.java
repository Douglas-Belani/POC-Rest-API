package resources.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.Address;
import entities.Order;
import entities.Product;
import entities.User;
import security.util.JWTUtil;
import services.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class ResourceUtil {

    public static void sendJson(Object object, int statusCode, HttpServletResponse response) throws IOException {
        ObjectWriter writer = new ObjectMapper().writer();
        String json = writer.writeValueAsString(object);
        response.getOutputStream().print(json);
        response.setStatus(statusCode);
    }

    public static String getJsonFromRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        return sb.toString();
    }

    public static Object getObjectFromJson(Class<?> type, String json) throws IOException {
        ObjectReader reader = new ObjectMapper().reader();
            if (type.getName().equals(User.class.getName())) {
                checkUserJsonFields(json);

            } else if (type.getName().equals(Address.class.getName())) {
                checkAddressJsonFields(json);

            } else if (type.getName().equals(Order.class.getName())) {
                checkOrderJsonFields(json);

            } else if (type.getName().equals(Product.class.getName())) {
                checkProductJsonFields(json);
            }
        return reader.readValue(json, type);
    }

    public static String getJsonFromObject(Object object) throws IOException {
        ObjectWriter writer = new ObjectMapper().writer();
        return writer.writeValueAsString(object);
    }

    public static User getUserFromAuthorizationHeader(HttpServletRequest request, IUserService userService) {
        String token = request.getHeader("Authorization").substring(7);
        DecodedJWT decodedJWT = JWTUtil.getDecodedJWT(token);
        String email = decodedJWT.getClaim("email").asString();
        return userService.getUserByEmail(email);
    }

    private static void checkUserJsonFields(String json) throws IOException {
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
        node.required("fullName");
        node.required("cpf");
        node.required("email");
        node.required("password");
        node.required("birthDate");
    }

    private static void checkAddressJsonFields(String json) throws IOException {
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
        node.required("neighborhood");
        node.required("number");
        node.required("zipCode");
        node.required("street");
        node.at("/city/name").require();
        node.at("/city/state/initials").require();
    }

    private static void checkOrderJsonFields(String json) throws IOException {
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
        node.required("date");
        node.required("deliveryAddress");
        node.requiredAt("/deliveryAddress/addressId");
        node.requiredAt("/deliveryAddress/neighborhood");
        node.requiredAt("/deliveryAddress/number");
        node.requiredAt("/deliveryAddress/zipCode");
        node.requiredAt("/deliveryAddress/street");
        node.requiredAt("/deliveryAddress/city/name");
        node.requiredAt("/deliveryAddress/city/state/initials");
        node.required("orderItems");
        ArrayNode arrayNode = (ArrayNode) node.at("/orderItems");
        for (int i = 0; i < arrayNode.size(); i++) {
            node.requiredAt("/orderItems/" + i + "/product/productId");
            node.requiredAt("/orderItems/" + i + "/product/price");
            node.requiredAt("/orderItems/" + i + "/product/stock");
            node.requiredAt("/orderItems/" + i + "/quantity");
        }
    }

    private static void checkProductJsonFields(String json) throws IOException {
        ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
        node.required("name");
        node.required("price");
        node.required("description");
        node.required("stock").require();
        node.required("categories");
        ArrayNode arrayNode = (ArrayNode) node.at("/categories");
        for(int i = 0; i < arrayNode.size(); i++) {
            node.requiredAt("/categories/" + i + "/categoryId");
        }
    }
}