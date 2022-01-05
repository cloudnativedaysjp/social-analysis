package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.*;
import jp.cloudnativedays.social.analysis.model.TweetData;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TwitterMetrics {

	private final MeterRegistry meterRegistry;

	private static final String METRICS_NAME = "social.twitter.sentiment";

	private static final String QUERY_STRING = "queryString";

	private static final String IS_RETWEET = "isRetweet";

	private static final String TWEET_ID = "tweetId";

	private final Map<String, Integer> gaugeCache = new HashMap<>();

	public TwitterMetrics(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	public void setSentimentMetrics(TweetData tweetData) {
		gaugeCache.put(tweetData.getTweetId(), tweetData.getSentimentScore());
		meterRegistry.gauge(METRICS_NAME,
				Tags.of(Tag.of(QUERY_STRING, tweetData.getQueryString()), Tag.of(TWEET_ID, tweetData.getTweetId()),
						Tag.of(IS_RETWEET, String.valueOf(tweetData.isRetweet()))),
				gaugeCache, g -> g.get(tweetData.getTweetId()));
	}



}
