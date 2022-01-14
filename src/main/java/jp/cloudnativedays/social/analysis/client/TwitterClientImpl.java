package jp.cloudnativedays.social.analysis.client;

import org.springframework.stereotype.Component;
import twitter4j.*;

@Component
public class TwitterClientImpl implements TwitterClient {

	private Twitter twitter;

	public TwitterClientImpl() {
		this.twitter = getTwitterInstance();
	}

	private Twitter getTwitterInstance() {
		twitter = TwitterFactory.getSingleton();
		return twitter;
	}

	public QueryResult search(Query query) throws TwitterException {
		// Maximize Count numbers
		query.setCount(100);
		return twitter.search(query);
	}

}
