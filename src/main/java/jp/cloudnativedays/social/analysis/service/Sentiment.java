package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;

@Service
public class Sentiment {

	private static final Logger logger = LoggerFactory.getLogger(SentimentLoader.class);

	SentimentLoader sentimentLoader;

	Map<String, Integer> sentiMap;

	public Sentiment(SentimentLoader sentimentLoader) {
		this.sentimentLoader = sentimentLoader;
		this.sentiMap = sentimentLoader.getSentimentMap();
	}

	public Integer getSentimentScore(String in) {
		String encodedString = Base64.getEncoder().encodeToString(in.getBytes());
		logger.debug("Performing scoring : Original String : " + in + " Encoded String :" + encodedString);
		if (sentiMap.containsKey(encodedString)) {
			return sentiMap.get(encodedString);
		}
		return 0;
	}

}
