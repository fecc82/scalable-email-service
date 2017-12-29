package com.email.service.cucumber;

import com.email.service.EmailIntegrationTest;
import com.email.service.EmailServiceResponse;
import com.email.service.data.SendEmailResponse;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class TestSteps extends EmailIntegrationTest {
    private EmailServiceResponse testRunResponse;
    private RestTemplate restTemplate;
    private TestContext testContext;
    protected String domain;

    public TestSteps() {
        reset();
    }

    public void reset() {
        testContext = new TestContext();
        domain = testContext.getDomain();
        restTemplate = new RestTemplate();
        testRunResponse = new EmailServiceResponse();
    }

    void executeGet(String url) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        restTemplate.execute(url, HttpMethod.GET, null, new ResponseExtractor<Object>() {
            public Object extractData(ClientHttpResponse clientResponse) throws IOException {
                getTestRunResponse().setStatus(clientResponse.getStatusCode());
                return clientResponse;
            }
        });
    }

    void executePost(String url) {
        HttpHeaders apiHeader = new HttpHeaders();
        apiHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(getTestRunResponse().getData(), apiHeader);
        ResponseEntity<SendEmailResponse> apiResponse = restTemplate.postForEntity(url, entity, SendEmailResponse.class);
        assertThat("Response is OK or Accepted", apiResponse.getStatusCode() == HttpStatus.OK || apiResponse.getStatusCode() == HttpStatus.ACCEPTED);
        SendEmailResponse response = apiResponse.getBody();
        getTestRunResponse().setStatus(response.getStatus());
    }

    public EmailServiceResponse getTestRunResponse() {
        return testRunResponse;
    }

    public void setTestRunResponse(EmailServiceResponse testRunResponse) {
        this.testRunResponse = testRunResponse;
    }
}
