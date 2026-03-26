package tests;

import base.LoginAPI;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import services.BookingService;
import utils.TokenManager;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class BookingAPITest extends LoginAPI {

    @BeforeMethod
    public void loginBeforeTest() {
        LoginAPI.generateToken(); // token for current thread
    }

    @Test(description = "Create a booking with valid data", priority = 1)
    public void validateSuccessfulBookingCreation(){

        String jsonBody = "{\n" +
                "  \"firstname\": \"John\",\n" +
                "  \"lastname\": \"Doe\",\n" +
                "  \"totalprice\": 150,\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": {\n" +
                "    \"checkin\": \"2026-04-01\",\n" +
                "    \"checkout\": \"2026-04-05\"\n" +
                "  },\n" +
                "  \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        Response createResponse = BookingService.createBooking(jsonBody);
        // validations
        Assert.assertEquals(createResponse.statusCode(), 200);
        JsonPath jsonPath = new JsonPath(createResponse.asString());
        Assert.assertNotNull(jsonPath.getInt("bookingid"));
        // Validate all details later using testdata file

    }

    @Test(description = "Validate 500 is thrown when firstname is missed", priority = 2)
    public void validateMissingPayloadDetails(){

        String jsonBody = "{\n" +
                "  \"lastname\": \"Doe\",\n" +
                "  \"totalprice\": 150,\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": {\n" +
                "    \"checkin\": \"2026-04-01\",\n" +
                "    \"checkout\": \"2026-04-05\"\n" +
                "  },\n" +
                "  \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        given()
                .spec(LoginAPI.getLoginReqSpec())
                .body(jsonBody)
                .when()
                .post("/booking")
                .then()
                .spec(responseSpec)
                .statusCode(500);

    }

    @Test(description = "Validate Get Booking details with valid booking Id created", priority = 3)
    public void validateGetBookingIdDetails(){
        String jsonBody = "{\n" +
                "  \"firstname\": \"John\",\n" +
                "  \"lastname\": \"Doe\",\n" +
                "  \"totalprice\": 150,\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": {\n" +
                "    \"checkin\": \"2026-04-01\",\n" +
                "    \"checkout\": \"2026-04-05\"\n" +
                "  },\n" +
                "  \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        Response createResponse = BookingService.createBooking(jsonBody);
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
