# Social analysis and metrics exporter based on Spring Boot

This is a simple Java Spring Boot application to perform social analysis. The following technologies are used.

- Spring Boot
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)  
- [Kuromoji](https://www.atilika.com/ja/kuromoji/)  
- [Twitter4j](https://twitter4j.org/en/index.html)

# Features

- Search twitter tweets based on specified query
- Simple scoring based on noun included in the tweet text
- Export metrics via Prometheus format

# Metrics Example   


Metrics is available at `http://:8080/actuator/prometheus`. Two labels will be included for each metrics.   

- `queryString`: The query string used to search tweet
- `tweetId`: Individual tweet id

Each metrics will expose the sentiment score based on the nouns included in the tweet message.

```
social_twitter_sentiment{queryString="#o11y2022",tweedId="1476928217445384194",} 1.0
social_twitter_sentiment{queryString="#o11y2022",tweedId="1476058627333312516",} 0.0
social_twitter_sentiment{queryString="#o11y2022",tweedId="1476044205881716737",} 1.0
social_twitter_sentiment{queryString="#o11y2022",tweedId="1476850968440750084",} 0.0
```

# Prerequisite

- Java 8 (or above)
- [Japanese Sentiment Polarity Dictionary](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html)
- [Twiiter API account](https://developer.twitter.com/en/docs/twitter-api/getting-started/getting-access-to-the-twitter-api)

## Download and placing sentiment dictionary

Download the latest japanese sentiment dictionary from [here](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html). In `application.properties` specify the file path of the file. 
( Or update the value via supported methods in spring boot )

```
sentimentFile=data/pn.csv.m3.120408.trim # < update this 
```

## Create twitter4j.properties file

Create `twitter4j.properties` file based on the following [guide]()