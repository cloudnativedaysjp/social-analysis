package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SentimentTest {

	private Sentiment sentiment;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(classLoader.getResource("data/pn.csv.m3.120408.trim").getFile());

	@BeforeEach
	void setUp() {
		SentimentLoader sentimentLoader = new SentimentLoader(sentiFile);
		sentiment = new Sentiment(sentimentLoader);
	}

	@Test
	void getSentimentScore() {
		final String positiveString = "あざやか";
		final String negativeString = "あだおろそか";
		final String neutralString = "あなたがた";
		final String posNegString = "ぐうたら";
		assertEquals(1, sentiment.getSentimentScore(positiveString));
		assertEquals(-1, sentiment.getSentimentScore(negativeString));
		assertEquals(0, sentiment.getSentimentScore(neutralString));
		assertEquals(0, sentiment.getSentimentScore(posNegString));
	}

}