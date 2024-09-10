package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String sayHello(String name) {
        String message = "Hello " + name + "!";
        System.out.println(message);
        return message;
    }
}
