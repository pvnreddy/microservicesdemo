package com.microservices.demo.twitter.to.kafka.service.listener;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.producer.config.service.KafkaProducer;
import com.microservices.demo.twitter.to.kafka.service.transform.TwitterStatusToAvroModelTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {
 private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);
    private final KafkaProducer kafkaProducer;
    private  final KafkaConfigData kafkaConfigData;
    private final TwitterStatusToAvroModelTransform transform;
    public TwitterKafkaStatusListener(KafkaProducer kafkaProducer, KafkaConfigData kafkaConfigData, TwitterStatusToAvroModelTransform transform) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaConfigData = kafkaConfigData;
        this.transform = transform;
    }

    @Override
    public void onStatus(Status status) {

        LOG.info(status.getUser().getName() + " : " + status.getText());
        LOG.info("Received text {} sending it to kafka topic {}",status.getText(),kafkaConfigData.getTopicName());

        TwitterAvroModel twitterAvroModel=transform.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(),status.getUser().getId(),twitterAvroModel);

    }
}
