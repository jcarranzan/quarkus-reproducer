package io.quarkus.brotli4j;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/compression")
public class Brotli4JResource {

    public final static String DEFAULT_TEXT_PLAIN = "In life, you have to trust that every little bit helps. As you know," +
            " every small step forward counts." +
            " It's the accumulation of these efforts that ultimately leads to success." +
            " So, don't underestimate the power of persistence and determination in achieving your dreams";

    @GET
    @Path("/text")
    @Produces(MediaType.TEXT_PLAIN)
    public String textHttpCompressionResponse() {
        return DEFAULT_TEXT_PLAIN;
    }
}