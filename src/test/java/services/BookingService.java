package services;

import base.LoginAPI;
import io.restassured.response.Response;
import models.Booking;

import static io.restassured.RestAssured.given;

public class BookingService extends LoginAPI {

    public static Response createBooking(Booking payload) {
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
                .get("/booking/" + id);
    }
}
