package jp.cloudnativedays.social.analysis.configuration;

import com.slack.api.app_backend.events.payload.EventsApiPayload;
import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.event.MessageEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SlackInitializerTest {

	private SlackInitializer slackInitializer;

	@BeforeEach
	void setUp() throws IOException {
		slackInitializer = new SlackInitializer("dummy-slack-app-token", "dummy-slack-bot-token");
	}

	@Test
	void startSocketModeApp() {
		String message = assertThrows(IllegalStateException.class, () -> slackInitializer.startSocketModeApp())
				.getMessage();

		assertEquals("Failed to connect to the Socket Mode endpoint URL (error: invalid_auth)", message);
	}

	@Test
	void setAppMessageEvent() {
		slackInitializer.setAppMessageEvent(this::eventHandler);
	}

	@Test
	void getTeamNameFromEvent() throws IOException {
		MessageEvent payload = new MessageEvent();
		EventContext ctx = new EventContext();
		assertEquals("", slackInitializer.getTeamNameFromEvent(payload, ctx));
	}

	@Test
	void getChannelNameFromEvent() throws IOException {
		MessageEvent payload = new MessageEvent();
		EventContext ctx = new EventContext();
		assertEquals("", slackInitializer.getChannelNameFromEvent(payload, ctx));
	}

	@Test
	void getUserNameFromEvent() throws IOException {
		MessageEvent payload = new MessageEvent();
		EventContext ctx = new EventContext();
		assertEquals("", slackInitializer.getUserNameFromEvent(payload, ctx));
	}

	public Response eventHandler(EventsApiPayload<MessageEvent> payload, EventContext ctx) {
		// Do nothing.
		return ctx.ack();
	}

}