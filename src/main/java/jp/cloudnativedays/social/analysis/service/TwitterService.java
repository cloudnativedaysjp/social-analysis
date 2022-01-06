package jp.cloudnativedays.social.analysis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import jp.cloudnativedays.social.analysis.model.TweetData;
import org.springframework.beans.factory.annotation.Autowired;
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

	private final String[] queryStrings;

	private Twitter twitter;

	@Autowired
	public TwitterService(Sentiment sentiment, MorphologicalAnalysis morphologicalAnalysis,
			TwitterMetrics twitterMetrics, @Value("${twitter.query}") String[] queryStrings) {
		this.sentiment = sentiment;
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.twitterMetrics = twitterMetrics;
		this.twitter = getTwitterInstance();
		this.queryStrings = queryStrings;
	}

	public TwitterService(Sentiment sentiment, MorphologicalAnalysis morphologicalAnalysis,
			TwitterMetrics twitterMetrics, @Value("${twitter.query}") String[] queryStrings, Twitter twitter) {
		this.sentiment = sentiment;
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.twitterMetrics = twitterMetrics;
		this.twitter = twitter;
		this.queryStrings = queryStrings;
	}

	private Twitter getTwitterInstance() {
		twitter = TwitterFactory.getSingleton();
		return twitter;
	}

	public void searchTwitterAndSetMetrics() throws TwitterException {

		for (String queryString : queryStrings) {
			Query query = new Query(queryString + " -filter:retweets");
			boolean hasNext = true;

			while (hasNext) {

				QueryResult queryResult = twitter.search(query);
				hasNext = queryResult.hasNext();
				query = queryResult.nextQuery();

				logger.info("Performed Twitter Query : QueryString '" + queryString + "': "
						+ queryResult.getTweets().size() + " items found " + ": hasNextPage is " + hasNext);

				for (Status status : queryResult.getTweets()) {
					if (status.isRetweet()) {
						continue;
					}
					TweetData tweetData = new TweetData(Long.toString(status.getId()), queryString,
							status.getUser().getScreenName());
					tweetData.setSentimentScore(0);
					tweetData.setRetweetCount(status.getRetweetCount());
					tweetData.setFavoriteCount(status.getFavoriteCount());

					if (!status.isRetweet() && !twitterMetrics.isSentimentSet(tweetData)) {
						String tweetTxt = status.getText();
						if (status.getLang().equals("ja")) {
							logger.debug("Sentiment Check on tweet : " + tweetTxt);
							List<Token> tokenList = morphologicalAnalysis.getToken(tweetTxt);
							int sentiScore = 0;
							for (Token token : tokenList) {
								String surface = token.getSurface();
								logger.debug("Surface is : "+ surface);
								int Score = sentiment.getSentimentScore(surface);
								if (Score != 0) {
									logger.debug("Found sentiment score match in tweetID  : " + status.getId()
											+ " ; Word : " + surface + " Score : " + Score);
								}
								sentiScore += sentiment.getSentimentScore(surface);
							}
							tweetData.setSentimentScore(sentiScore);
						}
					}
					twitterMetrics.setMetrics(tweetData);
				}
			}
		}
	}

}
