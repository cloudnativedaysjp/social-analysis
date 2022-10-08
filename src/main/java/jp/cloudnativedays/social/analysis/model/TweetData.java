package jp.cloudnativedays.social.analysis.model;

import java.util.Map;

public class TweetData {

	public String tweetId;

	public String queryString;

	public String username;

	public Integer sentimentScore;

	public Integer retweetCount;

	public Integer favoriteCount;

	public TweetData(String tweetId, String queryString, String username) {
		this.tweetId = tweetId;
		this.queryString = queryString;
		this.username = username;

	}

	public String getTweetId() {
		return tweetId;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getUsername() {
		return username;
	}

	public Integer getSentimentScore() {
		return sentimentScore;
	}

	public void setSentimentScore(Integer sentimentScore) {
		this.sentimentScore = sentimentScore;
	}

	public Integer getRetweetCount() {
		return retweetCount;
	}

	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setRetweetCount(Integer retweetCount) {
		this.retweetCount = retweetCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

}
