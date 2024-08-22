package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "example-client")
@ApplicationScoped
public interface ExampleRestClient {

    @GET
    @Produces({MediaType.TEXT_HTML})
    @Path("")
     Uni<String> getHtml();
}
