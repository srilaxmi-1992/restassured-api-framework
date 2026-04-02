package services;

import base.LoginAPI;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Booking;

import static io.restassured.RestAssured.given;

public class BookingService extends LoginAPI {

    @Step("Create Booking Service")
    public static Response createBooking(Booking payload) {
        return given()
                .spec(LoginAPI.getLoginReqSpec())
                .body(payload)
                .when()
                .post("/booking");
    }

    @Step("Get Booking Service")
    public static Response getBooking(int id) {
        return given()
                .spec(LoginAPI.getLoginReqSpec())
                .when()
                .get("/booking/" + id);
    }

    @Step("Update Booking Service")
    public static Response updateBooking(int id, Booking booking) {
        return given()
                .spec(LoginAPI.getUpdateReqSpec())
                .body(booking)
                .when()
                .put("/booking/" + id);
    }

    @Step("Delete Booking Service")
    public static Response deleteBooking(int id) {
        return given()
                .spec(LoginAPI.getUpdateReqSpec())
                .when()
                .delete("/booking/" + id);
    }
}
