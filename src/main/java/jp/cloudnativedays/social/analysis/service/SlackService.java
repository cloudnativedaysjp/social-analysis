package jp.cloudnativedays.social.analysis.service;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.event.MessageEvent;
import jp.cloudnativedays.social.analysis.client.SlackClient;
import jp.cloudnativedays.social.analysis.metrics.SlackMetrics;
import jp.cloudnativedays.social.analysis.model.SlackData;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "slack.enabled", havingValue = "true")
public class SlackService {

	private final SlackClient slackClient;

	private final SlackMetrics slackMetrics;

	private final Tracer tracer;

	public SlackService(SlackClient slackClient, SlackMetrics slackMetrics, Tracer tracer) {
		this.slackClient = slackClient;
		this.slackMetrics = slackMetrics;
		this.tracer = tracer;
	}

	public Response eventHandler(EventsApiPayload<MessageEvent> payload, EventContext ctx) {

		ScopedSpan span = tracer.startScopedSpan("Message Event");

		String teamName = slackClient.getTeamNameFromEvent(payload.getEvent(), ctx);

		String channelName = slackClient.getChannelNameFromEvent(payload.getEvent(), ctx);

		String userName = slackClient.getUserNameFromEvent(payload.getEvent(), ctx);

		SlackData slackData = new SlackData(teamName, channelName, userName);

		slackMetrics.setMessageMetrics(slackData);

		span.end();

		return ctx.ack();
	}

	public void startMessageMetricsListener() throws Exception {
		slackClient.setAppMessageEvent(this::eventHandler);
		slackClient.startSocketModeApp();
	}

}
