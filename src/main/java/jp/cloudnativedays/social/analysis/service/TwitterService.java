package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterService {

    private Sentiment sentiment;
    private MorphologicalAnalysis morphologicalAnalysis;
    private Twitter twitter;
    private TwitterMetrics twitterMetrics;

    public TwitterService(Sentiment sentiment, MorphologicalAnalysis morphologicalAnalysis, Twitter twitter, TwitterMetrics twitterMetrics) {
        this.sentiment = sentiment;
        this.morphologicalAnalysis = morphologicalAnalysis;
        this.twitter = twitter;
        this.twitterMetrics = twitterMetrics;
    }

    @Value("${twitter.query}")
    String queryTag;

    public Long getTwitterData(Long sinceId) {

        SearchParameters searchParameters = new SearchParameters(queryTag);
        searchParameters.sinceId(sinceId);

        SearchResults results = twitter.searchOperations().search(searchParameters);
        SearchMetadata searchMetadata= results.getSearchMetadata();

        List<Tweet> tweets = results.getTweets();

        twitterMetrics.incrementTotalTweets(tweets.size());

        for (Tweet tweet: tweets){
            String tweetTxt = tweet.getText();
            //System.out.println(tweetTxt);

            if (tweet.getLanguageCode().equals("ja")) {
                List<Token> tokenList = morphologicalAnalysis.getToken(tweetTxt);

                int sentiScore = 0;
                for (Token token : tokenList) {
                    String surface = token.getSurface();
                    sentiScore += sentiment.getSentimentScore(surface);
                }
                twitterMetrics.setSentimentScore(sentiScore);
            }
        }
        System.out.println(twitterMetrics.getSentimentScore());
        System.out.println(twitterMetrics.getTotalTweets());
        return searchMetadata.getMaxId();
    }
}
