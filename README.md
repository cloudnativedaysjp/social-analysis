# Social analysis and metrics exporter based on Spring Boot

This is a simple Java Spring Boot application to perform social analysis. The following technologies are used.

- Spring Boot
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)  
- [Kuromoji](https://www.atilika.com/ja/kuromoji/)  
- [Twitter4j](https://twitter4j.org/en/index.html)

# Features

- Search Twitter tweets based on specified query
- Simple scoring based on noun included in the tweet text
- Export metrics via Prometheus format

# Metrics Example   


Metrics is available at `http://:8080/actuator/prometheus`. The following labels will be included for each metric.   

- `isRetweet` Indicates whether a tweet is a retweet
- `queryString`The query string used to search tweet
- `tweetId`Individual tweet id

Each metric will expose the sentiment score based on the nouns included in the tweet message.

```
social_twitter_sentiment{isRetweet="true",queryString="#o11y2022",tweetId="1476205741967294466",} 1.0
social_twitter_sentiment{isRetweet="true",queryString="#o11y2022",tweetId="1478281008890134528",} 0.0
social_twitter_sentiment{isRetweet="false",queryString="#o11y2022",tweetId="1476927383177412610",} 1.0
social_twitter_sentiment{isRetweet="false",queryString="#o11y2022",tweetId="1476085397315002369",} 0.0
```

# Prerequisite

- Java 8 (or above)
- [Japanese Sentiment Polarity Dictionary](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html)
- [Twitter API account](https://developer.twitter.com/en/docs/twitter-api/getting-started/getting-access-to-the-twitter-api)

## Download and placing sentiment dictionary

Download the latest japanese sentiment dictionary from [here](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html). In `application.properties` specify the file path of the file. 
( Or update the value via supported methods in spring boot )

```
sentimentFile=data/pn.csv.m3.120408.trim # < update this 
```

## Create twitter4j.properties file

Create `twitter4j.properties` file based on the following [guide](https://twitter4j.org/en/configuration.html)