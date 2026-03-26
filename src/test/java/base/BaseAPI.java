package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;
import utils.ConfigReader;

import java.io.IOException;

public class BaseAPI {

    public static RequestSpecification authReqSpec;
    public static ResponseSpecification responseSpec;

    @BeforeSuite
    public void setup() throws IOException {
        String baseUrl = ConfigReader.getConfigProps().getProperty("base.url");
        authReqSpec = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
}
