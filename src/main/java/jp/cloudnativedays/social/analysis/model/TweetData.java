package jp.cloudnativedays.social.analysis.model;

public class TweetData {

	public String tweetId;

	public String queryString;

	public boolean isRetweet;

	public Integer sentimentScore;

	public TweetData(String tweetId, String queryString, boolean isRetweet, Integer sentimentScore) {
		this.tweetId = tweetId;
		this.queryString = queryString;
		this.isRetweet = isRetweet;
		this.sentimentScore = sentimentScore;
	}

	public boolean isRetweet() {
		return isRetweet;
	}

	public String getTweetId() {
		return tweetId;
	}

	public String getQueryString() {
		return queryString;
	}

	public Integer getSentimentScore() {
		return sentimentScore;
	}

}
