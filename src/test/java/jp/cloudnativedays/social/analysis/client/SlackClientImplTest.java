package jp.cloudnativedays.social.analysis.client;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.event.MessageEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SlackClientImplTest {

	private SlackClient slackClient;

	@BeforeEach
	void setUp() {
		slackClient = new SlackClientImpl("dummy-slack-app-token", "dummy-slack-bot-token");
	}

	@Test
	void startSocketModeApp() {
		String message = assertThrows(IllegalStateException.class, () -> slackClient.startSocketModeApp()).getMessage();

		assertEquals("Failed to connect to the Socket Mode endpoint URL (error: invalid_auth)", message);
	}

	@Test
	void setAppMessageEvent() {
		slackClient.setAppMessageEvent(this::eventHandler);
	}

	@Test
	void getTeamNameFromEvent() {
		MessageEvent payload = new MessageEvent();
		EventContext ctx = new EventContext();
		assertEquals("", slackClient.getTeamNameFromEvent(payload, ctx));
	}

	@Test
	void getChannelNameFromEvent() {
		MessageEvent payload = new MessageEvent();
		EventContext ctx = new EventContext();
		assertEquals("", slackClient.getChannelNameFromEvent(payload, ctx));
	}

	@Test
	void getUserNameFromEvent() {
		MessageEvent payload = new MessageEvent();
		EventContext ctx = new EventContext();
		assertEquals("", slackClient.getUserNameFromEvent(payload, ctx));
	}

	public Response eventHandler(EventsApiPayload<MessageEvent> payload, EventContext ctx) {
		// Do nothing.
		return ctx.ack();
	}

}