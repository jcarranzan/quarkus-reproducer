package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

    @Test
    void testKafkaVersionInMetrics() {
        String response = given()
                .when().get("/q/metrics")
                .then()
                .statusCode(200)
                .extract().asString();
        System.out.println("RESPONEEEEEE  " + response);
        // Verificar que kafka_version no es "unknown"
        boolean isKafkaVersionKnown = response.contains("kafka_version=\"unknown\"");
        assertFalse(isKafkaVersionKnown, "Error: kafka_version is 'unknown' in the metrics response");
    }

}