package io.quarkus;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.QuarkusApplication;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusScenario
public class GzipMaxInputIT {

    final byte[] gzip_max_input_1K = new byte[1000];
    final byte[] gzip_max_input_10M = new byte[10000000];

    final byte[] gzip_same_1K_limit_max = new byte[1024];

    final byte[] zero_bytes = new byte[0];

    @QuarkusApplication(classes = {GzipResource.class, GzipClientService.class}, properties = "application.properties")
    static RestService app = new RestService();

    /**
     * According "All configurations options" guide the property 'quarkus.resteasy.gzip.max-input' refers to
     * <p>
     * Maximum deflated file bytes size
     * <p>
     * If the limit is exceeded, Resteasy will return Response with status 413("Request Entity Too Large")
     */

    @Test
    void testGzipOverTheMaxLimit() throws IOException {

        byte[] compressedData = generateCompressedData(gzip_max_input_10M);

        Response response = app.given()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Encoding", "gzip")
                .body(compressedData)
                .when()
                .post("/gzip")
                .then()
                .extract().response();
        assertEquals(HttpStatus.SC_REQUEST_TOO_LONG, response.statusCode());
    }

    @Test
    void testGzipAboveTheMaxLimit() throws IOException {
        // We are sending less than 1k bytes
        byte[] compressedData = generateCompressedData(gzip_max_input_1K);

       app.given()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Encoding", "gzip")
                .body(compressedData)
                .when()
                .post("/gzip")
                .then().log().all();

    }

    @Test
    void sendMaxLimit1k() throws IOException {
        byte[] compressedData = generateCompressedData(gzip_same_1K_limit_max);

        Response response = app.given()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Encoding", "gzip")
                .body(compressedData)
                .when()
                .post("/gzip")
                .then()
                .extract().response();
        assertEquals(HttpStatus.SC_OK, response.statusCode());
    }

    @Test
    void sendZeroValue() throws IOException {
        byte[] compressedData = generateCompressedData(zero_bytes);

        Response response = app.given()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Encoding", "gzip")
                .body(compressedData)
                .when()
                .post("/gzip")
                .then()
                .extract().response();
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode());
    }

    private byte[] generateCompressedData(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        gzipOut.write(data);
        gzipOut.close();
        return baos.toByteArray();
    }
}