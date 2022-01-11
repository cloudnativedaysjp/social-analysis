package jp.cloudnativedays.social.analysis.system.utils;

import com.slack.api.bolt.context.builtin.EventContext;
import com.slack.api.bolt.handler.BoltEventHandler;
import com.slack.api.model.event.MessageEvent;
import jp.cloudnativedays.social.analysis.client.SlackClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class TestSlackClient implements SlackClient {

	public TestSlackClient() {
		System.out.println("start slack dummy");
	}

	public String getTeamNameFromEvent(MessageEvent event, EventContext ctx) {
		return "team";
	}

	public String getChannelNameFromEvent(MessageEvent event, EventContext ctx) {
		return "channel";
	}

	public String getUserNameFromEvent(MessageEvent event, EventContext ctx) {
		return "user";
	}

	public void startSocketModeApp() {

	}

	public void setAppMessageEvent(BoltEventHandler<MessageEvent> handler) {

	}

}