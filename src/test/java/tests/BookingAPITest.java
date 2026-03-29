package tests;

import base.LoginAPI;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.Booking;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import services.BookingService;
import utils.TestDataLoader;
import utils.TokenManager;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookingAPITest extends LoginAPI {

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginAPI.generateToken(); // token for current thread
    }

    @DataProvider(name = "getBookings")
    public Object[][] getTestData() throws Exception {

        List<Booking> bookings = TestDataLoader.getDataDrivenInput("TC_001");
        List<JsonNode> expected = TestDataLoader.getDataDrivenExpected("TC_001");

        // columns fixed, passing 2 params Booking and Expected jsonNode
        // Rows based on How many times - how many bookings passing from testdata json
        Object[][] data = new Object[bookings.size()][2];
        for (int i = 0; i < bookings.size(); i++) {
            data[i][0] = bookings.get(i);
            data[i][1] = expected.get(i);
        }
        return data;
    }

    @Test(description = "Create a booking with valid data", priority = 1, groups = {"smoke"}, dataProvider = "getBookings")
    public void validateSuccessfulBookingCreation(Booking booking, JsonNode expected) throws Exception {

        Response createResponse = BookingService.createBooking(booking);
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        // validations
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(createResponse.statusCode(), expected.get("statusCode").asInt());
        softAssert.assertNotNull(jsonPath.getInt("bookingid"));
        softAssert.assertEquals(jsonPath.getString("booking.firstname"),
                expected.get("firstname").asText());
        softAssert.assertAll();

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
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(getResponse.statusCode(), expected.get("statusCode").asInt());
        softAssert.assertNotNull(getResponse.jsonPath().getString("firstname"));
        softAssert.assertEquals(getResponse.jsonPath().getString("firstname"), expected.get("firstname").asText());
        softAssert.assertEquals(getResponse.jsonPath().getString("lastname"), expected.get("lastname").asText());
        softAssert.assertAll();

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
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(updateResponse.statusCode(), expected.get("statusCode").asInt());
        softAssert.assertEquals(updateResponse.jsonPath().getString("firstname"), expected.get("firstname").asText());
        softAssert.assertAll();

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
