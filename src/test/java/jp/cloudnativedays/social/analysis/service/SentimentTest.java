package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SentimentTest {

	private Sentiment sentiment;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(
			Objects.requireNonNull(classLoader.getResource("data/test-data.trim")).getFile());

	@BeforeEach
	void setUp() {
		SentimentLoader sentimentLoader = new SentimentLoader(sentiFile);
		sentiment = new Sentiment(sentimentLoader);
	}

	@Test
	void getSentimentScore() {
		final String positiveString = "ていねい";
		final String negativeString = "ずぼら";
		final String neutralString = "はにかみ";
		final String posNegString = "ぐうたら";
		assertEquals(1, sentiment.getSentimentScore(positiveString));
		assertEquals(-1, sentiment.getSentimentScore(negativeString));
		assertEquals(0, sentiment.getSentimentScore(neutralString));
		assertEquals(0, sentiment.getSentimentScore(posNegString));
	}

}