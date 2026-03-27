package tests;

import base.LoginAPI;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Booking;
import models.BookingDates;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.BookingService;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

public class BookingAPITest extends LoginAPI {

    @BeforeMethod
    public void loginBeforeTest() {
        LoginAPI.generateToken(); // token for current thread
    }

    @Test(description = "Create a booking with valid data", priority = 1)
    public void validateSuccessfulBookingCreation() {

        BookingDates dates = new BookingDates("2026-04-01", "2026-04-05");
        Booking booking = new Booking(
                "John",
                "Doe",
                150,
                true,
                dates,
                "Breakfast"
        );
        Response createResponse = BookingService.createBooking(booking);
        // validations
        Assert.assertEquals(createResponse.statusCode(), 200);
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        Assert.assertNotNull(jsonPath.getInt("bookingid"));
        // Validate all details later using testdata file

    }

    @Test(description = "Validate 500 is thrown when firstname is missed", priority = 2)
    public void validateMissingPayloadDetails() {

        BookingDates dates = new BookingDates();
        dates.setCheckin("2026-04-01");
        dates.setCheckout("2026-04-05");

        Booking booking = new Booking();
        booking.setLastname("Doe");
        booking.setTotalprice(150);
        booking.setDepositpaid(true);
        booking.setBookingdates(dates);
        booking.setAdditionalneeds("Breakfast");


        given()
                .spec(LoginAPI.getLoginReqSpec())
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .spec(responseSpec)
                .statusCode(500);

    }

    @Test(description = "Validate Get Booking details with valid booking Id created", priority = 3)
    public void validateGetBookingIdDetails() {
        BookingDates dates = new BookingDates("2026-04-01", "2026-04-05");
        Booking booking = new Booking(
                "James",
                "Bond",
                250,
                true,
                dates,
                "Breakfast"
        );

        Response createResponse = BookingService.createBooking(booking);
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        // Step 2: Get booking
        Response getResponse = BookingService.getBooking(bookingId);

        // Step 3: Validations
        Assert.assertEquals(getResponse.statusCode(), 200);
        Assert.assertNotNull(getResponse.jsonPath().getString("firstname"));
        // Validate all details later using testdata file

    }

    @AfterMethod
    public void cleanup() {
        TokenManager.clearToken();
    }
}
