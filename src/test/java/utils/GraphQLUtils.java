package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GraphQLUtils {

    @Step("Execute GraphQL Query {baseUri}")
    public static Response executeQuery(String baseUri, String body) {
        return RestAssured.given()
                .log().all()
                .baseUri(baseUri)
                .contentType("application/json")
                .body(body)
                .post();
    }
}
