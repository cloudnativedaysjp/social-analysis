package jp.cloudnativedays.social.analysis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TweetDataTest {

	private TweetData tweetData;

	@BeforeEach
	void setup() {
		tweetData = new TweetData("11111", "aaaaa", true);
		tweetData.setSentimentScore(10);
		tweetData.setRetweetCount(5);
		tweetData.setFavoriteCount(2);
	}

	@Test
	void isRetweet() {
		boolean ans = true;
		assertEquals(ans, tweetData.isRetweet());
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