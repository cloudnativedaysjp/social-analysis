package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MorphologicalAnalysisTest {

	private MorphologicalAnalysis morphologicalAnalysis;

	@BeforeEach
	void setUp() {
		morphologicalAnalysis = new MorphologicalAnalysis();
	}

	@Test
	void getToken() {
		final String testString = "私は、テストです。";
		List<Token> tokens = morphologicalAnalysis.getToken(testString);

		assertEquals("私", tokens.get(0).getSurface());
		assertEquals("は", tokens.get(1).getSurface());
		assertEquals("、", tokens.get(2).getSurface());
		assertEquals("テスト", tokens.get(3).getSurface());
		assertEquals("です", tokens.get(4).getSurface());
		assertEquals("。", tokens.get(5).getSurface());

	}

}