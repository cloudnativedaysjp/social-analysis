package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class Sentiment {

	private static final Logger logger = LoggerFactory.getLogger(SentimentLoader.class);

	private final MorphologicalAnalysis morphologicalAnalysis;

	private final Map<ByteBuffer, Integer> sentiMap;

	public Sentiment(SentimentLoader sentimentLoader, MorphologicalAnalysis morphologicalAnalysis) {
		this.morphologicalAnalysis = morphologicalAnalysis;
		this.sentiMap = sentimentLoader.getSentimentMap();
	}

	protected Integer getSentimentScore(String in) {
		byte[] bytes = in.getBytes(StandardCharsets.UTF_8);
		logger.debug("Performing scoring : Original String : " + in + " Encoded String :" + ByteBuffer.wrap(bytes));
		if (sentiMap.containsKey(ByteBuffer.wrap(bytes))) {
			return sentiMap.get(ByteBuffer.wrap(bytes));
		}
		return 0;
	}

	@NewSpan
	public Integer getSentimentScoreFromSentence(String in) {
		List<Token> tokenList = morphologicalAnalysis.getToken(in);
		int sentiScore = 0;
		for (Token token : tokenList) {
			String surface = token.getSurface();
			logger.debug("Surface is : " + surface);
			int Score = getSentimentScore(surface);
			if (Score != 0) {
				logger.debug("Found sentiment score match  : " + " ; Word : " + surface + " Score : " + Score);
			}
			sentiScore += getSentimentScore(surface);
		}
		return sentiScore;
	}

	public Map<String, Integer> countWord(String in) {
		Map<String, Integer> wordCounts = new HashMap<>();
		List<Token> tokenList = morphologicalAnalysis.getToken(in);
		tokenList.stream().filter(e -> Objects.equals(e.getPartOfSpeechLevel1(), "名詞")).map(Token::getSurface)
				.forEach(s -> {
					wordCounts.put(s, wordCounts.getOrDefault(s, 0) + 1);
				});
		return wordCounts;
	}

}
