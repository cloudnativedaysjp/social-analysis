package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TwitterMetrics {
    private MeterRegistry meterRegistry;

    private Counter totalTweets;
    private AtomicInteger sentimentScore;
    private String metrics_prefix = "social.twitter";

    private Map<Integer, Counter> metricCounters = null;

    public TwitterMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        initMetrics();
    }

    public void initMetrics(){
        totalTweets = meterRegistry.counter(metrics_prefix + "total_tweets");
        sentimentScore = meterRegistry.gauge(metrics_prefix + "sentiment_score" , new AtomicInteger());
    }

    public void incrementTotalTweets(double d){
        totalTweets.increment(d);
    }

    public void setSentimentScore(int i){
        sentimentScore.getAndAdd(i);
    }

    public double getTotalTweets(){
        return totalTweets.count();
    }

    public int getSentimentScore(){
        return sentimentScore.get();
    }
}
