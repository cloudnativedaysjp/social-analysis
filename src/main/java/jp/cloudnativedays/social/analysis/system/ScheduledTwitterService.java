package jp.cloudnativedays.social.analysis.system;

import jp.cloudnativedays.social.analysis.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

@Component
@ConditionalOnProperty(value = "twitter.enabled", havingValue = "true")
public class ScheduledTwitterService {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTwitterService.class);

	private final TwitterService twitterService;

	public ScheduledTwitterService(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	@Scheduled(fixedDelayString = "${twitter.interval}")
	public void performTwitterSearch() throws TwitterException {
		logger.info("Performing scheduled base search");
		twitterService.searchTwitterAndSetMetrics();
	}

}
