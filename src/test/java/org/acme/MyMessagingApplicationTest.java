package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.test.junit.QuarkusTest;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
class MyMessagingApplicationTest {

    @Inject
    MyMessagingApplication application;

    @Test
    void test() {
        assertEquals("HELLO", application.toUpperCase(Message.of("Hello")).getPayload());
        assertEquals("BONJOUR", application.toUpperCase(Message.of("bonjour")).getPayload());
    }

}
