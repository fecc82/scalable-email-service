# Scalable Email Service

Fully Scalable Email Service

## Technology Stack

* Spring-platform - Spring-Platform as the application framework, Using dependency injection. This enables us to swap out implementation easily buy using interfaces instead of direct implemnetations.
* Spring-Boot -  Spring-boot as a lightweight server to run microservice
* Akka - for handling concurrency by using Actor Model together with Java 8 allows for asynchronous web services
* Docker - for creating images of the application, and run containers based on said images
* [Maven](https://maven.apache.org/) - Dependency Management
* Cucumber - Testing framework, that also gives us a low-level documentation and acts a source of truth for payloads
* MailGun and SendGrid - Third Party Email Integration

## Architectual Notes

### Implementations Notes

* We are using interface/implementation for Services to enable us to easily swap out implementation when needed, main Application does not need to know how the implementation is done, just need to call send method

```
  @Autowired
    @Qualifier("mailGun")
    private EmailService emailSenderService;

    ...
    
    emailSenderService.send(email)
```

* We are using annotations to valid incoming JSON request, rather than checking manually.
 For Example:
 
```
public class SendEmailRequest {
    @NotNull(message = "Recipient cannot be null")
    @EachPattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}.", message = "Request contains an invalid email")
    private ArrayList<String> recipients;
```

* We are using Akka for concurrency and handling high volume request, together with Java 8 Completable Future making the microservice asynchronous and non-blocking

### Test Framework
Cucumber provides a low-level documentation via *.features files, and payloads are easily found in the test/resources folder

Cucumber + Docker + Maven = fully integrated

Sample Cucumber Test Case:
```
  Scenario: User should be able to send email on /send
    When I have a new request
    And request contains json payloads/email.json
    And User sends a POST request for /send
    Then eventually the response status code is 200

```
What happens when you run:
```
mvn install
```

* mvn does all the usual steps for compiling and initializing required
* mvn packages the jar
* mvn Creates a Docker images from that jar, with the tag: latest
* mvn runs docker container from that tag 
* mvn runs cucumber on specified docker container
* cucumber picks up which environment he is on (Set by System Variable), and run tests accordingly
* Stops docker container
* Ready for deployment

### TODO

* We can further segregate Modules as -app -api -impl -test, but for simplicities sake I have chosen to put all in the -api for now
* We can further segregate Http Clients to have different implementations, but as for the current requirement, I have kept it simple and used Spring-boot and API provided Send Grid to generate HTTP requests\
* Fine Tune HTTP Response Code, Currently 202 is used as Error Response
*


## Authors
* **Melchor Tatlonghari** - *Initial work* - [mel3kings](https://github.com/mel3kings)



