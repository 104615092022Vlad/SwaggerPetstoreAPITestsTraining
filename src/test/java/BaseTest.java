import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;

public class BaseTest {
    RequestSpecification request;
    static Random r;

    @BeforeAll
    public static void setBaseURI() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        r = new Random();
    }

    @BeforeEach
    public void beginRequest() {
        request = RestAssured.given();
    }
}

