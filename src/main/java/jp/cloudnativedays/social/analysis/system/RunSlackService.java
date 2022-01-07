package jp.cloudnativedays.social.analysis.system;

import jp.cloudnativedays.social.analysis.service.SlackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@ConditionalOnProperty(value = "slack.enabled", havingValue = "true")
public class RunSlackService implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(RunSlackService.class);

	private final SlackService slackService;

	public RunSlackService(SlackService slackService) {
		this.slackService = slackService;
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Starting Slack metrics service");
		slackService.startMessageMetricsListener();
	}

	@PreDestroy
	public void stopMessageMetricsListener() throws Exception {
		logger.info("Stopping Slack metrics service");
		slackService.stopMessageMetricsListener();
	}

}
