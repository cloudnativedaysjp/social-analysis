package jp.cloudnativedays.social.analysis.model;

public class TweetData {
    public String tweetId;
    public String queryHashTag;
    public String tweetTxt;

    public TweetData(String tweetId, String queryHashTag, String tweetTxt) {
        this.tweetId = tweetId;
        this.queryHashTag = queryHashTag;
        this.tweetTxt = tweetTxt;
    }


    public String getTweetId() {
        return tweetId;
    }

    public String getQueryHashTag() {
        return queryHashTag;
    }

    public String getTweetTxt() {
        return tweetTxt;
    }
}
