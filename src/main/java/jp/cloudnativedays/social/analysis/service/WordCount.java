package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
