package tests;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.ConfigReader;
import utils.GraphQLUtils;
import utils.TestDataLoader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Epic("GraphQL API")
@Feature("Countries and Continents")
public class CountriesGraphQLAPITest {

    static String url = "";

    static {
        try {
            url = ConfigReader.getConfigProps().getProperty("countries_url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Logger logger = LogManager.getLogger(CountriesGraphQLAPITest.class);

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest(Method method) {
        // Timestamp safe for Windows
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String testLogName = method.getName();
        ThreadContext.put("testName", testLogName);
        ThreadContext.put("threadId", String.valueOf(Thread.currentThread().getId())+ "-" + timestamp);
    }

    @Story("Continents")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Get All Continents")
    @Test(groups = {"smoke"}, priority = 1)
    public void getAllContinents() throws IOException {
        String query = TestDataLoader.buildGraphQLRequestBody("getContinents");
        Response response = GraphQLUtils.executeQuery(url, query);

        response.then().statusCode(200);
        JsonNode continentsNode = TestDataLoader.readJson("src/test/resources/testdata/responses/getContinents.json");
        List<String> actualContinents = response.jsonPath().getList("data.continents.name");
        logger.info("Continents list --> " + actualContinents);

        List<String> expectedContinents = new ArrayList<>();
        JsonNode continents= continentsNode.path("continents");
        if(continents.isArray()){
            for(JsonNode continent : continents){
                expectedContinents.add(continent.path("name").asText());
            }
        }
        logger.info("expected continents from test data -->" + expectedContinents);
        Assert.assertEquals(actualContinents, expectedContinents);
    }

    @Story("Countries")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Get Country by Code")
    @Test(groups = {"smoke", "regression"}, priority = 2)
    public void getCountryByCode() throws IOException {

        String query = TestDataLoader.buildGraphQLRequestBody("getCountry");

        Response response = GraphQLUtils.executeQuery(url, query);
        response.then().statusCode(200);

        Map<String, Object> expectedCountry = TestDataLoader.readJsonAsMap("src/test/resources/testdata/responses/getCountry.json");
        logger.info("Expected Country -->" + expectedCountry);
        Map<String, Object> actualCountry = response.jsonPath().getMap("data.country");
        logger.info("Actual Country -->" + actualCountry);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualCountry.get("name"), expectedCountry.get("name"));
        softAssert.assertEquals(actualCountry.get("capital"), expectedCountry.get("capital"));
        softAssert.assertTrue(Arrays.stream(actualCountry.get("currency").toString().split(",")).toList().contains(expectedCountry.get("currency")));
        softAssert.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        ThreadContext.clearAll();
    }
}
