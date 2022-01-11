package jp.cloudnativedays.social.analysis.system.utils;

import jp.cloudnativedays.social.analysis.client.TwitterClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
@Primary
public class TestTwitterClient implements TwitterClient {

	public TestTwitterClient() {
		System.out.println("start twitter dummy");
	}

	public QueryResult search(Query query) {

		List<Status> statuses = new ArrayList<>();
		User user = mock(User.class);
		when(user.getScreenName()).thenReturn("tanaka");

		Status positiveStatus = mock(Status.class);
		when(positiveStatus.getId()).thenReturn(100L);
		when(positiveStatus.getText()).thenReturn("かれはていねいです");
		when(positiveStatus.getLang()).thenReturn("ja");
		when(positiveStatus.getUser()).thenReturn(user);
		when(positiveStatus.isRetweet()).thenReturn(false);
		when(positiveStatus.getRetweetCount()).thenReturn(10);
		when(positiveStatus.getFavoriteCount()).thenReturn(20);

		statuses.add(positiveStatus);

		QueryResult queryResult = mock(QueryResult.class);
		when(queryResult.getTweets()).thenReturn(statuses);
		when(queryResult.hasNext()).thenReturn(false);
		when(queryResult.nextQuery()).thenReturn(null);
		return queryResult;
	}

}