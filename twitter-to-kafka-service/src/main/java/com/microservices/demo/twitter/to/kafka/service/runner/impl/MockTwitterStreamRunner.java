package com.microservices.demo.twitter.to.kafka.service.runner.impl;


import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.exception.TwitterToKafkaServiceException;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@ConditionalOnProperty(name="twitter-to-kafka-service.mocktweet-enabled", havingValue = "true")
public class MockTwitterStreamRunner implements StreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MockTwitterStreamRunner.class);
    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;
    private static final Random RANDOM=new Random();
    private static final String [] WORDS = new String[]{"Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetur",
            "adipiscing",
            "elit",
            "Curabitur",
            "in",
            "ullamcorper",
            "libero",
            "Orci",
            "varius",
            "natoque",
            "penatibus",
            "et",
            "magnis",
            "dis",
            "parturient",
            "montes",
            "nascetur",
            "ridiculus",
            "mus",
            "Nunc",
            "nec",
            "condimentum",
            "erat",
            "at",
            "laoreet"};
    private static final String tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}";

    private static final String TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";


    public MockTwitterStreamRunner(TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData, TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    @Override
    public void start() throws TwitterException{
        final String [] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
        final int minTweetLength = twitterToKafkaServiceConfigData.getMintweetLength();
        final int maxTweetLength = twitterToKafkaServiceConfigData.getMaxtweetLength();
        Long sleetTimeMs = twitterToKafkaServiceConfigData.getTweetInterval();
        LOG.info("Starting mock filtering twitter stream for keywords {}"+ Arrays.toString(keywords));
        simulateTwitterStream(keywords, minTweetLength, maxTweetLength, sleetTimeMs);


    }

    private void simulateTwitterStream(String[] keywords, int minTweetLength, int maxTweetLength, Long sleetTimeMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    String formatedTweetAsRawJson = getFormattedTweet(keywords, minTweetLength, maxTweetLength);
                    LOG.info("Tweet: " + formatedTweetAsRawJson);
                    Status status = TwitterObjectFactory.createStatus(formatedTweetAsRawJson);
                    twitterKafkaStatusListener.onStatus(status);
                    sleep(sleetTimeMs);
                }
            }
            catch (TwitterException e){
                LOG.info("Error while getting Twitter status object" +e.getErrorMessage());
            }

        });

    }

    private void sleep(Long sleetTimeMs) {
        try {
            Thread.sleep(sleetTimeMs);
        } catch (InterruptedException e) {
            throw new TwitterToKafkaServiceException("Error while sleeping for waiting new status to create !!");
        }
    }

    private String getFormattedTweet(String[] keywords, int minTweetLength, int maxTweetLength) {

        String[] params = new String[]{
                 ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                 String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
                 getRandomTweetContent(keywords,minTweetLength,maxTweetLength),
                 String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
         };
        return formatTweetAsJSONWithParams(params);
    }

    private String formatTweetAsJSONWithParams(String[] params) {
        String tweet = tweetAsRawJson;
        for(int i = 0; i< params.length; i++){
            tweet = tweet.replace("{"+i+"}", params[i]);

        }
        return tweet;
    }

    private String getRandomTweetContent(String[] keywords, int minTweetLength, int maxTweetLength) {
        StringBuilder tweet = new StringBuilder();
        int tweetLengh=RANDOM.nextInt(maxTweetLength-minTweetLength+1)+minTweetLength;
        return constructRandomTweet(keywords, tweet, tweetLengh);

    }

    private String constructRandomTweet(String[] keywords, StringBuilder tweet, int tweetLengh) {
        for (int i = 0; i< tweetLengh; i++){
           /* tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            if(i== tweetLengh /2){
                tweet.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }*/

            if(i%2 == 0){
                tweet.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(" ");
            }
            else{
                tweet.append(keywords[RANDOM.nextInt(keywords.length)]).append(" ");
            }

        }

        return tweet.toString().trim();
    }
}
