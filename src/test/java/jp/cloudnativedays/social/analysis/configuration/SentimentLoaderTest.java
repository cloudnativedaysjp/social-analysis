package jp.cloudnativedays.social.analysis.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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
		Map<ByteBuffer, Integer> sentiMap = sentimentLoader.getSentimentMap();

		try {
			BufferedReader br = new BufferedReader(new FileReader(sentiFile));
			String str = br.readLine();
			while (str != null) {
				String[] split = str.split("\t");
				if (split.length > 1) {
					int score = 0;

					switch (split[1].trim()) {
					case "e":
						score = 0;
						break;
					case "?p?n":
						score = 0;
						break;
					case "p":
						score = 1;
						break;
					case "n":
						score = -1;
						break;
					}
					byte[] bytes = split[0].trim().getBytes(StandardCharsets.UTF_8);
					assertEquals(score, sentiMap.get(ByteBuffer.wrap(bytes)));
				}
				str = br.readLine();
			}
			br.close();
		}
		catch (IOException e) {
			throw new IllegalStateException(e.toString());
		}
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