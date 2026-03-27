package services;

import base.LoginAPI;
import io.restassured.filter.log.LogDetail;
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

    public static Response updateBooking(int id, Booking booking) {
        return given()
                .spec(LoginAPI.getUpdateReqSpec())
                .body(booking)
                .when()
                .put("/booking/" + id);
    }

    public static Response deleteBooking(int id) {
        return given()
                .spec(LoginAPI.getUpdateReqSpec())
                .when()
                .delete("/booking/" + id);
    }
}
