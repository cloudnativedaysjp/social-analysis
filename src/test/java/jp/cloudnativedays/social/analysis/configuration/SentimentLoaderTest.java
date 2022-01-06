package jp.cloudnativedays.social.analysis.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SentimentLoaderTest {

	private SentimentLoader sentimentLoader;

	private final ClassLoader classLoader = getClass().getClassLoader();

	private final File sentiFile = new File(classLoader.getResource("data/test-data.trim").getFile());

	@BeforeEach
	void setUp() {
		sentimentLoader = new SentimentLoader(sentiFile);
	}

	@Test
	void getSentimentMap() {
		Map<String, Integer> sentiMap = sentimentLoader.getSentimentMap();

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
					assertEquals(score, sentiMap.get(split[0].trim()));
				}
				str = br.readLine();
			}
			br.close();
		}
		catch (IOException e) {
			throw new IllegalStateException(e.toString());
		}
	}

}