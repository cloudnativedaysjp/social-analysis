package jp.cloudnativedays.social.analysis.model;

public class TweetData {
    public String tweetId;
    public String queryHashTag;
    public Integer sentimentScore;

    public TweetData(String tweetId, String queryHashTag, String tweetTxt, Integer sentimentScore) {
        this.tweetId = tweetId;
        this.queryHashTag = queryHashTag;
        this.sentimentScore = sentimentScore;
    }

    public String getTweetId() {
        return tweetId;
    }

    public String getQueryHashTag() {
        return queryHashTag;
    }

    public Integer getSentimentScore() {
        return sentimentScore;
    }
}
