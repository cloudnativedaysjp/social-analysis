package jp.cloudnativedays.social.analysis.service;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.event.MessageEvent;
import jp.cloudnativedays.social.analysis.configuration.SlackInitializer;
import jp.cloudnativedays.social.analysis.metrics.SlackMetrics;
import jp.cloudnativedays.social.analysis.model.SlackData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@ConditionalOnProperty(value = "slack.enabled", havingValue = "true")
public class SlackService {

	private final SlackInitializer slackInitializer;

	private final SlackMetrics slackMetrics;

	public SlackService(SlackInitializer slackInitializer, SlackMetrics slackMetrics) {
		this.slackInitializer = slackInitializer;
		this.slackMetrics = slackMetrics;
	}

	protected Response eventHandler(EventsApiPayload<MessageEvent> payload, EventContext ctx) throws IOException {

		String teamName = slackInitializer.getTeamNameFromEvent(payload.getEvent(), ctx);

		String channelName = slackInitializer.getChannelNameFromEvent(payload.getEvent(), ctx);

		String userName = slackInitializer.getUserNameFromEvent(payload.getEvent(), ctx);

		SlackData slackData = new SlackData(teamName, channelName, userName);

		slackMetrics.setMessageMetrics(slackData);

		return ctx.ack();
	}

	public void startMessageMetricsListener() throws Exception {
		slackInitializer.setAppMessageEvent(this::eventHandler);
		slackInitializer.startSocketModeApp();
	}

	public void stopMessageMetricsListener() throws Exception {
		slackInitializer.stopSocketModeApp();
	}

}
