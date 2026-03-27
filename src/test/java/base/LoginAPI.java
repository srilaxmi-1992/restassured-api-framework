package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Login;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

public class LoginAPI extends BaseAPI {

    public static RequestSpecification loginReqSpec;

    public static String generateToken() {

        Login login = new Login("admin", "password123");
        Response response = given()
                .spec(authReqSpec)
                .body(login)
                .when()
                .post("/auth")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = new JsonPath(response.asString());
        String token = jsonPath.getString("token");
        System.out.println("generated token is -->"+token);
        TokenManager.setToken(token);
        return token;
    }

    // Build RequestSpecification with dynamic token
    public static RequestSpecification getLoginReqSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(authReqSpec)
                .addHeader("Authorization", TokenManager.getToken())
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getUpdateReqSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(authReqSpec)
                .addHeader("Cookie", "token=" + TokenManager.getToken())
                .setContentType(ContentType.JSON)
                .build();
    }

}
