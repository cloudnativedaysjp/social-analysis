package jp.cloudnativedays.social.analysis.model;

public class TweetData {

	public String tweetId;

	public String queryString;

	public boolean isRetweet;

	public Integer sentimentScore;

	public Integer retweetCount;

	public Integer favoriteCount;

	public TweetData(String tweetId, String queryString, boolean isRetweet) {
		this.tweetId = tweetId;
		this.queryString = queryString;
		this.isRetweet = isRetweet;
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
