package org.acme;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.function.Supplier;


@ApplicationScoped
public class MySecurityIdentityAugmentor implements SecurityIdentityAugmentor {
    @Inject
    @RestClient
    ExampleRestClient exampleRestClient;

    @Inject
    @CacheName("my-cache")
    Cache cache;

    @Inject
    Instance<SecurityIdentitySupplier> identitySupplierInstance;


    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        SecurityIdentitySupplier identitySupplier = identitySupplierInstance.get();
        identitySupplier.setIdentity(identity);

        return getNonBlockingExpensiveValue("test").map(unused -> identitySupplier.get());
    }

    private Uni<String> getNonBlockingExpensiveValue(String key) {
        return cache.getAsync(key, unused -> exampleRestClient.getHtml());
    }
}

@Dependent
class SecurityIdentitySupplier implements Supplier<SecurityIdentity> {

    private SecurityIdentity identity;

    public void setIdentity(SecurityIdentity identity) {
        this.identity = identity;
    }

    @ActivateRequestContext
    @Override
    public SecurityIdentity get() {
        return QuarkusSecurityIdentity.builder(identity)
                .addRole("user")
                .build();
    }
}

