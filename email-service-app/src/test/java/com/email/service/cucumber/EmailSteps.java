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
        assertThat("Response should be new again", getTestRunResponse().getStatus() == null);
    }

    @When("request contains json ([^ ]+)")
    public void addJsonToRequest(String path) throws Exception {
        Gson gson = new Gson();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        JsonReader reader = new JsonReader(new FileReader(file));
        JsonElement data = gson.fromJson(reader, JsonElement.class);
        getTestRunResponse().setData(gson.toJson(data));
    }

    @When("User sends a (POST|GET) request for ([^ ]+)")
    public void executeMethod(String method, String url) throws Throwable {
        try {
            if (method.equals("GET")) {
                executeGet(domain + url);
            } else if (method.equals("POST")) {
                executePost(domain + url);
            }
        } catch (Exception ex) {
            getTestRunResponse().setErrorOccured(true);
        }
    }

    @Then("eventually the response status code is ([^ ]+)")
    public void checkHttpStatusCode(int httpStatus) {
        if (!getTestRunResponse().isErrorOccured()) {
            assertThat("HTTP Response should not be null at this point value:" + getTestRunResponse().toString() + " " + getTestRunResponse().getData() + " " + getTestRunResponse().isErrorOccured(), null != getTestRunResponse().getStatus());
            assertThat("Http Response Should Be " + httpStatus + " but was " + getTestRunResponse().getStatus(), getTestRunResponse().getStatus().value() == httpStatus);
        }
    }

    @Then("an Error should Occur")
    public void checkErrorOccurrance() {
        assertThat("Error is expected to Occur here", getTestRunResponse().isErrorOccured());
    }
}
