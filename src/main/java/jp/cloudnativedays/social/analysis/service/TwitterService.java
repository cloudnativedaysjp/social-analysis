package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import jp.cloudnativedays.social.analysis.model.TweetData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TwitterService {

    private Sentiment sentiment;
    private MorphologicalAnalysis morphologicalAnalysis;
    private TwitterMetrics twitterMetrics;
    private Twitter twitter;

    public TwitterService(Sentiment sentiment, MorphologicalAnalysis morphologicalAnalysis, TwitterMetrics twitterMetrics) {
        this.sentiment = sentiment;
        this.morphologicalAnalysis = morphologicalAnalysis;
        this.twitterMetrics = twitterMetrics;
        this.twitter = getTwitterInstance();
    }

    private static Twitter getTwitterInstance() {
        Twitter twitter = TwitterFactory.getSingleton();
        return twitter;
    }


    @Value("${twitter.query}")
    String[] queryHashTags;

    public void searchTwitterAndSetMetrics() throws TwitterException {
        System.out.println(queryHashTags);
        for (String queryHashTag: queryHashTags) {

            System.out.println(queryHashTag);
            Query query = new Query(queryHashTag);
            Boolean hasNext = true;

            while (hasNext) {
                QueryResult queryResult = twitter.search(query);
                hasNext = queryResult.hasNext();
                query = queryResult.nextQuery();

                System.out.println(queryResult.getCount());
                System.out.println(queryResult.hasNext());
                System.out.println(queryResult.nextQuery());

                for (Status status : queryResult.getTweets()) {
                    String tweetTxt = status.getText();

                    TweetData tweetData = new TweetData(
                            Long.toString(status.getId()),
                            queryHashTag,
                            status.getText()
                    );
                    if (status.getLang().equals("ja")) {
                        List<Token> tokenList = morphologicalAnalysis.getToken(tweetTxt);
                        int sentiScore = 0;
                        for (Token token : tokenList) {
                            String surface = token.getSurface();
                            sentiScore += sentiment.getSentimentScore(surface);
                        }
                        System.out.println(sentiScore);
                        twitterMetrics.setSentimentMetrics(tweetData, sentiScore);
                    }
                }
            }
        }
    }
}
