package jp.cloudnativedays.social.analysis;

import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import jp.cloudnativedays.social.analysis.service.MorphologicalAnalysis;
import jp.cloudnativedays.social.analysis.service.TwitterService;
import jp.cloudnativedays.social.analysis.service.Sentiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class TwitterAnalysisApplication implements CommandLineRunner {


    @Autowired
    Sentiment nlp;

    @Autowired
    MorphologicalAnalysis morphologicalAnalysis;

    @Autowired
    TwitterService twitterService;

    @Autowired
    TwitterMetrics twitterMetrics;

    public static void main(String[] args) {
        SpringApplication.run(TwitterAnalysisApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        List<Token> tokenList = morphologicalAnalysis.getToken("今日の発表資料をアップロードしました、ありがとうございました〜！ #CNDT2021");

        int sentiScore = 0;
        for (Token token : tokenList){
            String surface = token.getSurface();
            sentiScore = nlp.getSentimentScore(surface);
        }
        System.out.println(sentiScore);
        twitterService.searchTwitterAndSetMetrics();
        twitterMetrics.getSentimentMetrics();

    }
}
