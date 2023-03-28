import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


public class StoreTest extends BaseTest {
    final long maxLong = 9223372036854775807L;
    final int maxInt = 2147483647;
    final long maxDate = 32503669199000L;

    public JSONObject generateJSON(long a, long b, long c, long d, int e, int f, int t) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
        Date expectedDate = new Date();
        switch (t) {
            case 0: expectedDate.setTime(r.nextLong(0, expectedDate.getTime()));
                break;
            case 1: expectedDate.setTime(expectedDate.getTime());
                break;
            case 2: expectedDate.setTime(r.nextLong(expectedDate.getTime(), maxDate));
                break;
        }

        long id = r.nextLong(a, b);
        long petId = r.nextLong(c, d);
        int quantity = r.nextInt(e, f);
        String date = df.format(expectedDate);
        String status = OrderDto.statusList().get(r.nextInt(2));
        boolean complete = r.nextBoolean();

        JSONObject order = new JSONObject();
        order.put("id", id);
        order.put("petId", petId);
        order.put("quantity", quantity);
        order.put("shipDate", date);
        order.put("status", status);
        order.put("complete", complete);

        return order;
    }

    @Test
    public void shouldCreateOrderCurrentTime() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,1,maxInt,1);

        request.body(order.toString());
        request.header("content-type", "application/json");

        Response response = request.post("/store/order");

        response.then()
                .statusCode(200)
                .assertThat()
                .body("id", equalTo(order.getLong("id")),
                        "petId", equalTo(order.getLong("petId")),
                        "quantity", equalTo(order.getInt("quantity")),
                        "shipDate", equalTo(order.getString("shipDate")),
                        "status", equalTo(order.getString("status")),
                        "complete", equalTo(order.getBoolean("complete"))
                );
    }

    @Test
    public void shouldNotCreateOrderPastTime() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,1,maxInt,0);

        request.body(order.toString())
                .header("content-type", "application/json")
                .log().body();

        Response response = request.post("/store/order");

        response.then()
                .log().body()
                .statusCode(500);
    }

    @Test
    public void shouldNotCreateOrderFutureTime() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,1,maxInt,2);

        request.body(order.toString())
                .header("content-type", "application/json")
                .log().body();

        Response response = request.post("/store/order");

        response.then()
                .statusCode(500)
                .log().body();
    }

    @Test
    public void shouldNotCreateOrderWithIncorrectData() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,0,maxInt,1);
        order.put("quantity", "twenty");

        request.body(order.toString())
                .header("content-type", "application/json");

        Response response = request.post("/store/order");

        response.then()
                .statusCode(500);
    }

    @Test
    public void shouldGetOrderById() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,0,maxInt,1);

        request.body(order.toString())
                .header("content-type", "application/json")
                .log().body()
                .post("/store/order");

        request = RestAssured.given();

        Response response = request.get("/store/order/{id}", order.getLong("id"));
        response.then()
                .statusCode(200)
                .log().body()
                .body("id", equalTo(order.getLong("id")));
    }

    @Test
    public void shouldNotGetOrderById() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,0,maxInt,1);

        request.body(order.toString())
                .header("content-type", "application/json")
                .post("/store/order");

        Response response = request.get("/store/order/{id}", order.getLong("id"));
        response.then()
                .statusCode(200)
                .body("id", not(11));
    }

    @Test
    public void shouldDeleteOrderById() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,1,maxInt,1);

        request.body(order.toString())
                .header("content-type", "application/json")
                .log().body()
                .post("/store/order");

        request = RestAssured.given();

        Response response = request.delete("/store/order/{orderId}", order.getLong("id"));
        response.then()
                .statusCode(200);
    }

    @Test
    public void shouldNotDeleteOrderById() {
        JSONObject order = generateJSON(1,maxLong,0,maxLong,1,maxInt,1);

        request.body(order.toString())
                .header("content-type", "application/json")
                .log().body()
                .post("/store/order");

        request = RestAssured.given();

        Response response = request.delete("/store/order/{orderId}", 0);
        response.then()
                .statusCode(404);
    }
}
