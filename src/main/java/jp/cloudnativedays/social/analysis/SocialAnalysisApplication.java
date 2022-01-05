package jp.cloudnativedays.social.analysis;

import jp.cloudnativedays.social.analysis.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialAnalysisApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SocialAnalysisApplication.class);

	private final TwitterService twitterService;

	public SocialAnalysisApplication(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SocialAnalysisApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		logger.info("Perform initial twitter search");
		twitterService.searchTwitterAndSetMetrics();
		logger.info("Initial twitter search is completed.");
	}

}
