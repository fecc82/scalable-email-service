package com.email.service.cucumber;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TestContext {
    private Config config;
    private final String ENVIRONMENT = "ENVIRONMENT";

    public TestContext() {
        Config conf = ConfigFactory.load();
        Config environment = conf.getConfig("environment");
        if (null == System.getenv(ENVIRONMENT)) {
            this.config = environment.getConfig("local");
        } else {
            this.config = environment.getConfig(System.getenv(ENVIRONMENT));
        }
    }

    public String getDomain() {
        return this.config.getString("server.url");
    }

}
