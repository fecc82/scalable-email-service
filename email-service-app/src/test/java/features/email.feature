Feature: User should be able to send email and check API health

  Scenario: User should not be able to POST on health method
    When I have a new request
    And User sends a POST request for /health
    Then an Error should Occur

  Scenario: User checks API health using GET method
    When I have a new request
    And User sends a GET request for /health
    Then eventually the response status code is 200

  Scenario: User should be able to send email on /send
    When I have a new request
    And request contains json payloads/email.json
    And User sends a POST request for /send
    Then eventually the response status code is 200

  Scenario: User should receive an error if he tries to send invalid email
    When I have a new request
    And request contains json payloads/invalid_email.json
    And User sends a POST request for /send
    Then eventually the response status code is 202

  Scenario: User should receive an error when he tries to send with no header
    When I have a new request
    And request contains json payloads/invalid_title.json
    And User sends a POST request for /send
    Then eventually the response status code is 202

  Scenario: User should receive an error when he tries to send with no body
    When I have a new request
    And request contains json payloads/invalid_body.json
    And User sends a POST request for /send
    Then eventually the response status code is 202

  Scenario: User should get an error when he puts an invalid email on cc list
    When I have a new request
    And request contains json payloads/invalid_cc.json
    And User sends a POST request for /send
    Then eventually the response status code is 202

  Scenario: User should get an error when he puts an invalid email on bcc list
    When I have a new request
    And request contains json payloads/invalid_bcc.json
    And User sends a POST request for /send
    Then eventually the response status code is 202

  Scenario: User should get an error message when he sends a invalid json format
    When I have a new request
    And request contains json payloads/invalid_json.json
    And User sends a POST request for /send
    Then eventually the response status code is 202

  Scenario: User should not be able to GET on send method
    When I have a new request
    And User sends a GET request for /send
    Then an Error should Occur