package org.acme;

import io.quarkus.test.bootstrap.JaegerService;
import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.JaegerContainer;
import io.quarkus.test.services.QuarkusApplication;
import io.restassured.response.Response;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;

@QuarkusScenario
class OtelResourceTest {
    private static final Logger LOG = Logger.getLogger(OtelResourceTest.class);
    @JaegerContainer(expectedLog = "\"Health Check state change\",\"status\":\"ready\"")
    static final JaegerService jaeger = new JaegerService();

    @QuarkusApplication(classes = { GreetingResource.class })
    static final RestService app = new RestService()
            .withProperty("quarkus.application.name", "otel")
            .withProperty("quarkus.otel.exporter.otlp.traces.endpoint", jaeger::getCollectorUrl);


    @Test
    void testNoTracesOnHelloEndpointInOneSecond() throws InterruptedException {
        int pageLimit = 10;
        String operationName = "GET /hello";
            app.given()
                    .when().get("/hello")
                    .then()
                    .statusCode(200)
                    .body(is("Hello from Quarkus REST"));
            Thread.sleep(1000);
            Response response = thenRetrieveTraces(pageLimit, "1h", "otel", operationName);
            response.then()
                    .body("data", empty());


    }

    @Test
    void testYesTracesOnHelloEndpointInTenSecond() throws InterruptedException {
        int pageLimit = 10;
        String operationName = "GET /hello";
        app.given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
        Thread.sleep(10000);
        Response response = thenRetrieveTraces(pageLimit, "1h", "otel", operationName);
        response.then()
                .body("data", empty());

    }

    private Response thenRetrieveTraces(int pageLimit, String lookBack, String serviceName, String operationName) {
        Response response = given()
                .when()
                .queryParam("operation", operationName)
                .queryParam("lookback", lookBack)
                .queryParam("limit", pageLimit)
                .queryParam("service", serviceName)
                .get(jaeger.getTraceUrl());
        LOG.info("traces are supposed to be disable , but -->  " + response.asPrettyString());
        return response;

    }


}