package io.quarkus.brotli4j;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.QuarkusApplication;
import io.restassured.response.Response;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusScenario
class Brotli4JHttpTest {
    private static final Logger LOG = Logger.getLogger(Brotli4JHttpTest.class);

    @QuarkusApplication(classes = { Brotli4JHttpServerConfig.class, Brotli4JResource.class })
    static final RestService app = new RestService();

    private final static String DEFAULT_TEXT_PLAIN = Brotli4JResource.DEFAULT_TEXT_PLAIN;

    private final static String BROTLI_ENCODING = "br";


    @Test
    public void checkTextPlainWithtBrotli4J() {
        assertBrotli4JCompression("/compression/text", MediaType.TEXT_PLAIN, BROTLI_ENCODING, BROTLI_ENCODING,
                Brotli4JResource.DEFAULT_TEXT_PLAIN.length());
    }

    public void assertBrotli4JCompression(String path, String contentHeaderType, String acceptHeaderEncoding,
                                          String expectedHeaderContentEncoding, int originalContentLength) {
        Response response = app.given()
                .when()
                .contentType(contentHeaderType)
                .header(HttpHeaders.ACCEPT_ENCODING, acceptHeaderEncoding)
                .get(path)
                .then()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_ENCODING, expectedHeaderContentEncoding)
                .extract().response();
        byte[] responseBody = response.getBody().asByteArray();
        int compressedContentLength = responseBody.length;
        assertTrue(compressedContentLength < originalContentLength);
    }


}