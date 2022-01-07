package jp.cloudnativedays.social.analysis.service;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.app_backend.events.payload.MessagePayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.model.event.MessageEvent;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jp.cloudnativedays.social.analysis.configuration.SlackInitializer;
import jp.cloudnativedays.social.analysis.metrics.SlackMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SlackServiceTest {

	private SlackService slackService;

	private final MeterRegistry registry = new SimpleMeterRegistry();

	private final SlackMetrics slackMetrics = new SlackMetrics(registry);

	@Mock
	private SlackInitializer slackInitializer;

	@BeforeEach
	void setUp() {
		slackService = new SlackService(slackInitializer, slackMetrics);
	}

	@Test
	void setMessageMetricsListener() throws Exception {

		EventsApiPayload<MessageEvent> payload = new MessagePayload();
		EventContext context = new EventContext();

		Mockito.when(slackInitializer.getTeamNameFromEvent(any(), any())).thenReturn("team");
		Mockito.when(slackInitializer.getChannelNameFromEvent(any(), any())).thenReturn("channek");
		Mockito.when(slackInitializer.getUserNameFromEvent(any(), any())).thenReturn("user");

		slackService.eventHandler(payload, context);

		assertEquals(1, registry.get("social.slack.messages").meters().size());

	}

}