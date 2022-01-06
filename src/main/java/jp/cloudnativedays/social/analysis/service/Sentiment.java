package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class Sentiment {

	private static final Logger logger = LoggerFactory.getLogger(SentimentLoader.class);

	SentimentLoader sentimentLoader;

	Map<ByteBuffer, Integer> sentiMap;

	public Sentiment(SentimentLoader sentimentLoader) {
		this.sentimentLoader = sentimentLoader;
		this.sentiMap = sentimentLoader.getSentimentMap();
	}

	public Integer getSentimentScore(String in) {
		byte[] bytes = in.getBytes(StandardCharsets.UTF_8);
		logger.debug("Performing scoring : Original String : " + in + " Encoded String :" + ByteBuffer.wrap(bytes));
		if (sentiMap.containsKey(ByteBuffer.wrap(bytes))) {
			return sentiMap.get(ByteBuffer.wrap(bytes));
		}
		return 0;
	}

}
