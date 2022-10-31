package jp.cloudnativedays.social.analysis.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import jp.cloudnativedays.social.analysis.client.TwitterClient;
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

	private SentimentLoader sentimentLoader;

	private final MeterRegistry registry = new SimpleMeterRegistry();

	private final TwitterMetrics twitterMetrics = new TwitterMetrics(registry);

	@Mock
	private TwitterClient twitterClient;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(
			Objects.requireNonNull(classLoader.getResource("data/test-data.trim")).getFile());

	@Mock
	private WordCount wordCount;

	@BeforeEach
	void setUp() {

		sentimentLoader = new SentimentLoader(sentiFile);
		MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();
		Sentiment sentiment = new Sentiment(sentimentLoader, morphologicalAnalysis);
		String[] queryStrings = new String[] { "dummy" };

		twitterService = new TwitterService(sentiment, twitterMetrics, twitterClient, queryStrings, wordCount);
	}

	@Test
	void searchPositiveTwitterAndSetMetrics() throws TwitterException {
		List<Status> statuses = new ArrayList<>();
		User user = mock(User.class);
		when(user.getScreenName()).thenReturn("tanaka");

		Status positiveStatus = mock(Status.class);
		when(positiveStatus.getId()).thenReturn(100L);
		when(positiveStatus.getText()).thenReturn("かれはていねいです");
		when(positiveStatus.getLang()).thenReturn("ja");
		when(positiveStatus.getUser()).thenReturn(user);
		when(positiveStatus.isRetweet()).thenReturn(false);
		when(positiveStatus.getRetweetCount()).thenReturn(10);
		when(positiveStatus.getFavoriteCount()).thenReturn(20);

		statuses.add(positiveStatus);

		QueryResult queryResult = mock(QueryResult.class);
		when(queryResult.getTweets()).thenReturn(statuses);
		when(queryResult.hasNext()).thenReturn(false);
		when(queryResult.nextQuery()).thenReturn(null);

		Mockito.when(twitterClient.search(any())).thenReturn(queryResult);

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
		User user = mock(User.class);
		when(user.getScreenName()).thenReturn("tanaka");

		Status positiveStatus = mock(Status.class);
		when(positiveStatus.getId()).thenReturn(100L);
		when(positiveStatus.getText()).thenReturn("かれはずぼらです");
		when(positiveStatus.getLang()).thenReturn("ja");
		when(positiveStatus.getUser()).thenReturn(user);
		when(positiveStatus.isRetweet()).thenReturn(false);
		when(positiveStatus.getRetweetCount()).thenReturn(10);
		when(positiveStatus.getFavoriteCount()).thenReturn(20);

		statuses.add(positiveStatus);

		QueryResult queryResult = mock(QueryResult.class);
		when(queryResult.getTweets()).thenReturn(statuses);
		when(queryResult.hasNext()).thenReturn(false);
		when(queryResult.nextQuery()).thenReturn(null);

		Mockito.when(twitterClient.search(any())).thenReturn(queryResult);

		twitterService.searchTwitterAndSetMetrics();

		Gauge gauge1 = registry.get("social.twitter.sentiment").tags("tweetId", "100").gauge();
		Gauge gauge2 = registry.get("social.twitter.retweets").tags("tweetId", "100").gauge();
		Gauge gauge3 = registry.get("social.twitter.favorites").tags("tweetId", "100").gauge();

		assertEquals(-1, gauge1.value());
		assertEquals(10, gauge2.value());
		assertEquals(20, gauge3.value());

	}

	@Test
	void searchTwitterAndSetMetricsWithNextQuery() throws TwitterException {

		Query dummyQuery1 = new Query("dummy" + " -filter:retweets");
		Query dummyQuery2 = new Query("dummy2" + " -filter:retweets");

		List<Status> statusesLoop1 = new ArrayList<>();
		User user1 = mock(User.class);
		when(user1.getScreenName()).thenReturn("tanaka");

		Status statusLoop1 = mock(Status.class);
		when(statusLoop1.getId()).thenReturn(100L);
		when(statusLoop1.getText()).thenReturn("かれはぐうたらです");
		when(statusLoop1.getLang()).thenReturn("ja");
		when(statusLoop1.getUser()).thenReturn(user1);
		when(statusLoop1.isRetweet()).thenReturn(false);
		when(statusLoop1.getRetweetCount()).thenReturn(10);
		when(statusLoop1.getFavoriteCount()).thenReturn(20);

		statusesLoop1.add(statusLoop1);

		List<Status> statusesLoop2 = new ArrayList<>();
		User user2 = mock(User.class);
		when(user2.getScreenName()).thenReturn("nakata");

		Status statusLoop2 = mock(Status.class);
		when(statusLoop2.getId()).thenReturn(200L);
		when(statusLoop2.getText()).thenReturn("かれははにかみです");
		when(statusLoop2.getLang()).thenReturn("ja");
		when(statusLoop2.getUser()).thenReturn(user2);
		when(statusLoop2.isRetweet()).thenReturn(false);
		when(statusLoop2.getRetweetCount()).thenReturn(20);
		when(statusLoop2.getFavoriteCount()).thenReturn(5);

		statusesLoop2.add(statusLoop2);

		QueryResult queryResult2 = mock(QueryResult.class);
		when(queryResult2.getTweets()).thenReturn(statusesLoop2);
		when(queryResult2.hasNext()).thenReturn(false);
		when(queryResult2.nextQuery()).thenReturn(null);

		QueryResult queryResult1 = mock(QueryResult.class);
		when(queryResult1.getTweets()).thenReturn(statusesLoop1);
		when(queryResult1.hasNext()).thenReturn(true);
		when(queryResult1.nextQuery()).thenReturn(dummyQuery2);

		Mockito.when(twitterClient.search(dummyQuery1)).thenReturn(queryResult1);
		Mockito.when(twitterClient.search(dummyQuery2)).thenReturn(queryResult2);

		twitterService.searchTwitterAndSetMetrics();

		assertEquals(2, registry.get("social.twitter.sentiment").meters().size());
		assertEquals(2, registry.get("social.twitter.retweets").meters().size());
		assertEquals(2, registry.get("social.twitter.favorites").meters().size());

	}

	@Test
	void searchTwitterAndSetMetricsWith2Queries() throws TwitterException {

		MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();
		Sentiment sentiment = new Sentiment(sentimentLoader, morphologicalAnalysis);
		String[] queryStrings = new String[] { "dummy", "dummy2" };

		twitterService = new TwitterService(sentiment, twitterMetrics, twitterClient, queryStrings, wordCount);

		Query dummyQuery1 = new Query("dummy" + " -filter:retweets");
		Query dummyQuery2 = new Query("dummy2" + " -filter:retweets");

		List<Status> statusesLoop1 = new ArrayList<>();
		User user1 = mock(User.class);
		when(user1.getScreenName()).thenReturn("tanaka");

		Status statusLoop1 = mock(Status.class);
		when(statusLoop1.getId()).thenReturn(100L);
		when(statusLoop1.getText()).thenReturn("かれはぐうたらです");
		when(statusLoop1.getLang()).thenReturn("ja");
		when(statusLoop1.getUser()).thenReturn(user1);
		when(statusLoop1.isRetweet()).thenReturn(false);
		when(statusLoop1.getRetweetCount()).thenReturn(10);
		when(statusLoop1.getFavoriteCount()).thenReturn(20);

		statusesLoop1.add(statusLoop1);

		List<Status> statusesLoop2 = new ArrayList<>();
		User user2 = mock(User.class);
		when(user2.getScreenName()).thenReturn("nakata");

		Status statusLoop2 = mock(Status.class);
		when(statusLoop2.getId()).thenReturn(200L);
		when(statusLoop2.getText()).thenReturn("かれははにかみです");
		when(statusLoop2.getLang()).thenReturn("ja");
		when(statusLoop2.getUser()).thenReturn(user2);
		when(statusLoop2.isRetweet()).thenReturn(false);
		when(statusLoop2.getRetweetCount()).thenReturn(20);
		when(statusLoop2.getFavoriteCount()).thenReturn(5);

		statusesLoop2.add(statusLoop2);

		QueryResult queryResult2 = mock(QueryResult.class);
		when(queryResult2.getTweets()).thenReturn(statusesLoop2);
		when(queryResult2.hasNext()).thenReturn(false);
		when(queryResult2.nextQuery()).thenReturn(null);

		QueryResult queryResult1 = mock(QueryResult.class);
		when(queryResult1.getTweets()).thenReturn(statusesLoop1);
		when(queryResult1.hasNext()).thenReturn(false);
		when(queryResult1.nextQuery()).thenReturn(dummyQuery2);

		Mockito.when(twitterClient.search(dummyQuery1)).thenReturn(queryResult1);
		Mockito.when(twitterClient.search(dummyQuery2)).thenReturn(queryResult2);

		twitterService.searchTwitterAndSetMetrics();

		assertEquals(2, registry.get("social.twitter.sentiment").meters().size());
		assertEquals(2, registry.get("social.twitter.retweets").meters().size());
		assertEquals(2, registry.get("social.twitter.favorites").meters().size());

	}

}