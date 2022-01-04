package jp.cloudnativedays.social.analysis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SentimentLoader {
    @Value("classpath:${sentimentFile}")
    File sentimentFile;

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
                    } else if (emotion.equals("n")) {
                        sentiScore = -1; // nの場合-1
                    }
                    sentiMap.put(split[0].trim(), sentiScore);
                }
                str = br.readLine();
            }
            br.close();
        }catch (IOException e){
            throw new IllegalStateException("Failed to open sentiment file");
        }
        return sentiMap;
    }

}
