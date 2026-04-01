package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GraphQLUtils {

    public static Response executeQuery(String baseUri, String body) {
        return RestAssured.given()
                .log().all()
                .baseUri(baseUri)
                .contentType("application/json")
                .body(body)
                .post();
    }
}
