package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SentimentTest {

	private Sentiment sentiment;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(
			Objects.requireNonNull(classLoader.getResource("data/test-data.trim")).getFile());

	@BeforeEach
	void setUp() {
		SentimentLoader sentimentLoader = new SentimentLoader(sentiFile);
		MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();
		sentiment = new Sentiment(sentimentLoader, morphologicalAnalysis);
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

	@Test
	void getSentimentScoreFromSentence() {
		final String sentence1 = "かれはていねいです。";
		final String sentence2 = "かれはずぼらです。";
		assertEquals(1, sentiment.getSentimentScoreFromSentence(sentence1));
		assertEquals(-1, sentiment.getSentimentScoreFromSentence(sentence2));
	}

}