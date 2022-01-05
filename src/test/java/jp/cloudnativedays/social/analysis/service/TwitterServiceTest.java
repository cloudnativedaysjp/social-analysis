package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TwitterServiceTest {

    private TwitterService twitterService;

    private final ClassLoader classLoader = getClass().getClassLoader();

    private final File sentiFile = new File(classLoader.getResource("data/pn.csv.m3.120408.trim").getFile());

    @BeforeEach
    void setUp() {
        SentimentLoader sentimentLoader = new SentimentLoader(sentiFile);
        Sentiment sentiment = new Sentiment(sentimentLoader);

    }

    @Test
    void searchTwitterAndSetMetrics() {
    }
}