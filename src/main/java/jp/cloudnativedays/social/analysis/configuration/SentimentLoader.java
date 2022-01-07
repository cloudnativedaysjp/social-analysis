package jp.cloudnativedays.social.analysis.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SentimentLoader {

	private static final Logger logger = LoggerFactory.getLogger(SentimentLoader.class);

	private final File sentimentFile;

	public SentimentLoader(@Value("${sentiment.file}") File sentimentFile) {
		this.sentimentFile = sentimentFile;
	}

	public Map<ByteBuffer, Integer> getSentimentMap() {
		Map<ByteBuffer, Integer> sentiMap = new HashMap<>();

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(sentimentFile), StandardCharsets.UTF_8));

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

					byte[] bytes = split[0].trim().getBytes(StandardCharsets.UTF_8);
					logger.trace("The score for word " + split[0].trim() + "encoded string is '"
							+ ByteBuffer.wrap(bytes) + "' was saved as : " + sentiScore);

					sentiMap.put(ByteBuffer.wrap(bytes), sentiScore);
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
