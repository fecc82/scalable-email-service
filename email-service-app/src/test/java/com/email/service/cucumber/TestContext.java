package com.email.service.cucumber;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TestContext {
    private Config config;

    public TestContext() {
        Config conf = ConfigFactory.load();
        Config environment = conf.getConfig("environment");
        if (null == System.getenv("env")) {
            this.config = environment.getConfig("local");
        } else {
            this.config = environment.getConfig(System.getenv("env"));
        }
    }

    public String getDomain() {
        return this.config.getString("server.url");
    }

}
