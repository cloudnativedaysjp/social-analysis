package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.*;
import jp.cloudnativedays.social.analysis.model.TweetData;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TwitterMetrics {
    private MeterRegistry meterRegistry;
    private String metricsName = "social.twitter.sentiment";
    private Map<String, Integer> gaugeCache = new HashMap<>();

    public TwitterMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void setSentimentMetrics(TweetData tweetData){
        gaugeCache.put(tweetData.getTweetId(),tweetData.getSentimentScore());
        meterRegistry.gauge(
                    metricsName,
                    Tags.of(
                            Tag.of("hashTag", tweetData.getQueryHashTag()),
                            Tag.of("tweetId",tweetData.getTweetId())
                    ),
                    gaugeCache,
                    g -> g.get(tweetData.getTweetId())
            );
    }

    public void getSentimentMetrics(){
        for(Meter meter : meterRegistry.getMeters()){
            System.out.println(meter.getId());
        }
    }
}
