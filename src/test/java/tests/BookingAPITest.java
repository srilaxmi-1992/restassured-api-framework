package tests;

import base.LoginAPI;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Booking;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.BookingService;
import utils.TestDataLoader;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

public class BookingAPITest extends LoginAPI {

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginAPI.generateToken(); // token for current thread
    }

    @Test(description = "Create a booking with valid data", priority = 1, groups = {"smoke"})
    public void validateSuccessfulBookingCreation() throws Exception {

        Booking booking = TestDataLoader.getInput("TC_001");
        Response createResponse = BookingService.createBooking(booking);
        // validations
        JsonNode expected = TestDataLoader.getExpected("TC_001");

        Assert.assertEquals(createResponse.statusCode(), expected.get("statusCode").asInt());
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        Assert.assertNotNull(jsonPath.getInt("bookingid"));
        // Validate all details later using testdata file
        Assert.assertEquals(jsonPath.getString("booking.firstname"),
                expected.get("firstname").asText());

    }

    @Test(description = "Validate 500 is thrown when firstname is missed", priority = 2, groups = {"regression"})
    public void validateMissingPayloadDetails() throws Exception {
        Booking booking = TestDataLoader.getInput("TC_002");

        Response response = given()
                .spec(LoginAPI.getLoginReqSpec())
                .body(booking)
                .when()
                .post("/booking");

        JsonNode expected = TestDataLoader.getExpected("TC_002");
        Assert.assertEquals(response.getStatusCode(), expected.get("statusCode").asInt());

    }

    @Test(description = "Validate Get Booking details with valid booking Id created", priority = 3, groups = {"regression", "smoke"})
    public void validateGetBookingIdDetails() throws Exception {
        Booking booking = TestDataLoader.getInput("TC_003");
        Response createResponse = BookingService.createBooking(booking);
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        // Step 2: Get booking
        Response getResponse = BookingService.getBooking(bookingId);

        // Step 3: Validations
        JsonNode expected = TestDataLoader.getExpected("TC_003");
        Assert.assertEquals(getResponse.statusCode(), expected.get("statusCode").asInt());
        Assert.assertNotNull(getResponse.jsonPath().getString("firstname"));
        Assert.assertEquals(getResponse.jsonPath().getString("firstname"), expected.get("firstname").asText());
        Assert.assertEquals(getResponse.jsonPath().getString("lastname"), expected.get("lastname").asText());

    }

    @Test(description = "Valid update with all fields", priority = 4, groups = {"regression"})
    public void validateUpdateBookingIdDetails() throws Exception {
        Booking booking = TestDataLoader.getInput("TC_004");
        Response createResponse = BookingService.createBooking(booking);
        System.out.println("Create Booking API Response -> " + createResponse.asString());
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        Booking updateBooking = TestDataLoader.getUpdatedInput("TC_004");
        // Step 2: Update booking
        Response updateResponse = BookingService.updateBooking(bookingId, updateBooking);
        System.out.println("Update Booking API Response -> " + updateResponse.asString());


        // Step 3: Validations
        JsonNode expected = TestDataLoader.getExpected("TC_004");
        Assert.assertEquals(updateResponse.statusCode(), expected.get("statusCode").asInt());
        Assert.assertEquals(updateResponse.jsonPath().getString("firstname"), expected.get("firstname").asText());

    }

    @Test(description = "Valid Delete booking", priority = 5, groups = {"regression"})
    public void validateDeleteBookingIdDetails() throws Exception {

        Booking booking = TestDataLoader.getInput("TC_005");
        Response createResponse = BookingService.createBooking(booking);
        System.out.println("Create Booking API Response -> " + createResponse.body().asString());
        int bookingId = createResponse.jsonPath().getInt("bookingid");

        // Step 2: delete booking
        Response deleteResponse = BookingService.deleteBooking(bookingId);
        System.out.println("Delete Booking API Response -> " + deleteResponse.asString());

        // Step 3: Validations
        JsonNode expected = TestDataLoader.getExpected("TC_005");
        Assert.assertEquals(deleteResponse.statusCode(), expected.get("statusCode").asInt());


    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        TokenManager.clearToken();
    }
}
