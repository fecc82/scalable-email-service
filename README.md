# Scalable Email Service

Fully Scalable Email Service

## Getting Started
When running the install command Docker/Maven needs installed/running in your machine.
This will compile the codes, create images, run containers, run tests on it and stop containers.
See Testing Framework below for more details.

###Running Locally
JAR file:
```
java -jar email-service-app-1.0-SNAPSHOT.jar
```
Docker image:
```
docker run -p 8080:8080 mel3kings/scalable-email-service
```

## Technology Stack

* Spring-platform - Spring-Platform as the application framework, Using dependency injection. This enables us to swap out implementation easily buy using interfaces instead of direct implemnetations.
* Spring-Boot -  Spring-boot as a lightweight server to run microservice
* Akka - for handling concurrency by using Actor Model together with Java 8 allows for asynchronous processing of request
* Docker - for creating images of the application, and run containers based on said images
* Maven -  Dependency Management
* Cucumber - Testing framework, that also gives us a low-level documentation and acts a source of truth for payloads
* MailGun and SendGrid - Third Party Email Integration

## Architectual Notes

### Implementations Notes
* We are using [Actor Model](https://doc.akka.io/docs/akka/current/guide/actors-motivation.html) to handle calls, this enables calls to be non-blocking and allows asynchronous processing
* Currently we have One Actor System for the application, but this prepares it for cluster. An Actor System can have multiple Actors, which in turn have their own mailboxes which they events one by one without blocking each other. 

We fetch one Actor from an Actor System(Singleton), and let the Actor do the work. Sends back the response asynchronously
```
ActorRef r = actorSystem.actorOf(Props.create(EmailSenderActor.class,services), EMAIL_ACTOR);
```

At this point we have already given the actor all the services needed, 
he will then try to execute the task to send email until one of the services (MailGun or SendGrid) returns a successful response

We are purposely adding a Local email Service to test a failure scenario, whereas one of the email service is down.
This handles any failovers without affecting user experience.

This all done asynchronously, should all services fail the Actor Timeout will throw an exception as no response will be returned during the duration. Timeout is configurable.
```
  public void sendEmail(SendEmailRequest request) {
        for (EmailService service : emailServiceSet) {
            SendEmailResponse response = service.send(request);
            if (response.getStatus() == HttpStatus.OK || response.getStatus() == HttpStatus.ACCEPTED) {
                getSender().tell(response, ActorRef.noSender());
                break;
            }
        }
    }
```


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
* mvn Creates a Docker images from that jar, with the tag: test
* mvn runs docker container from that tag 
* mvn runs cucumber on specified docker container
* cucumber picks up which environment he is on (Set by System Variable), and run tests accordingly
* Stops docker container
* creates fimal container with new tag:latest for deployment


### Room for Improvement

* We can further segregate Modules as -app -api -impl -test, but for simplicities sake I have chosen to put all in the -api for now
* We can further segregate Http Clients to have different implementations, but as for the current requirement, I have kept it simple and used Spring-boot and API provided Send Grid to generate HTTP requests\
* Fine Tune HTTP Response Code, Currently 202 is used as Error Response
* Not Hardcoding parameters in pom.xml
* Convert Akka to cluster will require multiple nodes running at the same time
* Although service is already stateless, Akka Actor model should not pass around mutable objects, as it may result into returning to normal Java concurrency and its drawbacks. See [Actor Model Best Practices](https://doc.akka.io/docs/akka/2.5.5/java/general/actor-systems.html)
* I was unable to implement cc and bcc on send grid due to accounts always being [disabled](https://github.com/sendgrid/sendgrid-nodejs/issues/283)

### Considerations
Other considerations to implement same result:

* NodeJS - also allows to create lightweight microservices similar to spring-boot
    * Easier to work with JSON but limits the environment to C# and Javascripts V8 engine
    * Easier to setup rather than Spring platform
* NPM vs Maven - should we have gone with Node 
* Junit - against cucumber would have not provided us with low-level documentation, and payload source of truth
    * although it can be used to unit test individual methods/classes
* Play Framework vs Spring Boot 
* Guice vs Spring 

## Authors
* **Melchor Tatlonghari** - *Initial work* - [mel3kings](https://github.com/mel3kings)





