package com.email.service.cucumber;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;

import static org.hamcrest.MatcherAssert.assertThat;

public class EmailSteps extends TestSteps {


    @When("I have a new request")
    public void renewResponse() {
        reset();
    }

    @When("request contains json ([^ ]+)")
    public void addJsonToRequest(String path) throws Exception{
        Gson gson = new Gson();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        JsonReader reader = new JsonReader(new FileReader(file));
        JsonElement data = gson.fromJson(reader, JsonElement.class);
        testRunResponse.setData(gson.toJson(data));
    }

    @When("User sends a (POST|GET) request for ([^ ]+)")
    public void executeMethod(String method, String url) throws Throwable {
        try {
            if (method.equals("GET")) {
                executeGet(domain + url);
            } else if (method.equals("POST")) {
                executePost(domain + url);
            }
        }catch (Exception ex){
            testRunResponse.setErrorOccured(true);
        }
    }

    @Then("eventually the response status code is ([^ ]+)")
    public void checkHttpStatusCode(int httpStatus) {
        assertThat("Http Response Should Be " + httpStatus + " but was "+ testRunResponse.getStatus().value(), testRunResponse.getStatus().value() == httpStatus);
    }

    @Then("an Error should Occur")
    public void checkErrorOccurrance(){
        assertThat("Error is expected to Occur here", testRunResponse.isErrorOccured());
    }
}
