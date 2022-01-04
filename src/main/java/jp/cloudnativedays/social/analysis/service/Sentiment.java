package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Sentiment {

    SentimentLoader sentimentLoader;
    Map<String, Integer> sentiMap;

    public Sentiment(SentimentLoader sentimentLoader) {
        this.sentimentLoader = sentimentLoader;
        this.sentiMap = sentimentLoader.getSentimentMap();
    }

    public Integer getSentimentScore(String in){
        if (sentiMap.containsKey(in)){
            return sentiMap.get(in);
        }
        return 0;
    }
}
