package jp.cloudnativedays.social.analysis.client;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterException;

public interface TwitterClient {

	@NewSpan
	QueryResult search(Query query) throws TwitterException;

}
