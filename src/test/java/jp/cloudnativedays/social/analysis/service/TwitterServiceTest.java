package jp.cloudnativedays.social.analysis.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import twitter4j.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitterServiceTest {

	private TwitterService twitterService;

	private final MeterRegistry registry = new SimpleMeterRegistry();

	private final TwitterMetrics twitterMetrics = new TwitterMetrics(registry);

	@Mock
	private Twitter twitter;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(
			Objects.requireNonNull(classLoader.getResource("data/test-data.trim")).getFile());

	@BeforeEach
	void setUp() {

		SentimentLoader sentimentLoader = new SentimentLoader(sentiFile);
		Sentiment sentiment = new Sentiment(sentimentLoader);
		MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();
		String[] queryStrings = new String[] { "dummy" };

		twitterService = new TwitterService(sentiment, morphologicalAnalysis, twitterMetrics, queryStrings, twitter);
	}

	@Test
	void searchPositiveTwitterAndSetMetrics() throws TwitterException {
		List<Status> statuses = new ArrayList<>();

		Status positiveStatus = mock(Status.class);
		when(positiveStatus.getId()).thenReturn(100L);
		when(positiveStatus.getText()).thenReturn("かれはていねいです");
		when(positiveStatus.getLang()).thenReturn("ja");
		when(positiveStatus.isRetweet()).thenReturn(false);
		when(positiveStatus.getRetweetCount()).thenReturn(10);
		when(positiveStatus.getFavoriteCount()).thenReturn(20);

		statuses.add(positiveStatus);

		QueryResult queryResult = mock(QueryResult.class);
		when(queryResult.getTweets()).thenReturn(statuses);
		when(queryResult.hasNext()).thenReturn(false);
		when(queryResult.nextQuery()).thenReturn(null);

		Mockito.when(twitter.search(any())).thenReturn(queryResult);

		twitterService.searchTwitterAndSetMetrics();

		Gauge gauge1 = registry.get("social.twitter.sentiment").tags("tweetId", "100").gauge();
		Gauge gauge2 = registry.get("social.twitter.retweets").tags("tweetId", "100").gauge();
		Gauge gauge3 = registry.get("social.twitter.favorites").tags("tweetId", "100").gauge();

		assertEquals(1, gauge1.value());
		assertEquals(10, gauge2.value());
		assertEquals(20, gauge3.value());

	}

	@Test
	void searchNegativeTwitterAndSetMetrics() throws TwitterException {
		List<Status> statuses = new ArrayList<>();

		Status positiveStatus = mock(Status.class);
		when(positiveStatus.getId()).thenReturn(100L);
		when(positiveStatus.getText()).thenReturn("かれはずぼらです");
		when(positiveStatus.getLang()).thenReturn("ja");
		when(positiveStatus.isRetweet()).thenReturn(false);
		when(positiveStatus.getRetweetCount()).thenReturn(10);
		when(positiveStatus.getFavoriteCount()).thenReturn(20);

		statuses.add(positiveStatus);

		QueryResult queryResult = mock(QueryResult.class);
		when(queryResult.getTweets()).thenReturn(statuses);
		when(queryResult.hasNext()).thenReturn(false);
		when(queryResult.nextQuery()).thenReturn(null);

		Mockito.when(twitter.search(any())).thenReturn(queryResult);

		twitterService.searchTwitterAndSetMetrics();

		Gauge gauge1 = registry.get("social.twitter.sentiment").tags("tweetId", "100").gauge();
		Gauge gauge2 = registry.get("social.twitter.retweets").tags("tweetId", "100").gauge();
		Gauge gauge3 = registry.get("social.twitter.favorites").tags("tweetId", "100").gauge();

		assertEquals(-1, gauge1.value());
		assertEquals(10, gauge2.value());
		assertEquals(20, gauge3.value());

	}

}