# no-accept-header-reproducer

This is a no-accept-header-reproducer bug with quarkus-resteasy.
If you comment the dependency :
```
<dependency>
<groupId>io.quarkus</groupId>
<artifactId>quarkus-resteasy</artifactId>
</dependency>
```

and uncomment the next one dependency , it will works:
```
<!-- <dependency>
     <groupId>io.quarkus</groupId>
     <artifactId>quarkus-rest</artifactId>
 </dependency>-->
```
## Running the application 

You can run your application in dev mode that enables live coding using:

```
mvn clean verify
```

## Related Guides

- RESTEasy Classic ([guide](https://quarkus.io/guides/resteasy)): REST endpoint framework implementing Jakarta REST and more

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
