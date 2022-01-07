package jp.cloudnativedays.social.analysis.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SentimentLoaderTest {

	private SentimentLoader sentimentLoader;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(
			Objects.requireNonNull(classLoader.getResource("data/test-data.trim")).getFile());

	@BeforeEach
	void setUp() {
		sentimentLoader = new SentimentLoader(sentiFile);
	}

	@Test
	void getSentimentMap() {
		byte[] bytes;
		Map<ByteBuffer, Integer> sentiMap = sentimentLoader.getSentimentMap();

		bytes = "ていねい".getBytes(StandardCharsets.UTF_8);
		assertEquals(1, sentiMap.get(ByteBuffer.wrap(bytes)));

		bytes = "ずぼら".getBytes(StandardCharsets.UTF_8);
		assertEquals(-1, sentiMap.get(ByteBuffer.wrap(bytes)));

		bytes = "はにかみ".getBytes(StandardCharsets.UTF_8);
		assertEquals(0, sentiMap.get(ByteBuffer.wrap(bytes)));

		bytes = "ぐうたら".getBytes(StandardCharsets.UTF_8);
		assertEquals(0, sentiMap.get(ByteBuffer.wrap(bytes)));
	}

	@Test
	void errorWhenFileDoesNotExist() {
		assertThrows(IllegalStateException.class, () -> {
			File dummy = new File("aaa.txt");
			sentimentLoader = new SentimentLoader(dummy);
			sentimentLoader.getSentimentMap();
		});
	}

}