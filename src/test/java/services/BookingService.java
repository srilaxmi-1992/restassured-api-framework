package services;

import base.LoginAPI;
import groovy.util.logging.Log;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingService extends LoginAPI {

    public static Response createBooking(String payload) {
        return given()
                .spec(LoginAPI.getLoginReqSpec())
                .body(payload)
                .when()
                .post("/booking");
    }

    public static Response getBooking(int id) {
        return given()
                .spec(LoginAPI.getLoginReqSpec())
                .when()
                .get("/booking/" + id + "");
    }
}
