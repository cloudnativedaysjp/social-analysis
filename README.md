# Social analysis and metrics exporter based on Spring Boot

This is a simple Java Spring Boot application to perform social analysis. The following technologies are used.

- Spring Boot
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)  
- [Kuromoji](https://www.atilika.com/ja/kuromoji/)  
- [Twitter4j](https://twitter4j.org/en/index.html)
- [java-slack-sdk](https://github.com/slackapi/java-slack-sdk)

# Features
- Export metrics via Prometheus format
## Twitter Features
- Search Twitter tweets based on specified query
- Collect retweet and favorite counts  
- Simple scoring based on noun included in the tweet text
## Slack Features
- Collect number of messages and convert to countner

# Metrics Example   

Metrics is available at `http://:8080/actuator/prometheus`. 

## Twitter Example

The following labels will be included for each metric.   

- `queryString`The query string used to search tweet
- `tweetId`Individual tweet id
- `screenName` Screen name of the tweet
- `words` Words in tweet

Each metric will expose the sentiment score based on the nouns included in the tweet message, retweet counts, and favorite counts.

```
social_twitter_retweets{queryString="#o11y2022",screenName="cloudnativedays",tweetId="1477776728152240129",} 4.0
social_twitter_retweets{queryString="#o11y2022",screenName="cloudnativedays",tweetId="1476023933744910340",} 6.0
social_twitter_sentiment{queryString="#o11y2022",screenName="cloudnativedays",tweetId="1477776728152240129",} 0.0
social_twitter_sentiment{queryString="#o11y2022",screenName="cloudnativedays",tweetId="1476023933744910340",} 1.0
social_twitter_favorites{queryString="#o11y2022",screenName="cloudnativedays",tweetId="1477776728152240129",} 5.0
social_twitter_favorites{queryString="#o11y2022",screenName="cloudnativedays",tweetId="1476023933744910340",} 9.0
social_twitter_words{words="NewSQL",} 3.0
social_twitter_words{words="CNDT",} 2.0
```

## Slack Example

The following labels will be included for each metric.

- `workspaceName`The workspace name
- `channelName`The channel name
- `screenName` Screen name of the slack user

```
social_slack_messages_total{channelName="o11y-team-notification",screenName="machih",workspaceName="cloudnativedays",} 3.0
```


# Prerequisite

- Java 8 (or above)
- [Japanese Sentiment Polarity Dictionary](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html)
- [Twitter API account](https://developer.twitter.com/en/docs/twitter-api/getting-started/getting-access-to-the-twitter-api)
- [Slack App Registry](https://api.slack.com/apps)

## Download and placing sentiment dictionary

Download the latest japanese sentiment dictionary from [here](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html). In `application.properties` specify the file path of the file. 
( Or update the value via supported methods in spring boot )

```
sentimentFile=data/pn.csv.m3.120408.trim # < update this 
```

## Create twitter4j.properties file

Create `twitter4j.properties` file based on the following [guide](https://twitter4j.org/en/configuration.html)

## Set up Apps Environment values Slack API Token

The following values need to be defined in environment values

```
SLACK_APP_TOKEN = the slack app token (xapp-)
SLACK_BOT_TOKEN = the slack bot token (xoxb-)
```

# Running

Execute the following command to run on local

```
./mvnw spring-boot:run
```

# Containerize

Execute the following to containerize

```
./mvnw spring-boot:build-image
```