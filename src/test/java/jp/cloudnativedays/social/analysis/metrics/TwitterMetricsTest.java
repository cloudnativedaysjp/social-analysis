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

	@BeforeEach
	public void setup() {
		TweetData tweetData1 = new TweetData("11111", "aaaaa", true, 10);
		TweetData tweetData2 = new TweetData("22222", "bbbbb", false, -10);
		twitterMetrics.setSentimentMetrics(tweetData1);
		twitterMetrics.setSentimentMetrics(tweetData2);
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

}