package jp.cloudnativedays.social.analysis.service;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.app_backend.events.payload.MessagePayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.model.event.MessageEvent;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jp.cloudnativedays.social.analysis.client.SlackClient;
import jp.cloudnativedays.social.analysis.metrics.SlackMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SlackServiceTest {

	private SlackService slackService;

	private final MeterRegistry registry = new SimpleMeterRegistry();

	private final SlackMetrics slackMetrics = new SlackMetrics(registry);

	@Mock
	private Tracer tracer;

	@Mock
	private SlackClient slackClient;

	@BeforeEach
	void setUp() {
		slackService = new SlackService(slackClient, slackMetrics, tracer);
	}

	@Test
	void setMessageMetricsListener() {

		EventsApiPayload<MessageEvent> payload = new MessagePayload();
		EventContext context = new EventContext();

		ScopedSpan span = mock(ScopedSpan.class);

		Mockito.when(tracer.startScopedSpan(any())).thenReturn(span);
		Mockito.when(slackClient.getTeamNameFromEvent(any(), any())).thenReturn("team");
		Mockito.when(slackClient.getChannelNameFromEvent(any(), any())).thenReturn("channek");
		Mockito.when(slackClient.getUserNameFromEvent(any(), any())).thenReturn("user");

		slackService.eventHandler(payload, context);

		assertEquals(1, registry.get("social.slack.messages").meters().size());

	}

}