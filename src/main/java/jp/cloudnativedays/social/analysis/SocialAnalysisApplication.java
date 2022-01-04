package jp.cloudnativedays.social.analysis;

import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import jp.cloudnativedays.social.analysis.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SocialAnalysisApplication implements CommandLineRunner{

    @Autowired
    TwitterService twitterService;

    @Autowired
    TwitterMetrics twitterMetrics;

    public static void main(String[] args) {
        SpringApplication.run(SocialAnalysisApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("first run");
        twitterService.searchTwitterAndSetMetrics();
        System.out.println("second run");
        twitterService.searchTwitterAndSetMetrics();

    }
}
