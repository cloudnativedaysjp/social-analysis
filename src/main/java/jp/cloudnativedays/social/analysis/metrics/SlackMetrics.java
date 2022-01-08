package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import jp.cloudnativedays.social.analysis.model.SlackData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SlackMetrics {

	private final MeterRegistry meterRegistry;

	private static final String METRICS_PREFIX = "social.slack.";

	private static final String WORKSPACE_NAME = "workspaceName";

	private static final String CHANNEL_NAME = "channelName";

	private static final String USER_NAME = "screenName";

	private final Map<String, Counter> slackCounterCache = new HashMap<>();

	public SlackMetrics(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	public void setMessageMetrics(SlackData slackData) {
		String channelUser = slackData.getChannel() + slackData.getUser();
		Counter counter = slackCounterCache.get(channelUser);
		if (counter == null) {
			counter = meterRegistry.counter(METRICS_PREFIX + "messages",
					Tags.of(Tag.of(WORKSPACE_NAME, slackData.getTeam()), Tag.of(CHANNEL_NAME, slackData.getChannel()),
							Tag.of(USER_NAME, slackData.getUser())));
			slackCounterCache.put(channelUser, counter);
		}
		counter.increment();
	}

}
