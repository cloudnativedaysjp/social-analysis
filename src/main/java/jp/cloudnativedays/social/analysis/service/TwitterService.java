package jp.cloudnativedays.social.analysis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import jp.cloudnativedays.social.analysis.model.TweetData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.util.List;

@Service
public class TwitterService {

	private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

	private final Sentiment sentiment;

	private final MorphologicalAnalysis morphologicalAnalysis;

	private final TwitterMetrics twitterMetrics;

	private Twitter twitter;

	private Long maxId = 0L;

	public TwitterService(Sentiment sentiment, MorphologicalAnalysis morphologicalAnalysis,
			TwitterMetrics twitterMetrics) {
		this.sentiment = sentiment;
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.twitterMetrics = twitterMetrics;
		this.twitter = getTwitterInstance();
	}

	private Twitter getTwitterInstance() {
		twitter = TwitterFactory.getSingleton();
		return twitter;
	}

	@Value("${twitter.query}")
	String[] queryStrings;

	public void searchTwitterAndSetMetrics() throws TwitterException {

		for (String queryString : queryStrings) {
			Query query = new Query(queryString);
			query.setSinceId(maxId);
			boolean hasNext = true;

			while (hasNext) {

				QueryResult queryResult = twitter.search(query);
				hasNext = queryResult.hasNext();
				query = queryResult.nextQuery();

				if (maxId < queryResult.getMaxId())
					maxId = queryResult.getMaxId();

				logger.info("Performed Twitter Query : QueryString '" + queryString + "': "
						+ queryResult.getTweets().size() + " items found " + ": hasNextPage is " + hasNext);

				for (Status status : queryResult.getTweets()) {
					String tweetTxt = status.getText();
					if (status.getLang().equals("ja")) {
						List<Token> tokenList = morphologicalAnalysis.getToken(tweetTxt);
						int sentiScore = 0;
						for (Token token : tokenList) {
							String surface = token.getSurface();
							sentiScore += sentiment.getSentimentScore(surface);
						}
						TweetData tweetData = new TweetData(Long.toString(status.getId()), queryString, sentiScore);
						twitterMetrics.setSentimentMetrics(tweetData);
					}
				}
			}
		}
	}

}
