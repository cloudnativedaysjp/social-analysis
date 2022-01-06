package jp.cloudnativedays.social.analysis.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SentimentLoader {

	private static final Logger logger = LoggerFactory.getLogger(SentimentLoader.class);

	private final File sentimentFile;

	public SentimentLoader(@Value("${sentimentFile}") File sentimentFile) {
		this.sentimentFile = sentimentFile;
	}

	public Map<String, Integer> getSentimentMap() {
		Map<String, Integer> sentiMap = new HashMap<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(sentimentFile));
			String str = br.readLine();
			while (str != null) {
				String[] split = str.split("\t");
				if (split.length > 1) {
					String emotion = split[1].trim();
					int sentiScore = 0;
					if (emotion.equals("p")) {
						sentiScore = 1; // pの場合+1
					}
					else if (emotion.equals("n")) {
						sentiScore = -1; // nの場合-1
					}
					String encodedString = Base64.getEncoder().encodeToString(split[0].trim().getBytes());
					logger.trace("The score for word " + split[0].trim() + "encoded string is '" + encodedString
							+ "' was saved as : " + sentiScore);

					sentiMap.put(encodedString, sentiScore);
				}
				str = br.readLine();
			}
			logger.debug("The size of dictionary is " + sentiMap.size());
			br.close();
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to open sentiment file");
		}
		return sentiMap;
	}

}
