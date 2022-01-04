package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jp.cloudnativedays.social.analysis.model.TweetData;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TwitterMetrics {
    private MeterRegistry meterRegistry;
    private TweetData tweetData;
    private String metricsName = "social.twitter.sentiment";

    public TwitterMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void setSentimentMetrics(TweetData tweetData, Integer i){

        AtomicInteger sentimentScore = (AtomicInteger) meterRegistry.find(metricsName).
                tag("tweetID", tweetData.getTweetId()).
                gauge();
        if(sentimentScore == null){
            sentimentScore = meterRegistry.gauge(
                    metricsName,
                    Tags.concat(Tags.empty(),
                            "hashTag", tweetData.getQueryHashTag(),
                            "tweetId",tweetData.getTweetId()
                    ),
                    new AtomicInteger()
            );
        }
        sentimentScore.getAndAdd(i);
    }

    public void getSentimentMetrics(){
        for(Meter meter : meterRegistry.getMeters()){
            System.out.println(meter.getId());
        }
    }

}
