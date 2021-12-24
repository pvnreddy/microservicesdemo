package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.twitter.to.kafka.service.config.TwitterToKafkaServiceConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices.demo")
public class TwitterToKafkaServiceApplication implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    public TwitterToKafkaServiceApplication(TwitterToKafkaServiceConfigData configData) {
        this.twitterToKafkaServiceConfigData = configData;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }
    //This method used with Commandlinerunner implementation
    //@Override
    public void run(String... args) throws Exception {
        LOG.info("App starts...");
        LOG.info(Arrays.toString(twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[] {})));
        LOG.info(twitterToKafkaServiceConfigData.getWelcomeMessage1());
    }
    //This method used with Applicationrunner implementation
    @Override
    public void run(ApplicationArguments args) throws Exception {
        LOG.info("Application starts.....");
        LOG.info(Arrays.toString(twitterToKafkaServiceConfigData.getTwitterKeywords().toArray()));
        LOG.info(twitterToKafkaServiceConfigData.getWelcomeMessage1());

    }
}
