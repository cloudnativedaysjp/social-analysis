package jp.cloudnativedays.social.analysis.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordCountTest {

	private WordCount wordCount;

	@BeforeEach
	void setUp() {
		MorphologicalAnalysis morphologicalAnalysis = new MorphologicalAnalysis();
		wordCount = new WordCount(morphologicalAnalysis);
	}

	@Test
	void getWordCount() {
		final String word1 = "今日は、晴れです";
		final Map<String, Integer> expected1 = Map.of("今日", 1, "晴れ", 1);
		assertEquals(expected1, wordCount.countWord(word1));
		final String word2 = "今日の天気は、晴れのち晴れのち晴れです";
		final Map<String, Integer> expected2 = Map.of("今日", 1, "晴れ", 3, "天気", 1, "のち", 2);
		assertEquals(expected2, wordCount.countWord(word2));
		final String word3 = "今日は、晴れです 今日の天気は、晴れのち晴れのち晴れです";
		final Map<String, Integer> expected3 = Map.of("今日", 2, "晴れ", 4, "天気", 1, "のち", 2);
		assertEquals(expected3, wordCount.countWord(word3));
	}

	@Test
	void shouldRemoveUrl() {
		final String word1 = "この商品おすすめです。https://example.com/item/001";
		final Map<String, Integer> expected1 = Map.of("商品", 1, "おすすめ", 1);
		assertEquals(expected1, wordCount.countWord(word1));
		final String word2 = "この商品おすすめです。http://example.com/item/001";
		final Map<String, Integer> expected2 = Map.of("商品", 1, "おすすめ", 1);
		assertEquals(expected2, wordCount.countWord(word2));
	}

	@Test
	void shouldRemoveSymbol() {
		final Map<String, Integer> expected = Map.of("今日", 1, "天気", 1);
		final String word1 = "今日は、いい天気ですね-";
		assertEquals(expected, wordCount.countWord(word1));
		final String word2 = "今日は、いい天気ですね+";
		assertEquals(expected, wordCount.countWord(word2));
		final String word3 = "今日は、いい天気ですね.";
		assertEquals(expected, wordCount.countWord(word3));
		final String word4 = "今日は、いい天気ですね^";
		assertEquals(expected, wordCount.countWord(word4));
		final String word5 = "今日は、いい天気ですね:";
		assertEquals(expected, wordCount.countWord(word5));
		final String word6 = "今日は、いい天気ですね;";
		assertEquals(expected, wordCount.countWord(word6));
		final String word7 = "今日は、いい天気ですね,";
		assertEquals(expected, wordCount.countWord(word7));
		final String word8 = "今日は、いい天気ですね@";
		assertEquals(expected, wordCount.countWord(word8));
		final String word9 = "今日は、いい天気ですね&";
		assertEquals(expected, wordCount.countWord(word9));
		final String word10 = "今日は、いい天気ですね#";
		assertEquals(expected, wordCount.countWord(word10));
		final String word11 = "今日は、いい天気ですね!";
		assertEquals(expected, wordCount.countWord(word11));
		final String word12 = "今日は、いい天気ですね?";
		assertEquals(expected, wordCount.countWord(word12));
		final String word13 = "今日は、いい天気ですね(";
		assertEquals(expected, wordCount.countWord(word13));
		final String word14 = "今日は、いい天気ですね)";
		assertEquals(expected, wordCount.countWord(word14));
		final String word15 = "(今日は、いい天気ですね)";
		assertEquals(expected, wordCount.countWord(word15));
		final String word16 = "「今日は、いい天気ですね」";
		assertEquals(expected, wordCount.countWord(word16));
		final String word17 = "（今日は、いい天気ですね）";
		assertEquals(expected, wordCount.countWord(word17));
		final String word18 = "[今日は、いい天気ですね]";
		assertEquals(expected, wordCount.countWord(word18));
		final String word19 = "_今日は、いい天気ですね";
		assertEquals(expected, wordCount.countWord(word19));
	}

	@Test
	void shouldRemoveEventname() {
		final Map<String, Integer> expected1 = Map.of("今日", 1, "天気", 1);
		final Map<String, Integer> expected2 = Map.of("今日", 1, "天気", 1, "A", 1);
		final String word1 = "#CNDT2022 今日は、いい天気ですね";
		final String word2 = "今日は、いい天気ですね #CNDT2022";
		final String word3 = "今日は、いい天気ですね #CNDT2022 #CNDT2022_A";
		assertEquals(expected1, wordCount.countWord(word1));
		assertEquals(expected1, wordCount.countWord(word2));
		assertEquals(expected2, wordCount.countWord(word3));
	}

}
