package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class WordCount {

	private static final Logger logger = LoggerFactory.getLogger(WordCount.class);

	private final MorphologicalAnalysis morphologicalAnalysis;

	public WordCount(MorphologicalAnalysis morphologicalAnalysis) {
		this.morphologicalAnalysis = morphologicalAnalysis;
	}

	private static boolean filterToken(Token token) {
		return Objects.equals(token.getPartOfSpeechLevel1(), "名詞")
				&& !(Objects.equals(token.getPartOfSpeechLevel2(), "非自立")
						|| Objects.equals(token.getPartOfSpeechLevel2(), "接尾")
						|| Objects.equals(token.getPartOfSpeechLevel2(), "代名詞")
						|| Objects.equals(token.getPartOfSpeechLevel2(), "特殊"));
	}

	public Map<String, Integer> countWord(String in) {
		logger.debug(in);
		String normalizedInput = in.replaceAll("https?://\\S+\\s?", "").replaceAll("[-+.^:;,@&#!?()\\[\\]_]", "")
				.replaceAll("[「」（）]", "").replaceAll("CNDT|2022", "");
		logger.debug(normalizedInput);
		Map<String, Integer> wordCounts = new HashMap<>();
		List<Token> tokenList = morphologicalAnalysis.getToken(normalizedInput);

		tokenList.stream().filter(WordCount::filterToken).map(Token::getSurface).forEach(s -> {
			wordCounts.put(s, wordCounts.getOrDefault(s, 0) + 1);
		});
		return wordCounts;
	}

}
