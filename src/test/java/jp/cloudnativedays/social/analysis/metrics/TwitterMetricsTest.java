package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jp.cloudnativedays.social.analysis.model.TweetData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwitterMetricsTest {

	private final MeterRegistry registry = new SimpleMeterRegistry();

	private final TwitterMetrics twitterMetrics = new TwitterMetrics(registry);

	private TweetData tweetData1;

	private TweetData tweetData2;

	private TweetData tweetData3;

	@BeforeEach
	public void setup() {
		tweetData1 = new TweetData("11111", "aaaaa", true);
		tweetData1.setSentimentScore(10);
		tweetData1.setRetweetCount(5);
		tweetData1.setFavoriteCount(2);
		tweetData2 = new TweetData("22222", "bbbbb", false);
		tweetData2.setSentimentScore(-10);
		tweetData2.setRetweetCount(-5);
		tweetData2.setFavoriteCount(-2);
		tweetData3 = new TweetData("33333", "ccccc", false);

		twitterMetrics.setMetrics(tweetData1);
		twitterMetrics.setMetrics(tweetData2);
	}

	@Test
	void setSentimentMetrics() {
		String metricsName = "social.twitter.sentiment";

		Gauge gauge1 = registry.get(metricsName).tags("tweetId", "11111").gauge();
		Gauge gauge2 = registry.get(metricsName).tags("tweetId", "22222").gauge();

		assertEquals(registry.get(metricsName).meters().size(), 2);
		assertEquals(gauge1.value(), 10);
		assertEquals(gauge2.value(), -10);
	}

	// Since calculating sentiment is a heavy task we do not perform this when the metrics
	// is already created.
	@Test
	void setSentimentMetricsTwice() {
		String metricsName = "social.twitter.sentiment";
		// Create tweetData4 which is same with tweetData1 but has different sentiment
		TweetData tweetData4 = new TweetData("11111", "aaaaa", true);
		tweetData4.setSentimentScore(20);
		tweetData4.setRetweetCount(5);
		tweetData4.setFavoriteCount(2);

		twitterMetrics.setMetrics(tweetData4);

		Gauge gauge1 = registry.get(metricsName).tags("tweetId", "11111").gauge();

		// Should equal to twitterdata1(10) not twitterdata4(20)
		assertEquals(gauge1.value(), 10);

	}

	@Test
	void isSentimentSet() {
		assertEquals(true, twitterMetrics.isSentimentSet(tweetData1));
		assertEquals(false, twitterMetrics.isSentimentSet(tweetData3));
	}

	@Test
	void setRetweetCounts() {

		String metricsName = "social.twitter.retweets";

		Gauge gauge1 = registry.get(metricsName).tags("tweetId", "11111").gauge();
		Gauge gauge2 = registry.get(metricsName).tags("tweetId", "22222").gauge();

		assertEquals(registry.get(metricsName).meters().size(), 2);
		assertEquals(gauge1.value(), 5);
		assertEquals(gauge2.value(), -5);
	}

	@Test
	void setRetweetCountsTwice() {
		String metricsName = "social.twitter.retweets";
		// Create tweetData4 which is same with tweetData1 but has different retweets
		TweetData tweetData4 = new TweetData("11111", "aaaaa", true);
		tweetData4.setSentimentScore(10);
		tweetData4.setRetweetCount(10);
		tweetData4.setFavoriteCount(2);

		twitterMetrics.setMetrics(tweetData4);

		Gauge gauge1 = registry.get(metricsName).tags("tweetId", "11111").gauge();

		// Should equal to twitterdata4(10) not twitterdata1(5)
		assertEquals(gauge1.value(), 10);

	}

	@Test
	void setFavoriteCounts() {
		String metricsName = "social.twitter.favorites";

		Gauge gauge1 = registry.get(metricsName).tags("tweetId", "11111").gauge();
		Gauge gauge2 = registry.get(metricsName).tags("tweetId", "22222").gauge();

		assertEquals(registry.get(metricsName).meters().size(), 2);
		assertEquals(gauge1.value(), 2);
		assertEquals(gauge2.value(), -2);
	}

	@Test
	void setFavoriteCountsTwice() {
		String metricsName = "social.twitter.favorites";
		// Create tweetData4 which is same with tweetData1 but has different favorites
		TweetData tweetData4 = new TweetData("11111", "aaaaa", true);
		tweetData4.setSentimentScore(10);
		tweetData4.setRetweetCount(5);
		tweetData4.setFavoriteCount(1);

		twitterMetrics.setMetrics(tweetData4);

		Gauge gauge1 = registry.get(metricsName).tags("tweetId", "11111").gauge();

		// Should equal to twitterdata4(1) not twitterdata1(2)
		assertEquals(gauge1.value(), 1);

	}

}