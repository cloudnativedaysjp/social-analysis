package jp.cloudnativedays.social.analysis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetDataTest {

	private TweetData tweetData;

	@BeforeEach
	void setup() {
		tweetData = new TweetData("11111", "aaaaa", "john");
		tweetData.setSentimentScore(10);
		tweetData.setRetweetCount(5);
		tweetData.setFavoriteCount(2);
	}

	@Test
	void getTweetId() {
		String ans = "11111";
		assertEquals(ans, tweetData.getTweetId());
	}

	@Test
	void getQueryString() {
		String ans = "aaaaa";
		assertEquals(ans, tweetData.getQueryString());
	}

	@Test
	void getUsername() {
		String ans = "john";
		assertEquals(ans, tweetData.getUsername());
	}

	@Test
	void getSentimentScore() {
		Integer ans = 10;
		assertEquals(ans, tweetData.getSentimentScore());
	}

	@Test
	void getRetweetCount() {
		Integer ans = 5;
		assertEquals(ans, tweetData.getRetweetCount());
	}

	@Test
	void getFavoriteCount() {
		Integer ans = 2;
		assertEquals(ans, tweetData.getFavoriteCount());
	}

}