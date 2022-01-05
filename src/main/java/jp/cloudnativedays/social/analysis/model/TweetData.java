package jp.cloudnativedays.social.analysis.model;

public class TweetData {

	public String tweetId;

	public String queryString;

	public Integer sentimentScore;

	public TweetData(String tweetId, String queryString, Integer sentimentScore) {
		this.tweetId = tweetId;
		this.queryString = queryString;
		this.sentimentScore = sentimentScore;
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
